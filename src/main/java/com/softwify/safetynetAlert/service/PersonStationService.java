package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dto.PersonStarter;

public interface PersonStationService {
    PersonStarter findPersonByStation(int station);

}
