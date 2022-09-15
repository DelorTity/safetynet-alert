package com.softwify.safetynetAlert.dao;


import com.softwify.safetynetAlert.model.FireStation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FireStationDaoImpl implements FireStationDao{
    private final DataStoreManager dataStoreManager;

    public FireStationDaoImpl(DataStoreManager dataStoreManager) {
        this.dataStoreManager = dataStoreManager;
    }

    @Override
    public List<FireStation> findAll() {
        return dataStoreManager.getFireStation();
    }

    @Override
    public Optional<FireStation> addFireStation(FireStation fireStation) {
        List<FireStation> fireStations = dataStoreManager.getFireStation();
        boolean add = fireStations.add(fireStation);
        if (add) {
            return Optional.of(fireStation);
        }
        return Optional.empty();
    }
}
