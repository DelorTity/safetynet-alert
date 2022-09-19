package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.MedicalRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
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

        Optional<MedicalRecord> medicalRecordRetrieved = medicalRecordService.findByFirstnameAndLastname(medicalRecord.getFirstName(), medicalRecord.getLastName());
        assertNotNull(medicalRecordRetrieved);
        assertEquals("Marc", medicalRecordRetrieved.get().getFirstName());
        assertEquals(Collections.singletonList("Metizen:500g"), medicalRecordRetrieved.get().getMedications());

        verify(medicalRecordDao, times(1)).findMedicalRecordByFirstnameAndLastname(medicalRecord.getFirstName(), medicalRecord.getLastName());
    }

    @Test
    void testShouldVerifyThatReturnUpdatedMedicalRecord() {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("Boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build();

        when(medicalRecordDao.update(medicalRecord)).thenReturn(Optional.of(medicalRecord));
        Optional<MedicalRecord> updateMedicalRecord = medicalRecordService.update(medicalRecord);
        assertTrue(updateMedicalRecord.isPresent());
    }

    @Test
    public void testShouldThrowExceptionWhenTheMedicalRecordIsNotUpdate() {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("Boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build();
        assertThrows(PersonNotFoundException.class, () -> medicalRecordService.update(medicalRecord) );
        verify(medicalRecordDao, times(1)).update(medicalRecord);
    }
}