package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationDao;
import com.softwify.safetynetAlert.exceptions.FireStationNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FireStationServiceImpl implements FireStationService{
    private FireStationDao fireStationDao;

    public FireStationServiceImpl(FireStationDao fireStationDao) {
        this.fireStationDao = fireStationDao;
    }

    @Override
    public List<FireStation> getAll() {
        return fireStationDao.findAll();
    }

    @Override
    public Optional<FireStation> addedFireStation(FireStation fireStation) {
        Optional<FireStation> firestationSave = fireStationDao.save(fireStation);
        return firestationSave;
    }

    @Override
    public Optional<FireStation> findFireStationByAdresse(String adresse) {
        Optional<FireStation> optionalFireStation = fireStationDao.findFireStationByAdresse(adresse);
        return Optional.of(optionalFireStation.orElseThrow(FireStationNotFoundException::new));
    }

    @Override
    public Optional<FireStation> updateFireStation(FireStation fireStation) {
        Optional<FireStation> update = fireStationDao.update(fireStation);
        if (update.isEmpty()) {
            throw new FireStationNotFoundException();
        }
        return update;
    }

    @Override
    public Optional<FireStation> deleteFireStation(String adresse) {
        Optional<FireStation> delete = fireStationDao.delete(adresse);
        if (delete.isEmpty()) {
            throw new FireStationNotFoundException();
        }
        return delete;
    }
}
