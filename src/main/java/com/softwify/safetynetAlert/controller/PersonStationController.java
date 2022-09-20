package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.exceptions.StationNotFoundException;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.PersonStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonStationController {
    @Autowired
    private PersonStationService personStationService;

    @GetMapping(value = "/firestation/{station}")
    public ResponseEntity<List<Person>> retrievedPersonByAddress(@PathVariable int station) {
        try {
            List<Person> personList = personStationService.findPersonByStation(station);
            return ResponseEntity.ok(personList);
        } catch (StationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
