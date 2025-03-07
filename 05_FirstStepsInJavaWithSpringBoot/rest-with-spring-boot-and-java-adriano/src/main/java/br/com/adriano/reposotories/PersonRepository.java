package br.com.adriano.reposotories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.adriano.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {

	Optional<Person> findByEmail(String email);
	
	// Define custom query using JPQL with index parameter (?1)
	@Query("SELECT p FROM Person p WHERE p.firstName = ?1 AND p.lastName = ?2")
	Person findByJPQL(String firstName, String lastName);
	
	// Define custom query using JPQL with named parameter (:email)
	@Query("SELECT p FROM Person p WHERE p.email = :email")
	Person findByJPQLNamedParam(@Param("email") String email);
	
	// Define custom query using native SQL with index parameter (?1)
	@Query(value = "SELECT * FROM person p WHERE p.first_name = ?1 AND p.last_name = ?2", nativeQuery = true)
	Person findByNativeSQL(String firstName, String lastName);
	
	// Define custom query using native SQL with named parameter (:email)
	@Query(value = "SELECT * FROM person p WHERE p.email = :email", nativeQuery = true)
	Person findByNativeSQLNamedParam(@Param("email") String email);
}
