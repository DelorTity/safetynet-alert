package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.FireStation;

import java.util.List;
import java.util.Optional;

public interface FireStationsDao {
    List<FireStation> findAll();

    Optional<FireStation> save(FireStation fireStation);

    Optional<FireStation> update(FireStation fireStation);

    Optional<FireStation> findByAddress(String address);

    Optional<FireStation> delete(String address);

    List<FireStation> findByStationNumber(int stationNumber);

    List<FireStation> findByStationNumbers(List<Integer> stationNumbers);
}
