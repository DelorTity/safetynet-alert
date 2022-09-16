package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.ecception.FireStationNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FireStationServicesImpl implements FireStationService {
    private final FireStationsDao fireStationsDao;

    public FireStationServicesImpl(FireStationsDao fireStationsDao) {
        this.fireStationsDao = fireStationsDao;
    }

    @Override
    public List<FireStation> findAll() {
        return fireStationsDao.findAll();
    }

    @Override
    public Optional<FireStation> findFireStationByAddress(String address) {
        Optional<FireStation> optionalFireStation = fireStationsDao.findFireStationByAddress(address);
        return Optional.of(optionalFireStation.orElseThrow(FireStationNotFoundException::new));
    }

    @Override
    public Optional<FireStation> addFireStation(FireStation fireStation) {
        return fireStationsDao.save(fireStation);
    }

    @Override
    public Optional<FireStation> updateFireStation(FireStation fireStation) {
        Optional<FireStation> update = fireStationsDao.update(fireStation);
        if (update.isEmpty()) {
            throw new FireStationNotFoundException();
        }
        return update;
    }

    @Override
    public Optional<FireStation> deleteFireStation(String address) {
        Optional<FireStation> delete = fireStationsDao.delete(address);
        if (delete.isEmpty()) {
            throw new FireStationNotFoundException();
        }
        return delete;
    }
}
