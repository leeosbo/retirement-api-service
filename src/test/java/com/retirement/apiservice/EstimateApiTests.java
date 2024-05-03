package com.retirement.apiservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EstimateApiTests {
	@Autowired
	TestRestTemplate restTemplate;

	@Test
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
	}

}
