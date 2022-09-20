package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.FireStation;

import java.util.List;
import java.util.Optional;

public interface FireStationDao {
    List<FireStation> findAll();
    Optional<FireStation> save(FireStation fireStation);
    Optional<FireStation> findByAddress(String adresse);
    Optional<FireStation> update(FireStation fireStation);
    Optional<FireStation> delete(String adresse);
    List<FireStation> findByStationNumber(int station);
}
