package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.model.Person;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    List<Person> findAll();

    Person findByFirstnameLastname(String firstName, String lastName);

    Person save(Person personList);

    Optional<Person> update(Person person);

    Optional<Person> delete(String firstname, String lastname);
}
