package br.com.adriano.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

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
	
	@BeforeEach
	void setUp() {
		// Given / Arrange
		person = new Person();
		person.setFirstName("Adriano");
		person.setLastName("Santana");
		person.setAddress("Rua 1");
		person.setGender("Male");
		person.setEmail("fulano@gmail.com");
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
}
