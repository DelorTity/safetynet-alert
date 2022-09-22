package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.ecception.FireStationAlreadyExitsException;
import com.softwify.safetynetAlert.ecception.FireStationNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FireStationServicesImplTest {
    FireStationsDao fireStationDao = Mockito.mock(FireStationsDao.class);
    FireStationServicesImpl fireStationService = new FireStationServicesImpl(fireStationDao);

    @Test
    public void testShouldVerifyTheNumberOfTimeThatDaoIsCallIntoService() {
        fireStationService.findAll();
        verify(fireStationDao, times(1)).findAll();
    }

    @Test
    public void testShouldVerifyThatFireStationSaveIsReturn() {
        FireStation fireStation = FireStation.builder().station(3).build();
        Optional<FireStation> optionalFireStation = Optional.of(fireStation);
        when(fireStationDao.save(fireStation)).thenReturn(optionalFireStation);

        Optional<FireStation> saveFireStation = fireStationService.addFireStation(fireStation);

        assertTrue(saveFireStation.isPresent());
        assertEquals(3, saveFireStation.get().getStation());

        verify(fireStationDao, times(1)).save(fireStation);
    }

    @Test
    public void saveShouldThrowExceptionWhenTheFireStationIsNotSave() {
        FireStation fireStation = FireStation.builder().station(3).build();
        assertThrows(FireStationAlreadyExitsException.class, () -> fireStationService.addFireStation(fireStation) );
        verify(fireStationDao, times(1)).save(fireStation);
    }

    @Test
    public void getFireStationByAddressShouldReturnInformations() {
        FireStation fireStation = FireStation.builder().address("12 tokyo dr").station(3).build();
        Optional<FireStation> optionalPerson = Optional.of(fireStation);
        when(fireStationDao.findByAddress("12 tokyo dr")).thenReturn(optionalPerson);

        Optional<FireStation> fireStationRetrieved = fireStationService.findFireStationByAddress("12 tokyo dr");

        assertTrue(fireStationRetrieved.isPresent());
        assertEquals("12 tokyo dr", fireStationRetrieved.get().getAddress());
        assertEquals(3, fireStationRetrieved.get().getStation());

        verify(fireStationDao, times(1)).findByAddress("12 tokyo dr");
    }

    @Test
    public void getFireStationByAddressShouldThrowExceptionWhenThereIsNoPerson() {
        assertThrows(FireStationNotFoundException.class, () -> fireStationService.findFireStationByAddress("12 tokyo dr"));
        verify(fireStationDao, times(1)).findByAddress("12 tokyo dr");
    }

    @Test
    void testShouldVerifyThatUpdateFireStationIsReturn() {
        FireStation fireStation = FireStation.builder().address("12 tokyo dr").station(3).build();

        when(fireStationDao.update(fireStation)).thenReturn(Optional.of(fireStation));
        Optional<FireStation> updateFireStation = fireStationService.updateFireStation(fireStation);

        assertTrue(updateFireStation.isPresent());
        assertEquals("12 tokyo dr", updateFireStation.get().getAddress());
        verify(fireStationDao, times(1)).update(fireStation);
    }

    @Test
    public void testShouldThrowExceptionWhenTheFireStationIsNotUpdate() {
        FireStation fireStation = FireStation.builder().address("12 tokyo dr").station(3).build();
        assertThrows(FireStationNotFoundException.class, () -> fireStationService.updateFireStation(fireStation) );
        verify(fireStationDao, times(1)).update(fireStation);
    }

    @Test
    public void testShouldCheckThatDeleteProvidedFireStation() {
        FireStation fireStation = FireStation.builder().address("12 tokyo dr").station(3).build();

        when(fireStationDao.delete("12 tokyo dr")).thenReturn(Optional.of(fireStation));

        Optional<FireStation> deleteFireStation = fireStationService.deleteFireStation("12 tokyo dr");
        assertTrue(deleteFireStation.isPresent());
        assertEquals("12 tokyo dr", deleteFireStation.get().getAddress());
        verify(fireStationDao, times(1)).delete("12 tokyo dr");
    }

    @Test
    public void testShouldThrowExceptionWhenTheFireStationIsNotDelete() {
        assertThrows(FireStationNotFoundException.class, () -> fireStationService.deleteFireStation("12 berlin dr") );
        verify(fireStationDao, times(1)).delete("12 berlin dr");
    }

}