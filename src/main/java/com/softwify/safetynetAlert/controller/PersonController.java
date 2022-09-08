package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
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
    public Person save(@RequestBody Person person) {
       return personService.save(person);
    }
}