package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dto.Child;
import com.softwify.safetynetAlert.dto.PersonFire;
import com.softwify.safetynetAlert.dto.PersonStarter;
import com.softwify.safetynetAlert.model.FireStation;

import java.util.List;

public interface PersonStationService {
    PersonStarter findPersonByStation(int station);

    List<Child> findPersonByAddress(String address);

    List<String> findPhoneNumberByStation(int fireStationNumber);

    List<PersonFire> findFireStationByAddress(String address);
}
