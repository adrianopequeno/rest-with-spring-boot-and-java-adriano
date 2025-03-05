package br.com.adriano.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.adriano.models.Person;
import br.com.adriano.services.PersonServices;

@WebMvcTest(PersonController.class)
public class PersonControllerWithMockitoBeanTest {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	
	@MockitoBean
	private PersonServices service;
	
	private Person person;
	private Person person1;

	@BeforeEach
	void setUp() {
		// Given / Arrange
		person = new Person();
		person.setFirstName("Fulano");
		person.setLastName("Santana");
		person.setAddress("Rua 1");
		person.setGender("Male");
		person.setEmail("fulano@gmail.com");
		
		person1 = new Person("Ciclano", "Araujo", "Recife - Pernambuco - Brasil", "Female", "ciclano@hotmail.com");
	}
	
	@Test
	@DisplayName("JUnit test Given Person Object when Create Person then Return Saved Person")
	void testGivenPersonObject_WhenCreatePerson_thenReturnSavedPerson() throws JsonProcessingException, Exception {
		// Given / Arrange
		given(service.create(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));
		
		// When / Act
		ResultActions response = mockMvc.perform(post("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(person)));
				
		// Then / Assert
		response.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName", is(person.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(person.getLastName())))
			.andExpect(jsonPath("$.address", is(person.getAddress())));
	}
	
	@Test
	@DisplayName("JUnit test Given of Persons when findAll Persons then Return Persons List")
	void testGivenListOfPersons_WhenFindAllPersons_thenReturnPersonList() throws JsonProcessingException, Exception {
		// Given / Arrange
		List<Person> persons = Arrays.asList(person, person1);
		given(service.findAll()).willReturn(persons);
		
		// When / Act
		ResultActions response = mockMvc.perform(get("/person"));
		
		// Then / Assert
		response.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.size()", is(persons.size())));
	}

	@Test
	@DisplayName("JUnit test Given Person ID when findById then Return Person Object")
	void testGivenPersonId_WhenFindById_thenReturnPersonObject() throws JsonProcessingException, Exception {
		// Given / Arrange
		long personId = 1L;
		given(service.findById(anyLong())).willReturn(person);
		
		// When / Act
		ResultActions response = mockMvc.perform(get("/person/{id}", personId));
				
		// Then / Assert
		response
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.firstName", is(person.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(person.getLastName())))
			.andExpect(jsonPath("$.address", is(person.getAddress())));
	}
}
