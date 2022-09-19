package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireDtoDao;
import com.softwify.safetynetAlert.exceptions.StationNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class FireDtoServiceImpl implements FireDtoService{
    private FireDtoDao fireDtoDao;

    public FireDtoServiceImpl(FireDtoDao fireDtoDao) {
        this.fireDtoDao = fireDtoDao;
    }

    @Override
    public Optional<FireStation> findFireStationByStation(int station) {
        Optional<FireStation> stationOptional = fireDtoDao.findFireStationByStation(station);
        return Optional.of(stationOptional.orElseThrow(StationNotFoundException::new));

    }
}
