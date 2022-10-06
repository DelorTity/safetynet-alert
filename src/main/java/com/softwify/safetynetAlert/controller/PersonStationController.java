package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.dto.PersonStarter;
import com.softwify.safetynetAlert.exceptions.StationNotFoundException;
import com.softwify.safetynetAlert.service.PersonStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
