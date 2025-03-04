package br.com.adriano.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.adriano.exceptions.ResourceDuplicatedEmailException;
import br.com.adriano.models.Person;
import br.com.adriano.reposotories.PersonRepository;

@ExtendWith(MockitoExtension.class)
public class PersonServicesTest {

	@Mock
	private PersonRepository repository;
	
	@InjectMocks
	private PersonServices services;
	
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
	
	@DisplayName("JUnit test for Given Person Object when Save Person then Return Person Object")
	@Test
	void testGivenPersonObject_whenSavePerson_thenReturnPersonObject() {
		// Given / Arrange
		given(repository.findByEmail(anyString())).willReturn(Optional.empty());
		given(repository.save(person)).willReturn(person);
		
		// When / Act
		Person savedPerson = services.create(person);
		
		// Then / Assert
		assert savedPerson != null;
		assert savedPerson.getFirstName().equals(person.getFirstName());
		assert savedPerson.getLastName().equals(person.getLastName());
		assert savedPerson.getAddress().equals(person.getAddress());
		assert savedPerson.getGender().equals(person.getGender());
		assert savedPerson.getEmail().equals(person.getEmail());
	}
	
	@DisplayName("Given existing email when create then throw ResourceDuplicatedEmailException")
    @Test
    void testGivenExistingEmail_whenCreate_thenThrowResourceDuplicatedEmailException() {
		// Given / Arrange
		given(repository.findByEmail(person.getEmail())).willReturn(Optional.of(person));

		// When / Act
		ResourceDuplicatedEmailException exception = assertThrows(
	            ResourceDuplicatedEmailException.class,
	            () -> services.create(person),
	            "Esperava-se que ResourceDuplicatedEmailException fosse lançada"
	        );
		
		// Then / Assert
		assertEquals("Email already exists: " + person.getEmail(), exception.getMessage());
        verify(repository, never()).save(any(Person.class)); // Garante que o save não foi chamado
	}
	
	@DisplayName("JUnit test Given Persons List when FindAll Persons then Return Person List")
	@Test
	void testGivenPersonsList_whenFindAllPersons_thenReturnPersonList() {
		// Given / Arrange
		given(repository.findAll()).willReturn(List.of(person, person1));
		
		// When / Act
		List<Person> personsList = services.findAll();
		
		// Then / Assert
		assertNotNull(personsList);
		assertEquals(2, personsList.size());
	}
	
	@DisplayName("JUnit test Given Empty Persons List when FindAll Persons then Return Empty Person List")
	@Test
	void testGivenEmptyPersonsList_whenFindAllPersons_thenReturnEmptyPersonList() {
		// Given / Arrange
		given(repository.findAll()).willReturn(Collections.emptyList());
		
		// When / Act
		List<Person> personsList = services.findAll();
		
		// Then / Assert
		assertTrue(personsList.isEmpty());
		assertEquals(0, personsList.size());
	}
	
	@DisplayName("JUnit test for a given person id when findaById then return person object")
	@Test
	void testGivenPersonId_whenFindById_thenReturnPersonObject() {
		// Given / Arrange
		given(repository.findById(anyLong())).willReturn(Optional.of(person));
		
		// When / Act
		Person savedPerson = services.findById(1L);
		
		// Then / Assert
		assertNotNull(savedPerson);
		assertEquals(person.getId(), savedPerson.getId());
		assertEquals(person.getFirstName(), savedPerson.getFirstName());
	}
	
	@DisplayName("JUnit test Given Person Object when Update Person then Return Update Person Object")
	@Test
	void testGivenPersonObject_whenUpdatePerson_thenReturnUpdatePersonObject() {
		// Given / Arrange
		person.setId(1L);
		given(repository.findById(anyLong())).willReturn(Optional.of(person));
		
		person.setFirstName("Ciclano");
		person.setLastName("Araujo");
		person.setAddress("Recife - Pernambuco - Brasil");
		person.setEmail("ciclano@hotmail.com");
		
		given(repository.save(person)).willReturn(person);
		
		// When / Act
		Person updatePerson = services.update(person);
		
		// Then / Assert
		assertNotNull(updatePerson);
		assertEquals(person.getId(), updatePerson.getId());
		assertEquals(person.getFirstName(), updatePerson.getFirstName());
		assertEquals(person.getLastName(), updatePerson.getLastName());
		assertEquals(person.getAddress(), updatePerson.getAddress());
		assertEquals(person.getEmail(), updatePerson.getEmail());
	}
	
	@DisplayName("JUnit test Given Person ID when Delete Person then do Nothing")
	@Test
	void testGivenPersonID_whenDeletePerson_thenDoNothing() {
		// Given / Arrange
		person.setId(1L);
		given(repository.findById(anyLong())).willReturn(Optional.of(person));
		willDoNothing().given(repository).delete(person);
		
		// When / Act
		services.delete(person.getId());
		
		// Then / Assert
		verify(repository, times(1)).delete(person);
	}
}
