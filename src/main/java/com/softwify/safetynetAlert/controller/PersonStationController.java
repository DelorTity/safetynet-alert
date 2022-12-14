package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.dto.*;
import com.softwify.safetynetAlert.ecception.StationNotFoundException;
import com.softwify.safetynetAlert.service.PersonStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RestController
public class PersonStationController {
    @Autowired
    private PersonStationService personStationService;

    @GetMapping(value = "/firestation")
    public ResponseEntity<PersonStarter> retrievedPersonByAddress(@RequestParam("stationNumber") int stationNumber) {
        try {
            PersonStarter personByStation = personStationService.findPersonByStation(stationNumber);
            return ResponseEntity.ok(personByStation);
        } catch (StationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/childAlert")
    public ResponseEntity<List<Child>> retrievedChildByAddress(@RequestParam("address") String address) {
        try {
            List<Child> children = personStationService.findPersonByAddress(address);
            return ResponseEntity.ok(children);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/phoneAlert")
    public ResponseEntity<List<String>> retrievedPhoneNumberByStation(@RequestParam("stationNumber") int stationNumber) {
        try {
            List<String> phoneAlerts = personStationService.findPhoneNumberByStation(stationNumber);
            return ResponseEntity.ok(phoneAlerts);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/fire")
    public ResponseEntity<List<PersonFire>> retrievedFireStationByAddress(@RequestParam("address") String address) {
        try {
            List<PersonFire> personFires = personStationService.findFireStationByAddress(address);
            return ResponseEntity.ok(personFires);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/flood/stations")
    public ResponseEntity<List<FloodStation>> retrievedFloodStationByStationNumber(@RequestParam("station") String stationNumberString) {

        String input = stationNumberString.trim().replaceAll("\\s+", "");
        String[] numbersInString = input.split(",");
        List<Integer> stationNumbers = new ArrayList<>();
        for (String stringNumber : numbersInString) {
            stationNumbers.add(Integer.parseInt(stringNumber));
        }
        List<FloodStation> floodByStationNumber = personStationService.findFloodByStationNumber(Collections.singletonList(stationNumbers.size()));
        return ResponseEntity.ok(floodByStationNumber);
    }

    @GetMapping(value = "/personInfo")
    public ResponseEntity<List<PersonInfo>> retrievedPersonInfoByFirstNameAndLastName(@RequestParam("firstName") String firstName, @RequestParam("lastName") String lastName) {
        List<PersonInfo> persons = personStationService.findPersonByFirstNameAndLastName(firstName, lastName);
        return ResponseEntity.ok(persons);
    }

    @GetMapping(value = "/communityEmail")
    public ResponseEntity<List<String>> retrievedPersonsMailByCity(@RequestParam("city") String city) {
        List<String> personMail = personStationService.findBYCity(city);
        return ResponseEntity.ok(personMail);
    }
}
