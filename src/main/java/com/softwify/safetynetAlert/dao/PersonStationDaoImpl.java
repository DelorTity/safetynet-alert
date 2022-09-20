package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class PersonStationDaoImpl implements PersonStationDao {
    private DataStoreManager dataStoreManager;
    private PersonDao personDao;

    public PersonStationDaoImpl(DataStoreManager dataStoreManager, PersonDao personDao) {
        this.dataStoreManager = dataStoreManager;
        this.personDao = personDao;
    }

    @Override
    public List<FireStation> findFireStationByStation(int station) {
        List<FireStation> fireStations = dataStoreManager.getFireStation();
        return fireStations.stream().filter(s -> s.getStation() == station).collect(Collectors.toList());

    }

    @Override
    public List<Person> findPersonByStation(int station) {
        List<String> fireStationAddress = findFireStationByStation(station).stream().map(s->s.getAddress()).collect(Collectors.toList());
        List<Person> persons = personDao.findAll();
        persons.removeIf(person -> !fireStationAddress.contains(person.getAddress()));
        return persons;
    }
}
