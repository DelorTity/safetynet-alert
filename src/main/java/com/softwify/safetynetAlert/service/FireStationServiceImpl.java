package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationDao;
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
        Optional<FireStation> firestationSave = fireStationDao.addFireStation(fireStation);
        return firestationSave;
    }
}
