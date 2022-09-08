package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

import java.util.List;

@RestController
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping(value = "/persons")
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping(value = "persons/{firstname}/{lastname}")
    public ResponseEntity<Person> retrievePerson(@PathVariable String firstname, @PathVariable String lastname) {
       try {
           Person person = personService.findByFirstnameLastname(firstname, lastname);
           return ResponseEntity.ok(person);
       } catch (PersonNotFoundException e) {
           return ResponseEntity.notFound().build();
       }
    }

}
