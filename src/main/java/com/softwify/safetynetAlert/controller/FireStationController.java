package com.softwify.safetynetAlert.controller;

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
    public ResponseEntity<FireStation> saveFireStation(@RequestBody FireStation fireStation) {
        Optional<FireStation> optionalFireStation = fireStationServices.save(fireStation);
            FireStation savedFireStation = optionalFireStation.get();
            return ResponseEntity.ok(savedFireStation);
    }
}
