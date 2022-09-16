package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.model.FireStation;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class FireStationServicesImplTest {
    FireStationsDao fireStationsDao = Mockito.mock(FireStationsDao.class);
    FireStationServicesImpl fireStationServices = new FireStationServicesImpl(fireStationsDao);
    @Test
    public void testShouldVerifyTheNumberOfTimeThatPersonDaoIsCallIntoPersonService() {
        fireStationsDao.findAll();
        verify(fireStationsDao, times(1)).findAll();
    }

}