package br.com.adriano.services;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.adriano.exceptions.ResourceDuplicatedEmailException;
import br.com.adriano.exceptions.ResourceNotFoundException;
import br.com.adriano.models.Person;
import br.com.adriano.reposotories.PersonRepository;

@Service
public class PersonServices {
	private Logger logger = Logger.getLogger(PersonServices.class.getName());
	
	@Autowired
	PersonRepository repository;
	
	public List<Person> findAll() {
		logger.info("Method findAll");
//		List<Person> persons = new ArrayList<Person>();
		
		// Mocking a person
//		for (int i = 0; i < 8; i++) {
//			Person person = mockPerson(i);
//			persons.add(person);
//		}
		
		return repository.findAll();
	}

	public Person findById(Long id) {
		logger.info("Method findById");

		// Mocking a person
//		Person person = new Person();
//		person.setId(counter.incrementAndGet());
//		person.setFirstName("Adriano");
//		person.setLastName("Santana");
//		person.setAddress("Rua 1");
//		person.setGender("Male");
		
		return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID for find"));
	}
	
	public Person create(Person person) {
		logger.info("Method create");
		
		Optional<Person> savedPerson = repository.findByEmail(person.getEmail());
		
		if (savedPerson.isPresent()) {
			throw new ResourceDuplicatedEmailException("Email already exists: " + person.getEmail(), new Throwable("Email already exists"));
		}
		return repository.save(person);
	}
	
	public Person update(Person person) {
		logger.info("Method update");
		
		var entity = repository.findById(person.getId()).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID for update"));
		
		entity.setFirstName(person.getFirstName());
		entity.setLastName(person.getLastName());
		entity.setAddress(person.getAddress());
		entity.setGender(person.getGender());
		
		return repository.save(entity);
	}
	
	public void delete(Long id) {
		logger.info("Method delete");
		var entity = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("No records found for this ID for delete"));
		
		repository.delete(entity);
	}
	
//	private Person mockPerson(int i) {
//		Person person = new Person();
//		person.setId(counter.incrementAndGet());
//		person.setFirstName("Person Name " + i);
//		person.setLastName("Last Name " + i);
//		person.setAddress("Some Address in Brasil " + i);
//		person.setGender(i % 2 == 0 ? "Male" : "Female");
//		
//		return person;
//	}
}
