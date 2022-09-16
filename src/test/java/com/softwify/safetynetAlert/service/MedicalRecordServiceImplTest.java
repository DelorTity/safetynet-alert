package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MedicalRecordServiceImplTest {
    MedicalRecordDao medicalRecordDao = Mockito.mock(MedicalRecordDao.class);
    MedicalRecordServiceImpl medicalRecordService = new MedicalRecordServiceImpl(medicalRecordDao);
    @Test
    public void testShouldVerifyTheNumberOfTimeThatDaoIsCallIntoService() {
        medicalRecordService.findAll();
        verify(medicalRecordDao, times(1)).findAll();
    }
}
