package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.dto.PersonStation;
import com.softwify.safetynetAlert.ecception.StationNotFoundException;
import com.softwify.safetynetAlert.service.PersonStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class PersonStationController {
    @Autowired
    private PersonStationService personStationService;

    @GetMapping(value = "/firestation")
    public ResponseEntity<List<PersonStation>> retrievedPersonByAddress(@RequestParam("stationNumber") int stationNumber) {
        try {
            List<PersonStation> personList = personStationService.findPersonByStation(stationNumber);
            return ResponseEntity.ok(personList);
        } catch (StationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
