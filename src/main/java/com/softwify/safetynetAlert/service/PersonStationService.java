package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dto.PersonStation;

import java.util.List;

public interface PersonStationService {
    List<PersonStation> findPersonByStation(int station);

}
