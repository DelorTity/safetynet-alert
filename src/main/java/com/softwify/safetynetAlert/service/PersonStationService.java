package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dto.*;

import java.util.List;

public interface PersonStationService {
    PersonStarter findPersonByStation(int station);
    List<Child> findPersonByAddress(String address);
    List<String> findPhoneNumberByStation(int firestationNumber);

    List<PersonFire> findPersonFireByAddress(String address);

    List<FloodStation> findFloodByStationNumber(List<Integer> stationNumbers);

    List<PersonInfo> findPersonByFirstAndLastName(String firstname, String lastname);

    List<String> findPersonByCity(String city);
}
