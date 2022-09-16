package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.FireStation;

import java.util.List;
import java.util.Optional;

public interface FireStationsDao {
    List<FireStation> findAll();

    Optional<FireStation> save(FireStation fireStation);
}
