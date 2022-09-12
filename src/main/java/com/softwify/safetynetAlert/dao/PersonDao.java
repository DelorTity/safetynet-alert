package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonDao {
    List<Person> findAll();
    Optional<Person> findPersonByFirstnameAndLastname(String firstName, String lastName);
    Person addPerson(Person person);
}
