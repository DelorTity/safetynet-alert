package com.softwify.safetynetAlert.daotest;

import com.softwify.safetynetAlert.dao.DataStoreManager;
import com.softwify.safetynetAlert.dao.FireStationDaoImpl;
import com.softwify.safetynetAlert.model.FireStation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
        fireStationDao.addFireStation(fireStation);

        assertEquals(3, fireStations.size());
    }
}
