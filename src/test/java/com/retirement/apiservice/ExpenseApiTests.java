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
import com.retirement.apiservice.entity.Expense;

import net.minidev.json.JSONArray;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExpenseApiTests {
	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void shouldReturnTheRequestedExpense() throws IOException {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/expenses/1001", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int userId = documentContext.read("$.userId");
		assertThat(userId).isEqualTo(101);

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("Electricity Bill");

		int amount = documentContext.read("$.amount");
		assertThat(amount).isEqualTo(200);

		int yearlyFrequency = documentContext.read("$.frequencyPerYear");
		assertThat(yearlyFrequency).isEqualTo(12);
	}

	@Test
	void shouldNotReturnAnotherRetireesExpense() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/expenses/2001", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldReturnExpenseList() throws IOException {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/expenses?userId=101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int totalExpenses = documentContext.read("$.length()");
		assertThat(totalExpenses).isEqualTo(1);

		JSONArray userIds = documentContext.read("$..userId");
		assertThat(userIds).containsExactly(101);

		JSONArray names = documentContext.read("$..name");
		assertThat(names).containsExactly("Electricity Bill");

		JSONArray amounts = documentContext.read("$..amount");
		assertThat(amounts).containsExactly(200);

		JSONArray returnFrequencies = documentContext.read("$..frequencyPerYear");
		assertThat(returnFrequencies).containsExactly(12);
	}

	@Test
	void shouldNotReturnAnotherRetireesExpenseList() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/expenses?userId=102", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldCreateExpense() {
		Expense newExpense = new Expense(101, "Internet Bill", 75, 12);
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/expenses", newExpense,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI newExpenseLocation = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity(newExpenseLocation, String.class);
		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	void shouldNotCreateInvalidExpense() {
		// name validation
		Expense newExpense = new Expense(101, "", 0, 0);
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/expenses", newExpense,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("name");

		String message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must not be blank");

		// amount validation
		newExpense = new Expense(101, "John's Bill", -1, 0);
		response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/expenses", newExpense,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		documentContext = JsonPath.parse(response.getBody());

		name = documentContext.read("$.name");
		assertThat(name).isEqualTo("amount");

		message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must be greater than or equal to 0");

		// frequency validation
		newExpense = new Expense(101, "John's Bill", 100, -1);
		response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/expenses", newExpense,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		documentContext = JsonPath.parse(response.getBody());

		name = documentContext.read("$.name");
		assertThat(name).isEqualTo("frequencyPerYear");

		message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must be greater than or equal to 0");
	}

	@Test
	void shouldNotCreateExpenseForAnotherRetiree() {
		Expense newExpense = new Expense(102, "An Expense for Jane", 0, 0);
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/expenses", newExpense,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldUpdateExpense() {
		Expense newExpense = new Expense(1001, 101, "Electricity", 150, 12);
		HttpEntity<Expense> updateRequest = new HttpEntity<>(newExpense);
		ResponseEntity<Void> putResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/expenses/1001", HttpMethod.PUT, updateRequest,
						Void.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/expenses/1001", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int userId = documentContext.read("$.userId");
		assertThat(userId).isEqualTo(101);

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("Electricity");

		int amount = documentContext.read("$.amount");
		assertThat(amount).isEqualTo(150);
	}

	@Test
	void putShouldNotCreateExpense() {
		Expense newExpense = new Expense(1020, 101, "John's Expense", 1000, 12);
		HttpEntity<Expense> putCreateRequest = new HttpEntity<>(newExpense);
		ResponseEntity<Void> putResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/expenses/1020", HttpMethod.PUT, putCreateRequest,
						Void.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/expenses/1020", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotUpdateExpenseWithInvalidInput() {
		// name
		Expense newExpense = new Expense(1001, 101, "", 1, 1);
		HttpEntity<Expense> updateRequest = new HttpEntity<>(newExpense);
		ResponseEntity<String> putResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/expenses/1001", HttpMethod.PUT, updateRequest,
						String.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		DocumentContext documentContext = JsonPath.parse(putResponse.getBody());

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("name");

		String message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must not be blank");

		// amount
		newExpense = new Expense(1001, 101, "Electricity", -1, 1);
		updateRequest = new HttpEntity<>(newExpense);
		putResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/expenses/1001", HttpMethod.PUT, updateRequest,
						String.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		documentContext = JsonPath.parse(putResponse.getBody());

		name = documentContext.read("$.name");
		assertThat(name).isEqualTo("amount");

		message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must be greater than or equal to 0");

		// frequency
		newExpense = new Expense(1001, 101, "Electricity", 1, -1);
		updateRequest = new HttpEntity<>(newExpense);
		putResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/expenses/1001", HttpMethod.PUT, updateRequest,
						String.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		documentContext = JsonPath.parse(putResponse.getBody());

		name = documentContext.read("$.name");
		assertThat(name).isEqualTo("frequencyPerYear");

		message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must be greater than or equal to 0");
	}

	@Test
	void shouldNotUpdateExpenseForAnotherRetiree() {
		Expense newExpense = new Expense(1001, 101, "Electricity", 10000, 12);
		HttpEntity<Expense> updateRequest = new HttpEntity<>(newExpense);
		ResponseEntity<Void> putResponse = restTemplate.withBasicAuth("jane.doe@example.com", "janespassword")
				.exchange("/retirement/api/expenses/1001", HttpMethod.PUT, updateRequest,
						Void.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldDeleteExpense() {
		ResponseEntity<Void> deleteResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/expenses/1001", HttpMethod.DELETE, null,
						Void.class);

		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/expenses/1001", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotDeleteExpenseForAnotherRetiree() {
		ResponseEntity<Void> deleteResponse = restTemplate.withBasicAuth("jane.doe@example.com", "janespassword")
				.exchange("/retirement/api/expenses/1001", HttpMethod.DELETE, null,
						Void.class);

		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
