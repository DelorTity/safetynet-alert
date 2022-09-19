package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.dto.Station;
import com.softwify.safetynetAlert.model.FireStation;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class FireDtoImpl implements FireDtoDao {
    private DataStoreManager dataStoreManager;

    public FireDtoImpl(DataStoreManager dataStoreManager) {
        this.dataStoreManager = dataStoreManager;
    }

    @Override
    public Optional<FireStation> findFireStationByStation(int station) {
        List<FireStation> fireStations = dataStoreManager.getFireStation();
        for (FireStation fireStation: fireStations) {
            if (fireStation.getStation() == station) {
                fireStation.getAddress();
                return Optional.of(fireStation);
            }
        }
        return Optional.empty();
    }

    public Optional<Station> reStationByStation(int station) {
        Optional<FireStation> fireStationByStation = findFireStationByStation(station);
        String address = fireStationByStation.get().getAddress();
        if (address.equals())
        return Optional.empty();
    }
}
