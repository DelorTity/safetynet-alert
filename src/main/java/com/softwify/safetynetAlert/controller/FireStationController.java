package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.exceptions.FireStationAlreadyExistException;
import com.softwify.safetynetAlert.exceptions.FireStationNotFoundException;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/firestations")
public class FireStationController {
    @Autowired
    private FireStationService fireStationService;

    @GetMapping
    public List<FireStation> findAll() {
        return fireStationService.getAll();
    }

    @GetMapping(value = "/{adresse}")
    public ResponseEntity<FireStation> retrievedFireStation(@PathVariable String adresse) {
        try {
            Optional<FireStation> optionalFireStation = fireStationService.findFireStationByAddress(adresse);
            FireStation fireStation = optionalFireStation.get();
            return ResponseEntity.ok(fireStation);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<FireStation> addFirestation(@RequestBody FireStation fireStation) {
        try {
            Optional<FireStation> optionalFireStation = fireStationService.addedFireStation(fireStation);
            FireStation save = optionalFireStation.get();
            return ResponseEntity.ok(save);
        } catch (FireStationAlreadyExistException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<FireStation> updateFireStation(@RequestBody FireStation fireStation) {

        try {
            Optional<FireStation> optionalFireStation = fireStationService.updateFireStation(fireStation);
            FireStation updatedFireStation = optionalFireStation.get();
            return ResponseEntity.ok(updatedFireStation);
        } catch (FireStationNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{address}")
    public ResponseEntity<Person> deletePerson(@PathVariable String address) {
        try {
            fireStationService.deleteFireStation(address);
            return ResponseEntity.noContent().build();
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
