package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dto.PersonStarter;
import com.softwify.safetynetAlert.dto.PersonStation;

import java.util.List;

public interface PersonStationService {
    PersonStarter findPersonByStation(int station);

}
