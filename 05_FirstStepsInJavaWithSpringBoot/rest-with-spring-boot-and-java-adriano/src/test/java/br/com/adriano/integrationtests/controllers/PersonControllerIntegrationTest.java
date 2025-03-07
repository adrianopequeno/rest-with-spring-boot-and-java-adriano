package br.com.adriano.integrationtests.controllers;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.adriano.config.TestConfig;
import br.com.adriano.integrationtests.testcontainers.AbstractIntegrationTest;
import br.com.adriano.models.Person;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;

@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class PersonControllerIntegrationTest extends AbstractIntegrationTest {

	private static RequestSpecification specification;
	private static ObjectMapper objectMapper;
	private static Person person;
	
	@BeforeAll
	public static void setUp() {
		// Given / Arrange
		objectMapper = new ObjectMapper();
		objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
		
		specification = new RequestSpecBuilder()
				.setBasePath("/person")
				.setPort(TestConfig.SERVER_PORT)
				.addFilter(new RequestLoggingFilter(LogDetail.ALL))
				.addFilter(new ResponseLoggingFilter(LogDetail.ALL))
				.build();
		
		person = new Person("Fulano", "da Silva", "Patos - Paraiba - Brasil", "Male", "fulano@gmail.com");
	}
	
	@Test
	@Order(1)
	@DisplayName("JUnit integration Test Given Person Object when Create One Person Should Return Person Object")
	void integrationTestGivenPersonObject_when_CreateOndePerson_ShouldReturnPersonObject() throws JsonMappingException, JsonProcessingException {
		
		var content = given()
				.spec(specification)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
				.body(person)
				.when()
					.post()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();
		
		Person createdPerson = objectMapper.readValue(content, Person.class);
		
		person = createdPerson;
		
		assertNotNull(createdPerson);
		assertNotNull(createdPerson.getId());
		assertNotNull(createdPerson.getFirstName());
		assertNotNull(createdPerson.getLastName());
		assertNotNull(createdPerson.getAddress());
		assertNotNull(createdPerson.getGender());
		assertNotNull(createdPerson.getEmail());
		
		assertTrue(createdPerson.getId() > 0);
		assertEquals("Fulano", createdPerson.getFirstName());
		assertEquals("da Silva", createdPerson.getLastName());
		assertEquals("Patos - Paraiba - Brasil", createdPerson.getAddress());
		assertEquals("Male", createdPerson.getGender());
		assertEquals("fulano@gmail.com", createdPerson.getEmail());
	}
	
	@Test
	@Order(2)
	@DisplayName("JUnit integration Test Given Person Object when Update One Person Should Return Updated Person Object")
	void integrationTestGivenPersonObject_when_UpdateOndePerson_ShouldReturnUpdatedPersonObject() throws JsonMappingException, JsonProcessingException {
		
		person.setFirstName("Ciclano");
		person.setEmail("ciclano@teste.com.br");
		
		var content = given().spec(specification)
				.contentType(TestConfig.CONTENT_TYPE_JSON)
				.body(person)
				.when()
					.put()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();
		
		Person updatedPerson = objectMapper.readValue(content, Person.class);
		
		person = updatedPerson;
		
		assertNotNull(updatedPerson);
		assertNotNull(updatedPerson.getId());
		assertNotNull(updatedPerson.getFirstName());
		assertNotNull(updatedPerson.getLastName());
		assertNotNull(updatedPerson.getAddress());
		assertNotNull(updatedPerson.getGender());
		assertNotNull(updatedPerson.getEmail());
		
		assertTrue(updatedPerson.getId() > 0);
		assertEquals("Ciclano", updatedPerson.getFirstName());
		assertEquals("da Silva", updatedPerson.getLastName());
		assertEquals("Patos - Paraiba - Brasil", updatedPerson.getAddress());
		assertEquals("Male", updatedPerson.getGender());
		assertEquals("ciclano@teste.com.br", updatedPerson.getEmail());
	}

}
