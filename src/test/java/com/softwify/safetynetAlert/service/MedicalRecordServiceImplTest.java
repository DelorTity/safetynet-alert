package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.model.MedicalRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class MedicalRecordServiceImplTest {
    MedicalRecordDao medicalRecordDao = Mockito.mock(MedicalRecordDao.class);
    MedicalRecordServiceImpl medicalRecordService = new MedicalRecordServiceImpl(medicalRecordDao);
    @Test
    public void testShouldVerifyTheNumberOfTimeThatDaoIsCallIntoService() {
        medicalRecordService.findAll();
        verify(medicalRecordDao, times(1)).findAll();
    }

    @Test
    public void getPersonByFirstnameAndLastnameShouldReturnThePerson() {
        String firstName = "John";
        String lastName = "Boyd";
        MedicalRecord medicalRecord = MedicalRecord.builder().firstName("John").lastName("Boyd").build();
        Optional<MedicalRecord> optionalMedicalRecord = Optional.of(medicalRecord);
        when(medicalRecordDao.findMedicalRecordByFirstnameAndLastname(firstName, lastName)).thenReturn(optionalMedicalRecord);

        Optional<MedicalRecord> medicalRecordRetrieved = medicalRecordService.findMedicalRecordByFirstnameAndLastname(firstName, lastName);
        assertNotNull(medicalRecordRetrieved);
        assertEquals("John", medicalRecordRetrieved.get().getFirstName());
        assertEquals("Boyd", medicalRecordRetrieved.get().getLastName());

        verify(medicalRecordDao, times(1)).findMedicalRecordByFirstnameAndLastname(firstName, lastName);
    }

    @Test
    public void getPersonByFirstnameAndLastnameShouldThrowExceptionWhenThereIsNoPerson() {
        when(medicalRecordDao.findMedicalRecordByFirstnameAndLastname(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> medicalRecordService.findMedicalRecordByFirstnameAndLastname("Jack", "James"));
        verify(medicalRecordDao, times(1)).findMedicalRecordByFirstnameAndLastname("Jack", "James");
    }
}
