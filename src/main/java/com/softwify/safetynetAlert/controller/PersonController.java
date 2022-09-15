package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.exceptions.PersonAlreadyExistsException;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

//@RestController
@RequestMapping(value = "/persons")
public class PersonController {
    @Autowired
    private PersonService personService;

    @GetMapping
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping(value = "/{firstname}/{lastname}")
    public ResponseEntity<Person> retrievedPerson(@PathVariable String firstname, @PathVariable String lastname) {
        try {
            Person person = personService.findByFirstnameLastname(firstname, lastname);
            return ResponseEntity.ok(person);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Person> addPerson(@RequestBody Person person) {
        try {
            Optional<Person> optionalPerson = personService.savePerson(person);
            Person addedPerson = optionalPerson.get();
            return ResponseEntity.ok(addedPerson);
        } catch (PersonAlreadyExistsException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<Person> updatePerson(@RequestBody Person person) {
        try {
            Optional<Person> optionalPerson = personService.updatePerson(person);
            Person updatedPerson = optionalPerson.get();
            return ResponseEntity.ok(updatedPerson);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{firstname}/{lastname}")
    public ResponseEntity<Person> deletePerson(@PathVariable String firstname, @PathVariable String lastname) {
        try {
            personService.deletePerson(firstname, lastname);
            return ResponseEntity.noContent().build();
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}