package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.FireStation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

public class FireStationsDaoImplTest {
    DataStoreManager dataStoreManager = Mockito.mock(DataStoreManager.class);
    FireStationsDaoImpl fireStationsDao = new FireStationsDaoImpl(dataStoreManager);

    @Test
    public void getFireStationsReturnsExpectedSizeAndFireStations() {
        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().station(342).build(),
                FireStation.builder().station(565).build()
        );
        when(dataStoreManager.getFireStation()).thenReturn(fireStations);

        List<FireStation> fireStationList = fireStationsDao.findAll();
        assertEquals(342, fireStationList.get(0).getStation());
        assertEquals(2, fireStationList.size());

        verify(dataStoreManager, times(1)).getFireStation();
    }

    @Test
    void testShouldReturnOkWhenFireStationIsSaved() {
        List<FireStation> fireStationList = Arrays.asList(
                FireStation.builder().address("13-Douala-DR").build(),
                FireStation.builder().address("34-Yaounde-DR").build()
        );

        List<FireStation> fireStations = new ArrayList<>(fireStationList);

        when(dataStoreManager.getFireStation()).thenReturn(fireStations);

        FireStation fireStation = FireStation.builder().build();
        fireStations.add(fireStation);
        assertEquals(3, fireStations.size());
    }

    @Test
    public void getFireStationByAddressShouldReturnFalseWhenFireStationNotExist() {
        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().address("12 kumba dr").station(3).build(),
                FireStation.builder().address("432 Edea sr").station(0).build()
        );
        when(dataStoreManager.getFireStation()).thenReturn(fireStations);

        Optional<FireStation> optionalFireStation = fireStationsDao.findFireStationByAddress("kumba dr");
        assertFalse(optionalFireStation.isPresent());

        verify(dataStoreManager, times(1)).getFireStation();
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }
}