package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.model.Person;

import java.util.List;

public interface PersonStationService {
    List<Person> findPersonByStation(int station);

}
