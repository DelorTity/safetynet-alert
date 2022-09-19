package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.FireStation;

import java.util.Optional;

public interface FireDtoDao {
    Optional<FireStation> findFireStationByStation(int station);

}
