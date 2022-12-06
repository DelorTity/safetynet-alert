package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.dto.*;
import com.softwify.safetynetAlert.exceptions.CityNotFoundException;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.exceptions.StationNotFoundException;
import com.softwify.safetynetAlert.service.PersonStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
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
            List<Child> personByAddress = personStationService.findPersonByAddress(address);
            return ResponseEntity.ok(personByAddress);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(value = "/phoneAlert")
    public ResponseEntity<List<String>> retrievedPhoneByStationNumber(@RequestParam("firestation") int firestationNumber) {
        List<String> phoneNumberByStation = personStationService.findPhoneNumberByStation(firestationNumber);
        return ResponseEntity.ok(phoneNumberByStation);
    }

    @GetMapping(value = "/fire")
    public ResponseEntity<List<PersonFire>> retrievedPersonFireByAddress(@RequestParam("address") String address) {
        List<PersonFire> personFireByAddress = personStationService.findPersonFireByAddress(address);
        return ResponseEntity.ok(personFireByAddress);
    }

    @GetMapping(value = "/flood/stations")
    public ResponseEntity<List<FloodStation>> retrievedFloodStationByStationNumber(@RequestParam("station") String stationNumberString) {

        String input = stationNumberString.trim().replaceAll("\\s+", "");
        String[] numbersInString = input.split(",");
        List<Integer> stationNumbers = new ArrayList<>();
        for (String stringNumber : numbersInString) {
            stationNumbers.add(Integer.parseInt(stringNumber));
        }
        List<FloodStation> floodByStationNumber = personStationService.findFloodByStationNumber(stationNumbers);
        return ResponseEntity.ok(floodByStationNumber);
    }

    @GetMapping(value = "personInfo")
    public ResponseEntity<List<PersonInfo>> retrievedPersonsByFirstnameLastname(@RequestParam("firstName") String firstname, @RequestParam("lastName") String lastname) {
        List<PersonInfo> persons = personStationService.findPersonByFirstAndLastName(firstname, lastname);
        return ResponseEntity.ok(persons);
    }

    @GetMapping(value = "communityEmail")
    public ResponseEntity<List<String>> retrievedPersonsByCity(@RequestParam("city") String city) {
        try {
            List<String> personByCity = personStationService.findPersonByCity(city);
            return ResponseEntity.ok(personByCity);
        } catch (CityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
