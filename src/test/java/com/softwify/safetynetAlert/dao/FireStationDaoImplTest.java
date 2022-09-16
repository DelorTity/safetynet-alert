package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.FireStation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class FireStationDaoImplTest {
    DataStoreManager dataStoreManager = Mockito.mock(DataStoreManager.class);
    FireStationDaoImpl fireStationDao = new FireStationDaoImpl(dataStoreManager);

    @Test
    public void getFireStationReturnsExpectedSize() {
        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().address("12-limbe-DR").build(),
                FireStation.builder().address("129-Douala-DR").build()
        );

        when(dataStoreManager.getFireStation()).thenReturn(fireStations);

        List<FireStation> fireStationsList = fireStationDao.findAll();
        assertEquals("12-limbe-DR", fireStationsList.get(0).getAddress());
        assertEquals(2, fireStationsList.size());

        verify(dataStoreManager, times(1)).getFireStation();
    }

    @Test
    public void testShouldVerifyThatFireStationSizeAddWhenSaveCorrect() {
        List<FireStation> fireStationsArrays = Arrays.asList(
                FireStation.builder().address("12-limbe-DR").build(),
                FireStation.builder().address("129-Douala-DR").build()
        );
        List<FireStation> fireStations = new ArrayList<>(fireStationsArrays);

        when(dataStoreManager.getFireStation()).thenReturn(fireStations);
        FireStation fireStation = FireStation.builder().build();
        fireStationDao.save(fireStation);

        assertEquals(3, fireStations.size());
    }


    @Test
    public void testShouldReturnEmptyWhenNotSavingFireStation() {
        List<FireStation> fireStationsArrays = Arrays.asList(
                FireStation.builder().address("12-limbe-DR").build(),
                FireStation.builder().address("129-Douala-DR").build()
        );
        when(dataStoreManager.getFireStation()).thenReturn(fireStationsArrays);

        Optional<FireStation> save = fireStationDao.save(fireStationsArrays.get(0));

        assertTrue(save.isEmpty());
    }

    @Test
    public void getFireStationByAdressShouldReturnFalseWhenFireStationNotExist() {
        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().address("12 tokyo dr").station(3).build(),
                FireStation.builder().address("432 bombe sr").station(0).build()
        );
        when(dataStoreManager.getFireStation()).thenReturn(fireStations);

        Optional<FireStation> fireStationByAdresse = fireStationDao.findFireStationByAddress("tokyo dr");
        assertFalse(fireStationByAdresse.isPresent());

        verify(dataStoreManager, times(1)).getFireStation();
    }

    @Test
    public void getFireStationByAdressShouldReturnTrueWhenFireStationExist() {
        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().address("12 tokyo dr").station(3).build(),
                FireStation.builder().address("432 bombe sr").station(0).build()
        );
        when(dataStoreManager.getFireStation()).thenReturn(fireStations);

        Optional<FireStation> optionalFireStation = fireStationDao.findFireStationByAddress("12 tokyo dr");

        assertTrue(optionalFireStation.isPresent());
        assertEquals(3, optionalFireStation.get().getStation());
        verify(dataStoreManager, times(1)).getFireStation();
    }

    @Test
    void testShouldReturnFireStationWhenUpdate() {
        FireStation updatedfireStation = FireStation.builder()
                .address("12 tokyo dr")
                .station(3)
                .build();

        when(dataStoreManager.getFireStation()).thenReturn(new ArrayList<>(Collections.singleton(updatedfireStation)));
        FireStation fireStation = FireStation.builder()
                .address("432 bombe sr")
                .station(432)
                .build();

        Optional<FireStation> optionalFireStation = fireStationDao.update(fireStation);
        assertFalse(optionalFireStation.isPresent());
    }

    @Test
    void testShouldReturnTrueWhenNotUpdateFireStation() {
        FireStation fireStation = FireStation.builder()
                .address("432 bombe sr")
                .station(432)
                .build();
        Optional<FireStation> optionalFireStation = fireStationDao.update(fireStation);
        assertTrue(optionalFireStation.isEmpty());
    }

    @Test
    void deleteFireStationTest() {
        List<FireStation> fireStationsArrays = Arrays.asList(
                FireStation.builder().address("12-limbe-DR").build(),
                FireStation.builder().address("129-Douala-DR").build()
        );
        List<FireStation> fireStations = new ArrayList<>(fireStationsArrays);

        when(dataStoreManager.getFireStation()).thenReturn(fireStations);

        Optional<FireStation> delete = fireStationDao.delete("12-limbe-DR");
        assertEquals("12-limbe-DR", delete.get().getAddress());
        verify(dataStoreManager, atLeastOnce()).getFireStation();
    }

    @Test
    void deleteShouldStopWhenNoPersonInTheList() {
        List<FireStation> fireStationsArrays = Arrays.asList(
                FireStation.builder().address("12-limbe-DR").build(),
                FireStation.builder().address("129-Douala-DR").build()
        );
        List<FireStation> fireStations = new ArrayList<>(fireStationsArrays);

        when(dataStoreManager.getFireStation()).thenReturn(fireStations);

        Optional<FireStation> delete = fireStationDao.delete("12-kumba-DR");
        assertTrue(delete.isEmpty());
        verify(dataStoreManager, atLeastOnce()).getFireStation();
    }
}
