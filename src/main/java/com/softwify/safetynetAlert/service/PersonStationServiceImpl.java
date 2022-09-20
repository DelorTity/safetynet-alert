package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.PersonStationDao;
import com.softwify.safetynetAlert.model.Person;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonStationServiceImpl implements PersonStationService {
    private PersonStationDao personStationDao;

    public PersonStationServiceImpl(PersonStationDao fireDtoDao) {
        this.personStationDao = fireDtoDao;
    }

    @Override
    public List<Person> findPersonByStation(int station) {
        return personStationDao.findPersonByStation(station);
    }
}
