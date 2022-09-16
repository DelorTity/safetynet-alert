package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.model.FireStation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class FireStationServicesImplTest {
    FireStationsDao fireStationsDao = Mockito.mock(FireStationsDao.class);
    FireStationServicesImpl fireStationServices = new FireStationServicesImpl(fireStationsDao);
    @Test
    public void testShouldVerifyTheNumberOfTimeThatPersonDaoIsCallIntoPersonService() {
        fireStationsDao.findAll();
        verify(fireStationsDao, times(1)).findAll();
    }

    @Test
    public void testShouldVerifyThatFireStationSaveIsReturn() {
        FireStation fireStation = FireStation.builder().station(6).build();
        Optional<FireStation> optionalFireStation = Optional.of(fireStation);
        when(fireStationsDao.save(fireStation)).thenReturn(optionalFireStation);

        Optional<FireStation> saveFireStation = fireStationServices.save(fireStation);
        assertEquals(6, saveFireStation.get().getStation());

        verify(fireStationsDao, times(1)).save(fireStation);
    }
}