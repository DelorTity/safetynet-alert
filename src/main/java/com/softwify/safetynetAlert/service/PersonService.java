package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<Person> findAll();
    Person findByFirstnameLastname(String firstName, String lastName);
    Optional<Person> savePerson(Person person);
    Optional<Person> updatePerson(Person person);
    Optional<Person> deletePerson(String firstname, String lastname);

}
