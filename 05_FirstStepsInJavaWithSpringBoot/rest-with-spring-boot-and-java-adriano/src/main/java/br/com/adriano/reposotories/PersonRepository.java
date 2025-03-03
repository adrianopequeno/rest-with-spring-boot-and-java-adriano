package br.com.adriano.reposotories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.adriano.models.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {}
