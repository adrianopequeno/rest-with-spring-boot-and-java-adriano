package br.com.adriano.controllers;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.adriano.models.Person;
import br.com.adriano.services.PersonServices;

@ExtendWith(MockitoExtension.class) // Habilita mocks sem precisar do contexto Spring
class PersonControllerAtualizadoTest {
	
	private MockMvc mockMvc;
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

	
}
