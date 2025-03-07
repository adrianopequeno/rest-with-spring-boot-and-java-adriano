package br.com.adriano.integrationtests.controllers;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.List;

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
	
	@Test
	@Order(3)
	@DisplayName("JUnit integration Test Given Person Object when findById Should Return a Person Object")
	void integrationTestGivenPersonObject_when_FindById_ShouldReturnAPersonObject() throws JsonMappingException, JsonProcessingException {
		
		var content = given().spec(specification)
				.pathParam("id", person.getId())
				.when()
					.get("{id}")
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();
		
		Person foundPerson = objectMapper.readValue(content, Person.class);
		
		assertNotNull(foundPerson);
		assertNotNull(foundPerson.getId());
		assertNotNull(foundPerson.getFirstName());
		assertNotNull(foundPerson.getLastName());
		assertNotNull(foundPerson.getAddress());
		assertNotNull(foundPerson.getGender());
		assertNotNull(foundPerson.getEmail());
		
		assertTrue(foundPerson.getId() > 0);
		assertEquals("Ciclano", foundPerson.getFirstName());
		assertEquals("da Silva", foundPerson.getLastName());
		assertEquals("Patos - Paraiba - Brasil", foundPerson.getAddress());
		assertEquals("Male", foundPerson.getGender());
		assertEquals("ciclano@teste.com.br", foundPerson.getEmail());
	}
	
	@Test
	@Order(3)
	@DisplayName("JUnit integration Test Given Persons Object when findAll Should Return a Persons List")
	void integrationTestGivenPersonsObject_when_FindAll_ShouldReturnAPersonsList() throws JsonMappingException, JsonProcessingException {
		
		// instacio uma nova pessoa
		Person anotherPerson = new Person("Beltrano", "da Silva", "Patos - Paraiba - Brasil", "Female", "bel@hotmail.com");
		
		// crio uma nova pessoa
		given().spec(specification)
			.contentType(TestConfig.CONTENT_TYPE_JSON)
			.body(anotherPerson)
			.when()
				.post()
			.then()
				.statusCode(200);
		
		// recupero a lista de pessoas
		var content = given().spec(specification)
				.when()
					.get()
				.then()
					.statusCode(200)
						.extract()
							.body()
								.asString();
		
		// converto a lista de pessoas para um array
		Person[] myArray = objectMapper.readValue(content, Person[].class);
		
		// converto o array para uma lista de pessoas
		List<Person> peaples = Arrays.asList(myArray);
		
		// verifico se a lista de pessoas não está vazia
		assertNotNull(peaples);
		assertTrue(peaples.size() > 0);
		
		Person foundPersonOne = peaples.get(0);
		assertNotNull(foundPersonOne);
		assertNotNull(foundPersonOne.getId());
		assertNotNull(foundPersonOne.getFirstName());
		assertNotNull(foundPersonOne.getLastName());
		assertNotNull(foundPersonOne.getAddress());
		assertNotNull(foundPersonOne.getGender());
		assertNotNull(foundPersonOne.getEmail());
		
		assertTrue(foundPersonOne.getId() > 0);
		assertEquals("Ciclano", foundPersonOne.getFirstName());
		assertEquals("da Silva", foundPersonOne.getLastName());
		assertEquals("Patos - Paraiba - Brasil", foundPersonOne.getAddress());
		assertEquals("Male", foundPersonOne.getGender());
		assertEquals("ciclano@teste.com.br", foundPersonOne.getEmail());
		
		// verifico se a lista de pessoas contém a pessoa que criei
		Person foundPersonTwo = peaples.get(1);
		assertNotNull(foundPersonTwo);
		assertNotNull(foundPersonTwo.getId());
		assertNotNull(foundPersonTwo.getFirstName());
		assertNotNull(foundPersonTwo.getLastName());
		assertNotNull(foundPersonTwo.getAddress());
		assertNotNull(foundPersonTwo.getGender());
		assertNotNull(foundPersonTwo.getEmail());
		
		assertTrue(foundPersonTwo.getId() > 0);
		assertEquals("Beltrano", foundPersonTwo.getFirstName());
		assertEquals("da Silva", foundPersonTwo.getLastName());
		assertEquals("Patos - Paraiba - Brasil", foundPersonTwo.getAddress());
		assertEquals("Female", foundPersonTwo.getGender());
		assertEquals("bel@hotmail.com", foundPersonTwo.getEmail());
		
	}

}
