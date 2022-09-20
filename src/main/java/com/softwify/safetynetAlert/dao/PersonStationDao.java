package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.Person;

import java.util.List;

public interface PersonStationDao {
    List<FireStation> findFireStationByStation(int station);
    List<Person> findPersonByStation(int station);
}
