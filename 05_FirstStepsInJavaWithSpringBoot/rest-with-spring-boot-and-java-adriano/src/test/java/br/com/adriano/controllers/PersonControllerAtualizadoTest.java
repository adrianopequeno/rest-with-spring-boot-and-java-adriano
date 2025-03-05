package br.com.adriano.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.adriano.exceptions.ResourceNotFoundException;
import br.com.adriano.models.Person;
import br.com.adriano.services.PersonServices;

@ExtendWith(MockitoExtension.class) // Habilita mocks sem precisar do contexto Spring
class PersonControllerAtualizadoTest {
	
	@Autowired
	private MockMvc mockMvc;
	@Autowired
	private ObjectMapper mapper;
	
	@Mock
	private PersonServices service;
	
	@InjectMocks
	private PersonController controller;
	
	private Person person;
	private Person person1;
	
	@BeforeEach
	void setUp() {
		// Configura MockMvc manualmente, sem carregar o contexto Spring
		 mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
		 mapper = new ObjectMapper();
		 
		 // Given / Arrange
		 person = new Person();
		 person.setFirstName("Fulano");
		 person.setLastName("Santana");
		 person.setAddress("Rua 1");
		 person.setGender("Male");
		 person.setEmail("fulano@teste.com.br");
		 
		 person1 = new Person("Ciclano", "Araujo", "Recife - Pernambuco - Brasil","Female", "ciclano@teste.com");
	}

	@Test
	@DisplayName("JUnit test Given Person Object when Create Person then Return Saved Person")
	void testGivenPersonObject_WhenCreatePerson_thenReturnSavedPerson() throws JsonProcessingException, Exception {
		// Given / Arrange
		given(service.create(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));
		
		// When / Act
		ResultActions result = mockMvc.perform(post("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(person)));
		
		// Then / Assert
		result.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.firstName").value(person.getFirstName()))
			.andExpect(jsonPath("$.lastName").value(person.getLastName()))
			.andExpect(jsonPath("$.address").value(person.getAddress()));
	}
	
	@Test
	@DisplayName("JUnit test Given of Persons when findAll Persons then Return Persons List")
	void testGivenListOfPersons_WhenFindAllPersons_thenReturnPersonList() throws JsonProcessingException, Exception {
		// Given / Arrange
		List<Person> persons = new ArrayList<>();
		persons.addAll(Arrays.asList(person, person1));
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
	
	@Test
	@DisplayName("JUnit test Given Invalid Person ID when findById then Return Not Found")
	void testGivenInvalidPersonId_WhenFindById_thenReturnNotFound() throws JsonProcessingException, Exception {
		// Given / Arrange
		long personId = 1L;
		given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
		
		// When / Act
		ResultActions response = mockMvc.perform(get("/person/{id}", personId));
		
		// Then / Assert
		response
		.andExpect(status().isNotFound())
		.andDo(print());
	}
	

	@Test
	@DisplayName("JUnit test Given Update Person ID when Update then Return Person Object Updated")
	void testGivenUpdatePerson_WhenUpdate_thenReturnPersonObjectUpdated() throws JsonProcessingException, Exception {
		// Given / Arrange
//		long personId = 1L;
//		given(service.findById(personId)).willReturn(person);
		given(service.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(0));
		
		// When / Act
		Person updatedPerson = new Person("Ciclano", "Araujo", "Recife - Pernambuco - Brasil", "Female", "ciclano@hotmail.com");
		
		ResultActions response = mockMvc.perform(put("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(updatedPerson)));
		
		// Then / Assert
		response
			.andExpect(status().isOk())
			.andDo(print())
			.andExpect(jsonPath("$.firstName", is(updatedPerson.getFirstName())))
			.andExpect(jsonPath("$.lastName", is(updatedPerson.getLastName())))
			.andExpect(jsonPath("$.address", is(updatedPerson.getAddress())));
	}
	
	@Test
	@DisplayName("JUnit test Unexistent when Update then Return Person Object Updated")
	void testUnexistentPerson_WhenUpdate_thenReturnNotFound() throws JsonProcessingException, Exception {
		// Given / Arrange
//		long personId = 1L;
//		given(service.findById(personId)).willThrow(ResourceNotFoundException.class);
		given(service.update(any(Person.class))).willAnswer((invocation) -> invocation.getArgument(1));
		
		// When / Act
		Person updatedPerson = new Person("Ciclano", "Araujo", "Recife - Pernambuco - Brasil", "Female", "ciclano@hotmail.com");
		
		ResultActions response = mockMvc.perform(put("/person")
				.contentType(MediaType.APPLICATION_JSON)
				.content(mapper.writeValueAsString(updatedPerson)));
		
		// Then / Assert
		response
			.andExpect(status().isNotFound())
			.andDo(print());
	}
	
	@Test
	@DisplayName("JUnit test Given Person Id then Return Not Content")
	void testGivenPersonId_WhenDelete_thenReturnNotContent() throws JsonProcessingException, Exception {
		// Given / Arrange
		long personId = 1L;
		willDoNothing().given(service).delete(personId);
		
		// When / Act
		ResultActions response = mockMvc.perform(delete("/person/{id}", personId));
		
		// Then / Assert
		response
		.andExpect(status().isNoContent())
		.andDo(print());
	}
}
