package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.exceptions.PersonAlreadyExistsException;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
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
            Person createdPerson = personService.savePerson(person);
            return ResponseEntity.ok(createdPerson);
        } catch (PersonAlreadyExistsException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
