package com.retirement.apiservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.retirement.apiservice.entity.IncomeSource;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EstimateApiTests {
	@Autowired
	TestRestTemplate restTemplate;

	@Test
	@DirtiesContext
	void shouldReturnRetirementEstimate() throws IOException {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/estimates/101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int userId = documentContext.read("$.userId");
		assertThat(userId).isEqualTo(101);

		int monthlyIncomeAvailable = documentContext.read("$.monthlyIncomeAvailable");
		assertThat(monthlyIncomeAvailable).isEqualTo(95);

		int monthlyExpenses = documentContext.read("$.monthlyExpenses");
		assertThat(monthlyExpenses).isEqualTo(200);

		int disposableIncome = documentContext.read("$.monthlyDisposable");
		assertThat(disposableIncome).isEqualTo(-105);

		boolean onTrack = documentContext.read("$.onTrack");
		assertThat(onTrack).isEqualTo(false);

		int monthlyToSave = documentContext.read("$.monthlyToSave");
		assertThat(monthlyToSave).isEqualTo(2105);

		int totalAdditionalSavings = documentContext.read("$.totalAdditionalSavings");
		assertThat(totalAdditionalSavings).isEqualTo(757800);

		// since the retiree is not on track for savings, add an income source with the
		// total amount of additional savings calculated to achieve their goal then
		// retrieve a new estimate after the update
		IncomeSource newIncomeSource = new IncomeSource(101, "Cash Savings Account", totalAdditionalSavings, 0, 0);
		response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/incomesources", newIncomeSource,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// request an updated estimate
		response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/estimates/101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		// the new estimate validates that the progress calculation is accurate
		documentContext = JsonPath.parse(response.getBody());

		disposableIncome = documentContext.read("$.monthlyDisposable");
		assertThat(disposableIncome).isEqualTo(2000);

		onTrack = documentContext.read("$.onTrack");
		assertThat(onTrack).isEqualTo(true);
	}

}
