package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/firestation")
public class FireStationController {
    @Autowired
    private FireStationService fireStationService;

    @GetMapping
    public List<FireStation> findAll() {
        return fireStationService.getAll();
    }

    @PostMapping
    public ResponseEntity<FireStation> addFirestation(@RequestBody FireStation fireStation) {
        Optional<FireStation> optionalFireStation = fireStationService.addedFireStation(fireStation);
        FireStation addedFireStation = optionalFireStation.get();
        return ResponseEntity.ok(addedFireStation);
    }
}
