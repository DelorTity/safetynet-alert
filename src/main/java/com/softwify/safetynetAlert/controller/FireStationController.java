package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.service.FireStationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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
}
