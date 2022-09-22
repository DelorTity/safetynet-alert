package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        Optional<FireStation> optionalFireStation = findByAddress(fireStation.getAddress());
        if (optionalFireStation.isEmpty()) {
            List<FireStation> fireStations = dataStoreManager.getFireStation();
            fireStations.add(fireStation);
            return Optional.of(fireStation);
        }
        return Optional.empty();
    }

    public Optional<FireStation> findByAddress(String address) {
        List<FireStation> fireStations = dataStoreManager.getFireStation();
        for (FireStation fireStation : fireStations) {
            if (fireStation.getAddress().equals(address)) {
                return Optional.of(fireStation);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<FireStation> update(FireStation fireStation) {
        Optional<FireStation> optionalFireStation = findByAddress(fireStation.getAddress());
        if (optionalFireStation.isPresent()) {
            FireStation existingFireStation = optionalFireStation.get();
            existingFireStation.setAddress(fireStation.getAddress());
            existingFireStation.setStation(fireStation.getStation());
            return Optional.of(fireStation);
        }
        return Optional.empty();
    }

    @Override
    public Optional<FireStation> delete(String address) {
        Optional<FireStation> optionalFireStation = findByAddress(address);
        if (optionalFireStation.isPresent()) {
            List<FireStation> fireStations = dataStoreManager.getFireStation();
            FireStation fireStation = optionalFireStation.get();
            fireStations.remove(fireStation);
            return optionalFireStation;
        }
        return Optional.empty();
    }

    @Override
    public List<FireStation> findByStationNumber(int stationNumber) {
        List<FireStation> fireStations = dataStoreManager.getFireStation();
        return fireStations.stream()
                .filter(s -> s.getStation() == stationNumber)
                .collect(Collectors.toList());
    }
}
