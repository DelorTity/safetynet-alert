package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.FireStation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FireStationsDaoImpl implements FireStationsDao {
    private final DataStoreManager dataStoreManager;

    public FireStationsDaoImpl(DataStoreManager dataStoreManager) {
        this.dataStoreManager = dataStoreManager;
    }

    @Override
    public List<FireStation> findAll() {
        return dataStoreManager.getFireStation();
    }

    @Override
    public Optional<FireStation> save(FireStation fireStation) {
        List<FireStation> fireStationList = dataStoreManager.getFireStation();
        boolean add = fireStationList.add(fireStation);
        if(add) {
            return Optional.of(fireStation);
        }
        return Optional.empty();
    }
}
