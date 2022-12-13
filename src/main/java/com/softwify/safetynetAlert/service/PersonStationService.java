package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dto.*;

import java.util.List;

public interface PersonStationService {
    PersonStarter findPersonByStation(int station);

    List<Child> findPersonByAddress(String address);

    List<String> findPhoneNumberByStation(int fireStationNumber);

    List<PersonFire> findFireStationByAddress(String address);

    List<FloodStation> findFloodByStationNumber(List<Integer> stationNumber);

    List<PersonInfo> findPersonByFirstNameAndLastName(String firstName, String lastName);

    List<String> findBYCity(String city);
}