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

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApiServiceApplicationTests {
	@Autowired
	TestRestTemplate restTemplate;

	@Test
	void shouldReturnUserIfAuthenticated() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/users/101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int id = documentContext.read("$.user_id");
		assertThat(id).isEqualTo(101);
		String firstName = documentContext.read("$.first_name");
		assertThat(firstName).isEqualTo("John");
		String lastName = documentContext.read("$.last_name");
		assertThat(lastName).isEqualTo("Doe");
		String email = documentContext.read("$.email");
		assertThat(email).isEqualTo("john.doe@example.com");
	}

	@Test
	void shouldReturnRetireeIfAuthenticated() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("john.doe@example.com", "johnspassword")
				.getForEntity("/retirement/api/retirees/101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
		DocumentContext documentContext = JsonPath.parse(response.getBody());
		int id = documentContext.read("$.user_id");
		assertThat(id).isEqualTo(101);
		String firstName = documentContext.read("$.first_name");
		assertThat(firstName).isEqualTo("John");
		String lastName = documentContext.read("$.last_name");
		assertThat(lastName).isEqualTo("Doe");
		String email = documentContext.read("$.email");
		assertThat(email).isEqualTo("john.doe@example.com");
		String dateOfBirth = documentContext.read("$.date_of_birth");
		assertThat(dateOfBirth).isEqualTo("1990-03-16");
		String retirementDate = documentContext.read("$.retirement_date");
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-01");
		String retireDate = LocalDate.now().plusMonths(370).format(dateFormatter);
		assertThat(retirementDate).isEqualTo(retireDate);
		int retirementYears = documentContext.read("$.retirement_years");
		assertThat(retirementYears).isEqualTo(30);
	}

	@Test
	void shouldNotReturnAnotherUser() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("jane.doe@example.com", "janespassword")
				.getForEntity("/retirement/api/users/101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotReturnAnotherRetiree() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("jane.doe@example.com", "janespassword")
				.getForEntity("/retirement/api/retirees/101", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void shouldNotAllowAccessWhenRetireeHasNoAuthority() {
		ResponseEntity<String> response = restTemplate.withBasicAuth("jack.doe@example.com", "jackspassword")
				.getForEntity("/retirement/api/retirees/103", String.class);

		assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
	}
}
