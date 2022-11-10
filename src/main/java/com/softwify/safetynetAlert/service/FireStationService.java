package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.model.FireStation;

import java.util.List;
import java.util.Optional;

public interface FireStationService {
    List<FireStation> findAll();

    Optional<FireStation> findFireStationByAddress(String address);

    Optional<FireStation> addFireStation(FireStation fireStation);

    Optional<FireStation> updateFireStation(FireStation fireStation);

    Optional<FireStation> deleteFireStation(String address);
}
