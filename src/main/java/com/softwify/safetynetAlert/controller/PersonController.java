package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.ecception.PersonAlreadyExitException;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/persons")
public class PersonController {
    @Autowired
    private PersonService personService;

    //Récupére la liste des personnes
    @GetMapping
    public List<Person> findAll() {
        return personService.findAll();
    }

    @GetMapping(value = "/{firstname}/{lastname}")
    public ResponseEntity<Person> retrievePerson(@PathVariable String firstname, @PathVariable String lastname) {
        try {
            Person person = personService.findByFirstnameLastname(firstname, lastname);
            return ResponseEntity.ok(person);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<Person> save(@RequestBody Person person) {
        try {
            Optional<Person> optionalPerson = personService.save(person);
            return ResponseEntity.ok(optionalPerson.get());
        } catch (PersonAlreadyExitException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<Person> update(@RequestBody Person person) throws Exception {
        try {
            Optional<Person> optionalPerson = personService.update(person);
            Person updatedPerson = optionalPerson.get();
            return ResponseEntity.ok(updatedPerson);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{firstname}/{lastname}")
    public ResponseEntity<Person> delete (@PathVariable String firstname, @PathVariable String lastname) {
        try {
            personService.delete(firstname, lastname);
            return ResponseEntity.noContent().build();
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}