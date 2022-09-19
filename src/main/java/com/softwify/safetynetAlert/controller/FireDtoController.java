package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.exceptions.StationNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.service.FireDtoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class FireDtoController {
    @Autowired
    private FireDtoService fireDtoService;

    @GetMapping(value = "/firestation/{station}")
    public ResponseEntity<FireStation> retrievedAddress(@PathVariable int station) {
        try {
            Optional<FireStation> stationOptional = fireDtoService.findFireStationByStation(station);
            return ResponseEntity.ok(stationOptional.get());
        } catch (StationNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
