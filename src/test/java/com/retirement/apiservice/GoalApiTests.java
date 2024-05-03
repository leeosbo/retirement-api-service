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
import com.retirement.apiservice.entity.Goal;

import net.minidev.json.JSONArray;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;
import java.net.URI;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class GoalApiTests {
	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void shouldReturnTheRequestedGoal() throws IOException {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/goals/1001", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int userId = documentContext.read("$.userId");
		assertThat(userId).isEqualTo(101);

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("Primary Goal");

		int disposableIncome = documentContext.read("$.disposableIncome");
		assertThat(disposableIncome).isEqualTo(2000);

		int frequency = documentContext.read("$.frequency");
		assertThat(frequency).isEqualTo(12);
	}

	@Test
	void shouldNotReturnAnotherRetireesGoal() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/goals/2001", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldReturnGoalList() throws IOException {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/goals?user=101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int totalgoals = documentContext.read("$.length()");
		assertThat(totalgoals).isEqualTo(2);

		JSONArray userIds = documentContext.read("$..userId");
		assertThat(userIds).containsExactly(101, 101);

		JSONArray names = documentContext.read("$..name");
		assertThat(names).containsExactly("Primary Goal", "Secondary Goal");

		JSONArray disposableIncomes = documentContext.read("$..disposableIncome");
		assertThat(disposableIncomes).containsExactly(2000, 3000);

		JSONArray returnFrequencies = documentContext.read("$..frequency");
		assertThat(returnFrequencies).containsExactly(12, 1);

		JSONArray primaryGoals = documentContext.read("$..primaryGoal");
		assertThat(primaryGoals).containsExactly(true, false);
	}

	@Test
	void shouldNotReturnAnotherRetireesGoalList() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/goals?user=102", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldCreateGoal() {
		Goal newGoal = new Goal(101, "Another Goal", 150, 3, false);
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/goals", newGoal,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);

		URI newGoalLocation = response.getHeaders().getLocation();
		ResponseEntity<String> getResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity(newGoalLocation, String.class);

		assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
	}

	@Test
	@DirtiesContext
	void shouldNotCreateInvalidGoal() {
		// name validation
		Goal newGoal = new Goal(101, "", 10, 52, false);
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/goals", newGoal,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("name");

		String message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must not be blank");

		// disposableIncome validation
		newGoal = new Goal(101, "John's Bill", 0, 52, false);
		response = restTemplate.withBasicAuth("john.doe@example.com",
				"johnspassword")
				.postForEntity("/retirement/api/goals", newGoal,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		documentContext = JsonPath.parse(response.getBody());

		name = documentContext.read("$.name");
		assertThat(name).isEqualTo("disposableIncome");

		message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must be greater than 0");

		// frequency validation
		newGoal = new Goal(101, "John's Bill", 100, -1, false);
		response = restTemplate.withBasicAuth("john.doe@example.com",
				"johnspassword")
				.postForEntity("/retirement/api/goals", newGoal,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		documentContext = JsonPath.parse(response.getBody());

		name = documentContext.read("$.name");
		assertThat(name).isEqualTo("frequency");

		message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must be greater than 0");
	}

	@Test
	@DirtiesContext
	void shouldNotCreateGoalForAnotherRetiree() {
		Goal newGoal = new Goal(102, "A Goal for Jane", 100, 52, true);
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.postForEntity("/retirement/api/goals", newGoal,
						String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldUpdateGoal() {
		Goal newGoal = new Goal(1001, 101, "Primary Goal", 1500, 12, true);
		HttpEntity<Goal> updateRequest = new HttpEntity<>(newGoal);
		ResponseEntity<Void> putResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/goals/1001", HttpMethod.PUT, updateRequest,
						Void.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/goals/1001", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());

		int userId = documentContext.read("$.userId");
		assertThat(userId).isEqualTo(101);

		int disposableIncome = documentContext.read("$.disposableIncome");
		assertThat(disposableIncome).isEqualTo(1500);
	}

	@Test
	void putShouldNotCreateGoal() {
		Goal newGoal = new Goal(1020, 101, "John's goal", 1000, 12, false);
		HttpEntity<Goal> putCreateRequest = new HttpEntity<>(newGoal);
		ResponseEntity<Void> putResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/goals/1020", HttpMethod.PUT, putCreateRequest,
						Void.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);

		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/goals/1020", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotUpdategoalWithInvalidInput() {
		// name
		Goal newGoal = new Goal(1001, 101, "", 1, 1, false);
		HttpEntity<Goal> updateRequest = new HttpEntity<>(newGoal);
		ResponseEntity<String> putResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/goals/1001", HttpMethod.PUT, updateRequest,
						String.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		DocumentContext documentContext = JsonPath.parse(putResponse.getBody());

		String name = documentContext.read("$.name");
		assertThat(name).isEqualTo("name");

		String message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must not be blank");

		// disposableIncome
		newGoal = new Goal(1001, 101, "Primary Goal", -1, 1, true);
		updateRequest = new HttpEntity<>(newGoal);
		putResponse = restTemplate.withBasicAuth("john.doe@example.com",
				"johnspassword")
				.exchange("/retirement/api/goals/1001", HttpMethod.PUT, updateRequest,
						String.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		documentContext = JsonPath.parse(putResponse.getBody());

		name = documentContext.read("$.name");
		assertThat(name).isEqualTo("disposableIncome");

		message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must be greater than 0");

		// frequency
		newGoal = new Goal(1001, 101, "Primary Goal", 1, -1, true);
		updateRequest = new HttpEntity<>(newGoal);
		putResponse = restTemplate.withBasicAuth("john.doe@example.com",
				"johnspassword")
				.exchange("/retirement/api/goals/1001", HttpMethod.PUT, updateRequest,
						String.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

		documentContext = JsonPath.parse(putResponse.getBody());

		name = documentContext.read("$.name");
		assertThat(name).isEqualTo("frequency");

		message = documentContext.read("$.message");
		assertThat(message).isEqualTo("must be greater than 0");
	}

	@Test
	void shouldNotUpdateGoalForAnotherRetiree() {
		Goal newGoal = new Goal(1001, 101, "Primary Goal", 10000, 12, false);
		HttpEntity<Goal> updateRequest = new HttpEntity<>(newGoal);
		ResponseEntity<Void> putResponse = restTemplate.withBasicAuth("jane.doe@example.com", "janespassword")
				.exchange("/retirement/api/goals/1001", HttpMethod.PUT, updateRequest,
						Void.class);

		assertThat(putResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	@DirtiesContext
	void shouldDeleteGoal() {
		ResponseEntity<Void> deleteResponse = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.exchange("/retirement/api/goals/1001", HttpMethod.DELETE, null,
						Void.class);

		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);

		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/goals/1001", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotDeleteGoalForAnotherRetiree() {
		ResponseEntity<Void> deleteResponse = restTemplate.withBasicAuth("jane.doe@example.com", "janespassword")
				.exchange("/retirement/api/goals/1001", HttpMethod.DELETE, null,
						Void.class);

		assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}
}
