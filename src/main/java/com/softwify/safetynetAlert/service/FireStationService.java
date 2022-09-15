package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.model.FireStation;

import java.util.List;
import java.util.Optional;

public interface FireStationService {
    List<FireStation> getAll();
    Optional<FireStation> addedFireStation(FireStation fireStation);

}
