package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.ecception.PersonAlreadyExitException;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.MedicalRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;
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
        when(medicalRecordDao.findByFirstnameAndLastname(firstName, lastName)).thenReturn(optionalMedicalRecord);

        Optional<MedicalRecord> medicalRecordRetrieved = medicalRecordService.findByFirstnameAndLastname(firstName, lastName);
        assertNotNull(medicalRecordRetrieved);
        assertEquals("John", medicalRecordRetrieved.get().getFirstName());
        assertEquals("Boyd", medicalRecordRetrieved.get().getLastName());

        verify(medicalRecordDao, times(1)).findByFirstnameAndLastname(firstName, lastName);
    }

    @Test
    public void getPersonByFirstnameAndLastnameShouldThrowExceptionWhenThereIsNoPerson() {
        when(medicalRecordDao.findByFirstnameAndLastname(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> medicalRecordService.findByFirstnameAndLastname("Jack", "James"));
        verify(medicalRecordDao, times(1)).findByFirstnameAndLastname("Jack", "James");
    }

    @Test
    public void saveShouldVerifyThatMedicalRecordSaveIsReturn() {
        Optional<MedicalRecord> optionalMedicalRecord = Optional.of(MedicalRecord.builder().firstName("John").lastName("Boyd").build());
        when(medicalRecordDao.save(optionalMedicalRecord.get())).thenReturn(optionalMedicalRecord);

        Optional<MedicalRecord> saveMedicalRecord = medicalRecordService.save(optionalMedicalRecord.get());
        assertEquals("John", saveMedicalRecord.get().getFirstName());
        assertEquals("Boyd", saveMedicalRecord.get().getLastName());

        verify(medicalRecordDao, times(1)).save(optionalMedicalRecord.get());
    }

    @Test
    public void saveShouldThrowExceptionWhenTheMedicalRecordIsNotSave() {
        MedicalRecord medicalRecord = MedicalRecord.builder().firstName("John").lastName("Boyd").build();
        assertThrows(PersonAlreadyExitException.class, () -> medicalRecordService.save(medicalRecord) );
        verify(medicalRecordDao, times(1)).save(medicalRecord);
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

    @Test
    public void testShouldCheckThatDeleteProvidedPerson() {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("pierre")
                .build();
        when(medicalRecordDao.delete(anyString(), anyString())).thenReturn(Optional.of(medicalRecord));

        Optional<MedicalRecord> deletedMedicalRecord = medicalRecordService.delete("John", "pierre");

        assertTrue(deletedMedicalRecord.isPresent());
        assertEquals("John", deletedMedicalRecord.get().getFirstName());
        verify(medicalRecordDao, times(1)).delete("John", "pierre");
    }

    @Test
    public void testShouldThrowExceptionWhenThePersonIsNotDelete() {
        assertThrows(PersonNotFoundException.class, () -> medicalRecordService.delete("joe", "ben") );
        verify(medicalRecordDao, times(1)).delete("joe", "ben");
    }
}