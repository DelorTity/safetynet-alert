package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.model.MedicalRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

class MedicalRecordServiceImplTest {

    MedicalRecordDao medicalRecordDao = Mockito.mock(MedicalRecordDao.class);
    MedicalRecordServiceImpl medicalRecordService = new MedicalRecordServiceImpl(medicalRecordDao);

    @Test
    public void testShouldVerifyTheNumberOfTimeThatPersonDaoIsCallIntoPersonService() {
        medicalRecordService.findAll();
        verify(medicalRecordDao, times(1)).findAll();
    }

    @Test
    public void getPersonByFirstnameAndLastnameShouldReturnThePersonAndMedicalRecord() {
        MedicalRecord medicalRecord = MedicalRecord.builder().firstName("Marc").lastName("Jean").medications(Collections.singletonList("Metizen:500g")).build();
        Optional<MedicalRecord> optionalMedicalRecord = Optional.of(medicalRecord);
        when(medicalRecordDao.findMedicalRecordByFirstnameAndLastname(medicalRecord.getFirstName(), medicalRecord.getLastName())).thenReturn(optionalMedicalRecord);

        Optional<MedicalRecord> medicalRecordRetrieved = medicalRecordService.findMedicalRecordByFirstnameAndLastname(medicalRecord.getFirstName(), medicalRecord.getLastName());
        assertNotNull(medicalRecordRetrieved);
        assertEquals("Marc", medicalRecordRetrieved.get().getFirstName());
        assertEquals(Collections.singletonList("Metizen:500g"), medicalRecordRetrieved.get().getMedications());

        verify(medicalRecordDao, times(1)).findMedicalRecordByFirstnameAndLastname(medicalRecord.getFirstName(), medicalRecord.getLastName());
    }
}