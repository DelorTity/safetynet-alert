package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.dto.PersonStation;
import com.softwify.safetynetAlert.mappers.PersonMapper;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.Person;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonStationServiceImpl implements PersonStationService {
    private FireStationsDao fireStationDao;
    private PersonDao personDao;

    public PersonStationServiceImpl(FireStationsDao fireStationDao, PersonDao personDao) {
        this.fireStationDao = fireStationDao;
        this.personDao = personDao;
    }

    @Override
    public List<PersonStation> findPersonByStation(int stationNumber) {
        List<FireStation> fireStations = fireStationDao.findByStationNumber(stationNumber);
        List<Person> persons = new ArrayList<>();
        for (FireStation fireStation : fireStations) {
            List<Person> personsFromAddress = personDao.findByAddress(fireStation.getAddress());
            persons.addAll(personsFromAddress);
        }

        return PersonMapper.mapToPersonStations(persons);
    }
}
