package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.model.FireStation;

import java.util.Optional;

public interface FireDtoService {
    Optional<FireStation> findFireStationByStation(int station);

}
