package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.Person;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class FireStationsDaoImplTest {
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

}