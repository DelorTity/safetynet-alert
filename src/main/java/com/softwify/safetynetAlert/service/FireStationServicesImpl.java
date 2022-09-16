package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.model.FireStation;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
