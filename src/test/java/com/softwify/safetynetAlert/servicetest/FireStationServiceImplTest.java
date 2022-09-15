package com.softwify.safetynetAlert.servicetest;

import com.softwify.safetynetAlert.dao.FireStationDao;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.service.FireStationServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FireStationServiceImplTest {
    FireStationDao fireStationDao = Mockito.mock(FireStationDao.class);
    FireStationServiceImpl fireStationService = new FireStationServiceImpl(fireStationDao);

    @Test
    public void testShouldVerifyTheNumberOfTimeThatDaoIsCallIntoService() {
        fireStationService.getAll();
        verify(fireStationDao, times(1)).findAll();
    }

    @Test
    public void testShouldVerifyThatFireStationSaveIsReturn() {
        FireStation fireStation = FireStation.builder().station(3).build();
        Optional<FireStation> optionalFireStation = Optional.of(fireStation);
        when(fireStationDao.addFireStation(fireStation)).thenReturn(optionalFireStation);

        Optional<FireStation> saveFirestation = fireStationService.addedFireStation(fireStation);
        assertEquals(3, saveFirestation.get().getStation());

        verify(fireStationDao, times(1)).addFireStation(fireStation);
    }

}
