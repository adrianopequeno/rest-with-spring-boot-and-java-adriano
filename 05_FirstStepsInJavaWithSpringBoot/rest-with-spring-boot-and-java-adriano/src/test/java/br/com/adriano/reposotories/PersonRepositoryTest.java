package br.com.adriano.reposotories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import br.com.adriano.models.Person;

@DataJpaTest
class PersonRepositoryTest {
	
	@Autowired
	private PersonRepository repository;

	@DisplayName("Given PersonObject when Save then Return Saved Person")
	@Test
	void testGivenPersonObject_whenSave_thenReturnSavedPerson() {
		// Given / Arrange
		Person person0 = new Person("Fulano", "da Silva", "Patos - Paraiba - Brasil", "Male", "fulano@gmail.com");
		
		// When / Act
		Person savedPerson = repository.save(person0);
		
		// Then / Assert
		assertNotNull(savedPerson);
		assertTrue(savedPerson.getId() > 0);
	}
	
	// Verifica se uma pessoa salva pode ser recuperada corretamente pelo ID.
	@DisplayName("Given PersonId when FindById then Return Person")
	@Test
	void testGivenPersonId_whenFindById_thenReturnPerson() {
	    // Given / Arrange
	    Person person = new Person("Fulano", "da Silva", "Patos - Paraiba - Brasil", "Male", "fulano@gmail.com");
	    Person savedPerson = repository.save(person);

	    // When / Act
	    // pessoa recuperada pelo ID
	    Optional<Person> retrievedPerson = repository.findById(savedPerson.getId());

	    // Then / Assert
	    assertTrue(retrievedPerson.isPresent());
	    assertEquals(savedPerson.getId(), retrievedPerson.get().getId());
	}

	// Verifica se uma pessoa salva pode ser recuperada corretamente pelo email.
	@DisplayName("Given PersonEmail when FindByEmail then Return Person")
	@Test
	void testGivenPersonEmail_whenFindByEmail_thenReturnPerson() {
	    // Given / Arrange
	    Person person = new Person("Fulano", "da Silva", "Patos - Paraiba - Brasil","Male", "fulano@gmail.com");
	    Person savedPerson = repository.save(person);
	    
	    // When / Act
	    // pessoa recuperada pelo email
	    Optional<Person> retrievedPerson = repository.findByEmail(savedPerson.getEmail());
	    
	    // Then / Assert
	    assertTrue(retrievedPerson.isPresent());
	    assertEquals(savedPerson.getEmail(), retrievedPerson.get().getEmail());
	    
	}
	
	// Verifica se o repositório pode recuperar todas as pessoas armazenadas.
	@DisplayName("When FindAll then Return List of Persons")
	@Test
	void testWhenFindAll_thenReturnListOfPersons() {
	    // Given / Arrange
	    Person person1 = new Person("Fulano", "da Silva", "Patos - Paraiba - Brasil", "Male", "fulano@gmail.com");
	    Person person2 = new Person("Ciclano", "Pereira", "João Pessoa - Paraiba - Brasil", "Male", "ciclano@gmail.com");
	    repository.save(person1);
	    repository.save(person2);

	    // When / Act
	    List<Person> persons = repository.findAll();

	    // Then / Assert
	    assertFalse(persons.isEmpty());
	    assertEquals(2, persons.size());
	}
	
	// Verifica se uma pessoa pode ser atualizada corretamente.
	@DisplayName("Given Updated Person when Save then Return Updated Person")
	@Test
	void testGivenUpdatedPerson_whenSave_thenReturnUpdatedPerson() {
	    // Given / Arrange
	    Person person = new Person("Fulano", "da Silva", "Patos - Paraiba - Brasil", "Male", "fulano@gmail.com");
	    Person savedPerson = repository.save(person);
	    
	    // Atualizando os dados
	    savedPerson.setLastName("Souza");
	    savedPerson.setEmail("fulano.souza@gmail.com");

	    // When / Act
	    Person updatedPerson = repository.save(savedPerson);

	    // Then / Assert
	    assertEquals("Souza", updatedPerson.getLastName());
	    assertEquals("fulano.souza@gmail.com", updatedPerson.getEmail());
	}

	// Garante que a remoção de uma pessoa funciona corretamente.
	@DisplayName("Given PersonId when Delete then Person is Removed")
	@Test
	void testGivenPersonId_whenDelete_thenPersonIsRemoved() {
	    // Given / Arrange
	    Person person = new Person("Fulano", "da Silva", "Patos - Paraiba - Brasil", "Male", "fulano@gmail.com");
	    Person savedPerson = repository.save(person);

	    // When / Act
	    repository.deleteById(savedPerson.getId());
	    Optional<Person> deletedPerson = repository.findById(savedPerson.getId());

	    // Then / Assert
	    // Verifica se a pessoa foi removida
	    assertFalse(deletedPerson.isPresent());
	}
	
	// Verifica se a consulta personalizada JPQL funciona corretamente.
	@DisplayName("Given PersonName when FindByJPQL then Return Person")
	@Test
	void testGivenPersonName_whenFindByJPQL_thenReturnPerson() {
	    // Given / Arrange
	    Person person = new Person("Fulano", "da Silva", "Patos - Paraiba - Brasil", "Male", "fulano@gmail.com");
	    repository.save(person);
	    
	    String firstName = "Fulano";
	    String lastName = "da Silva";
	    
	    // When / Act
	    // pessoa recuperada pela consulta JPQL
	    Person retrievedPerson = repository.findByJPQL(firstName, lastName);
	    
	    // Then / Assert
	    assertNotNull(retrievedPerson);
	    assertEquals(firstName, retrievedPerson.getFirstName());
	    assertEquals(lastName, retrievedPerson.getLastName());
	    assertEquals(person.getId(), retrievedPerson.getId());
	    
	}


}
