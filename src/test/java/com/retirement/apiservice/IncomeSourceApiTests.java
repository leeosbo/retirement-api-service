package com.retirement.apiservice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.retirement.apiservice.entity.IncomeSource;

import net.minidev.json.JSONArray;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class IncomeSourceApiTests {
	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void shouldReturnTheRequestedIncomeSource() throws IOException {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/incomesources/101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int userId = documentContext.read("$.userId");
		assertThat(userId).isEqualTo(101);

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("Money Market Savings");

		int accountBalance = documentContext.read("$.accountBalance");
		assertThat(accountBalance).isEqualTo(10000);

		double returnRate = documentContext.read("$.returnRate");
		assertThat(returnRate).isEqualTo(4.0);

		int returnFrequency = documentContext.read("$.returnFrequency");
		assertThat(returnFrequency).isEqualTo(12);
	}

	@Test
	void shouldNotReturnAnotherRetireesIncomeSource() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/incomesources/102", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldReturnIncomeSourceList() throws IOException {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/incomesources?user=101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int totalIncomeSources = documentContext.read("$.length()");
		assertThat(totalIncomeSources).isEqualTo(1);

		JSONArray userIds = documentContext.read("$..userId");
		assertThat(userIds).containsExactly(101);

		JSONArray names = documentContext.read("$..name");
		assertThat(names).containsExactly("Money Market Savings");

		JSONArray accountBalances = documentContext.read("$..accountBalance");
		assertThat(accountBalances).containsExactly(10000);

		JSONArray returnRates = documentContext.read("$..returnRate");
		assertThat(returnRates).containsExactly(4.0);

		JSONArray returnFrequencies = documentContext.read("$..returnFrequency");
		assertThat(returnFrequencies).containsExactly(12);
	}

	@Test
	void shouldNotReturnAnotherRetireesIncomeSourceList() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/incomesources?user=102", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldCreateIncomeSource() {
		IncomeSource newIncomeSource = new IncomeSource(101, "ROTH IRA", 0, 0, 0);
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/incomesources", newIncomeSource,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		// test that the returned location header is accurate for the new income source
		URI newIncomeSourceLocation = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity(newIncomeSourceLocation, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldNotCreateInvalidIncomeSource() {
		IncomeSource newIncomeSource = new IncomeSource(101, "", 0, 0, 0);
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/incomesources", newIncomeSource,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("name");

		String message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must not be blank");
	}

	@Test
	void shouldNotCreateIncomeSourceForAnotherRetiree() {
		IncomeSource newIncomeSource = new IncomeSource(102, "An Income Source for Jane", 0, 0, 0);
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/incomesources", newIncomeSource,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldUpdateIncomeSource() {
		IncomeSource newIncomeSource = new IncomeSource(101, 101, "Money Market Savings", 15000, 4.0, 12);
		HttpEntity<IncomeSource> updateRequest = new HttpEntity<>(newIncomeSource);
		ResponseEntity<Void> putResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/incomesources/101", HttpMethod.PUT, updateRequest, Void.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/incomesources/101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int userId = documentContext.read("$.userId");
		assertThat(userId).isEqualTo(101);

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("Money Market Savings");

		int accountBalance = documentContext.read("$.accountBalance");
		assertThat(accountBalance).isEqualTo(15000);
	}

	@Test
	void putShouldNotCreateIncomeSource() {
		IncomeSource newIncomeSource = new IncomeSource(120, 101, "John's Annuity", 1000, 4.0, 12);
		HttpEntity<IncomeSource> putCreateRequest = new HttpEntity<>(newIncomeSource);
		ResponseEntity<Void> putResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/incomesources/120", HttpMethod.PUT, putCreateRequest, Void.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/incomesources/120", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotUpdateIncomeSourceWithInvalidInput() {
		IncomeSource newIncomeSource = new IncomeSource(101, 101, "Money Market Savings", -1, 4.0, 12);
		HttpEntity<IncomeSource> updateRequest = new HttpEntity<>(newIncomeSource);
		ResponseEntity<String> putResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/incomesources/101", HttpMethod.PUT, updateRequest, String.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		DocumentContext documentContext = JsonPath.parse(putResponse.getBody());

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("accountBalance");

		String message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must be greater than or equal to 0");
	}

	@Test
	void shouldNotUpdateIncomeSourceForAnotherRetiree() {
		IncomeSource newIncomeSource = new IncomeSource(101, 101, "Money Market Savings", 10000, 4.0, 12);
		HttpEntity<IncomeSource> updateRequest = new HttpEntity<>(newIncomeSource);
		ResponseEntity<Void> putResponse = restTemplate.withBasicAuth("jane.doe@example.com", "janespassword")
				.exchange("/retirement/api/incomesources/101", HttpMethod.PUT, updateRequest, Void.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldDeleteIncomeSource() {
		ResponseEntity<Void> deleteResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/incomesources/101", HttpMethod.DELETE, null, Void.class);

		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/incomesources/101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotDeleteIncomeSourceForAnotherRetiree() {
		ResponseEntity<Void> deleteResponse = restTemplate.withBasicAuth("jane.doe@example.com", "janespassword")
				.exchange("/retirement/api/incomesources/101", HttpMethod.DELETE, null, Void.class);

		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
