package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.ecception.FireStationNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        FireStation fireStation = FireStation.builder().station(10).build();
        Optional<FireStation> optionalFireStation = Optional.of(fireStation);
        when(fireStationsDao.save(fireStation)).thenReturn(optionalFireStation);

        Optional<FireStation> saveFireStation = fireStationServices.addFireStation(fireStation);
        assertEquals(10, saveFireStation.get().getStation());

        verify(fireStationsDao, times(1)).save(fireStation);
    }

    @Test
    public void getFireStationByAdressShouldReturnTheInformations() {
        FireStation fireStation = FireStation.builder().address("12 loum dr").station(3).build();
        Optional<FireStation> optionalPerson = Optional.of(fireStation);
        when(fireStationsDao.findFireStationByAddress("12 loum dr")).thenReturn(optionalPerson);

        Optional<FireStation> fireStationRetrieved = fireStationServices.findFireStationByAddress("12 loum dr");
        assertNotNull(fireStationRetrieved);
        assertEquals("12 loum dr", fireStationRetrieved.get().getAddress());
        assertEquals(3,fireStationRetrieved.get().getStation());

        verify(fireStationsDao, times(1)).findFireStationByAddress("12 loum dr");
    }

    @Test
    public void getFireStationByAdressShouldThrowExceptionWhenThereIsNoPerson() {
        assertThrows(FireStationNotFoundException.class, () -> fireStationServices.findFireStationByAddress("12 Melon dr"));
        verify(fireStationsDao, times(1)).findFireStationByAddress("12 Melon dr");
    }

    @Test
    void testShouldVerifyThatUpdateFireStationIsReturn() {
        FireStation fireStation = FireStation.builder().address("12 Jombe dr").station(3).build();

        when(fireStationsDao.update(fireStation)).thenReturn(Optional.of(fireStation));
        Optional<FireStation> updateFireStation = fireStationServices.updateFireStation(fireStation);

        assertTrue(updateFireStation.isPresent());
        assertEquals("12 Jombe dr", updateFireStation.get().getAddress());
        verify(fireStationsDao, times(1)).update(fireStation);
    }

    @Test
    public void testShouldThrowExceptionWhenTheFireStationIsNotUpdate() {
        FireStation fireStation = FireStation.builder().address("12 tokyo dr").station(3).build();
        assertThrows(FireStationNotFoundException.class, () -> fireStationServices.updateFireStation(fireStation) );
        verify(fireStationsDao, times(1)).update(fireStation);
    }

    @Test
    public void testShouldCheckThatDeleteProvidedFireStation() {
        FireStation fireStation = FireStation.builder().address("12 Douala dr").station(3).build();

        when(fireStationsDao.delete("12 Douala dr")).thenReturn(Optional.of(fireStation));

        Optional<FireStation> deleteFireStation = fireStationServices.deleteFireStation("12 Douala dr");
        assertTrue(deleteFireStation.isPresent());
        assertEquals("12 Douala dr", deleteFireStation.get().getAddress());
        verify(fireStationsDao, times(1)).delete("12 Douala dr");
    }

    @Test
    public void testShouldThrowExceptionWhenTheFireStationIsNotDelete() {
        assertThrows(FireStationNotFoundException.class, () -> fireStationServices.deleteFireStation("12 berlin dr") );
        verify(fireStationsDao, times(1)).delete("12 berlin dr");
    }

}