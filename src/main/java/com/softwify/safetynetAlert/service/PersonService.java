package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.model.Person;

import java.util.List;

public interface PersonService {
    List<Person> findAll();

    Person findByFirstnameLastname(String firstName, String lastName);

    Person save(Person personList);
}
