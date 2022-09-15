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
    public Optional<FireStation> save(FireStation fireStation) {
        List<FireStation> fireStations = dataStoreManager.getFireStation();
        boolean add = fireStations.add(fireStation);
        if (add) {
            return Optional.of(fireStation);
        }
        return Optional.empty();
    }

    @Override
    public Optional<FireStation> findFireStationByAdresse(String adresse) {
        List<FireStation> fireStations = dataStoreManager.getFireStation();
        for (FireStation fireStation : fireStations) {
            if (fireStation.getAddress().equals(adresse)) {
                return Optional.of(fireStation);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<FireStation> update(FireStation fireStation) {
        Optional<FireStation> optionalFireStation = findFireStationByAdresse(fireStation.getAddress());
        if (optionalFireStation.isPresent()) {
            FireStation existingFireStation = optionalFireStation.get();
            existingFireStation.setAddress(fireStation.getAddress());
            existingFireStation.setStation(fireStation.getStation());
            return Optional.of(fireStation);
        }
        return Optional.empty();
    }

    @Override
    public Optional<FireStation> delete(String adresse) {
        Optional<FireStation> optionalFireStation = findFireStationByAdresse(adresse);
        if (optionalFireStation.isPresent()) {
            List<FireStation> fireStations = dataStoreManager.getFireStation();
            FireStation fireStation = optionalFireStation.get();
            fireStations.remove(fireStation);
            return optionalFireStation;
        }
        return Optional.empty();
    }
}
