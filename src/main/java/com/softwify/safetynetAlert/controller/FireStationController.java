package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.ecception.FireStationAlreadyExitsException;
import com.softwify.safetynetAlert.ecception.FireStationNotFoundException;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
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
    private FireStationService fireStationServices;

    //Récupére la liste des fireStation
    @GetMapping
    public List<FireStation> findAll() {
        return fireStationServices.findAll();
    }

    @PostMapping
    public ResponseEntity<FireStation> addFireStation(@RequestBody FireStation fireStation) {
        try {
            Optional<FireStation> optionalFireStation = fireStationServices.addFireStation(fireStation);
            FireStation newFireStation = optionalFireStation.get();
            return ResponseEntity.ok(newFireStation);
        } catch (FireStationAlreadyExitsException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = "/{address}")
    public ResponseEntity<FireStation> retrievedFireStation(@PathVariable String address) {
        try {
            Optional<FireStation> optionalFireStation = fireStationServices.findFireStationByAddress(address);
            FireStation fireStation = optionalFireStation.get();
            return ResponseEntity.ok(fireStation);
        } catch (FireStationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping
    public ResponseEntity<FireStation> updateFireStation(@RequestBody FireStation fireStation) {
        try {
            Optional<FireStation> optionalFireStation = fireStationServices.updateFireStation(fireStation);
            FireStation updatedFireStation = optionalFireStation.get();
            return ResponseEntity.ok(updatedFireStation);
        } catch (FireStationNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{address}")
    public ResponseEntity<FireStation> deletePerson(@PathVariable String address) {
        try {
            fireStationServices.deleteFireStation(address);
            return ResponseEntity.noContent().build();
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
