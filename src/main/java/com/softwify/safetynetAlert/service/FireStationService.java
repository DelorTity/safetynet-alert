package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.model.FireStation;

import java.util.List;
import java.util.Optional;

public interface FireStationService {
    List<FireStation> getAll();
    Optional<FireStation> addedFireStation(FireStation fireStation);
    Optional<FireStation> findFireStationByAddress(String adresse);
    Optional<FireStation> updateFireStation(FireStation fireStation);
    Optional<FireStation> deleteFireStation(String adresse);
    List<FireStation> findByStations(List<Integer> stationNumbers);
}
