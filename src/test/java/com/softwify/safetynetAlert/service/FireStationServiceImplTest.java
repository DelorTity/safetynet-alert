package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationDao;
import com.softwify.safetynetAlert.exceptions.FireStationAlreadyExistException;
import com.softwify.safetynetAlert.exceptions.FireStationNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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
        when(fireStationDao.save(fireStation)).thenReturn(optionalFireStation);

        Optional<FireStation> saveFirestation = fireStationService.addedFireStation(fireStation);
        assertEquals(3, saveFirestation.get().getStation());

        verify(fireStationDao, times(1)).save(fireStation);
    }

    @Test
    public void saveShouldThrowExceptionWhenTheFireStationIsNotSave() {
        FireStation fireStation = FireStation.builder().station(3).build();
        assertThrows(FireStationAlreadyExistException.class, () -> fireStationService.addedFireStation(fireStation) );
        verify(fireStationDao, times(1)).save(fireStation);
    }

    @Test
    public void getFireStationByAdressShouldReturnTheInformations() {
        FireStation fireStation = FireStation.builder().address("12 tokyo dr").station(3).build();
        Optional<FireStation> optionalPerson = Optional.of(fireStation);
        when(fireStationDao.findByAddress("12 tokyo dr")).thenReturn(optionalPerson);

        Optional<FireStation> firestationRetrieved = fireStationService.findFireStationByAddress("12 tokyo dr");
        assertNotNull(firestationRetrieved);
        assertEquals("12 tokyo dr", firestationRetrieved.get().getAddress());
        assertEquals(3, firestationRetrieved.get().getStation());

        verify(fireStationDao, times(1)).findByAddress("12 tokyo dr");
    }

    @Test
    public void getFireStationByAdressShouldThrowExceptionWhenThereIsNoPerson() {
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

    @Test
    public void testShouldVerifyThatFireStationIsReturn() {
        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().station(3).build(),
                FireStation.builder().station(10).build());

        when(fireStationDao.findByStationNumbers(anyList())).thenReturn(fireStations);

        List<Integer> numbers = new ArrayList<>();
        List<FireStation> byStations = fireStationService.findByStations(numbers);
        assertEquals(3, byStations.get(0).getStation());

        verify(fireStationDao, times(1)).findByStationNumbers(numbers);
    }
}
