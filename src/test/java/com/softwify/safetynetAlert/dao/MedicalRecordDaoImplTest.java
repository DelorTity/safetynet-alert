package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.MedicalRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MedicalRecordDaoImplTest {

    DataStoreManager dataStoreManager = Mockito.mock(DataStoreManager.class);
    MedicalRecordDaoImpl medicalRecordDao = new MedicalRecordDaoImpl(dataStoreManager);

    @Test
    public void getMedicalRecordReturnsExpectedSizeAndPersonFirstNameAndAlsoExpectedMedicationsList() {
        List<MedicalRecord> medicalRecords = Arrays.asList(
                MedicalRecord.builder().firstName("john").medications(Collections.singletonList("paracetamol:500g")).build(),
                MedicalRecord.builder().firstName("jean").medications(Collections.singletonList("abendazol:400mg")).build()
        );
        when(dataStoreManager.getMedicalRecords()).thenReturn(medicalRecords);

        List<MedicalRecord> medicalRecordList = medicalRecordDao.findAll();
        assertEquals("john", medicalRecordList.get(0).getFirstName());
        assertEquals(Collections.singletonList("paracetamol:500g"), medicalRecordList.get(0).getMedications());
        assertEquals(2, medicalRecordList.size());

        verify(dataStoreManager, times(1)).getMedicalRecords();
    }

    @Test
    public void getByFirstnameAndLastnameShouldReturnTrueWhenMedicalRecordNotExist(){
        List<MedicalRecord> medicalRecords = Arrays.asList(MedicalRecord.builder()
                .firstName("Delor")
                .lastName("Tatus")
                .medications(Collections.singletonList("paracetamol:500g"))
                .build()
        );
        when(dataStoreManager.getMedicalRecords()).thenReturn(medicalRecords);
        Optional<MedicalRecord> medicalRecord = medicalRecordDao.findMedicalRecordByFirstnameAndLastname("Delor", "Tity");
        assertFalse(medicalRecord.isPresent());

        verify(dataStoreManager, times(1)).getMedicalRecords();
    }

    @Test
    public void getByFirstnameAndLastnameShouldReturnFalseWhenMedicalRecordNotExist() {
        List<MedicalRecord> medicalRecords = Arrays.asList(MedicalRecord.builder()
                .firstName("jean")
                .lastName("pierre")
                .medications(Collections.singletonList("abendazol:350mg"))
                .build()
        );
        when(dataStoreManager.getMedicalRecords()).thenReturn(medicalRecords);

        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.findMedicalRecordByFirstnameAndLastname("jean", "pierre");

        assertFalse(optionalMedicalRecord.isEmpty());
        assertEquals("jean", optionalMedicalRecord.get().getFirstName());
        assertEquals(Collections.singletonList("abendazol:350mg"), optionalMedicalRecord.get().getMedications());
        verify(dataStoreManager, times(1)).getMedicalRecords();
    }

    @Test
    void testShouldStopWhenTheMedicalRecordToUpdateHasBeenUpdateExist() {
        MedicalRecord updatedMedicalRecord = MedicalRecord.builder()
                .firstName("Sen")
                .lastName("Floor")
                .medications(Collections.singletonList("metizan:350mg"))
                .build();
        when(dataStoreManager.getMedicalRecords()).thenReturn(new ArrayList<>(Collections.singleton(updatedMedicalRecord)));

        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName("Sen")
                .lastName("Floor")
                .medications(Collections.singletonList("paracetamol:350mg"))
                .build();
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.update(medicalRecord);
        assertEquals(Collections.singletonList("paracetamol:350mg"), medicalRecord.getMedications());
        assertTrue(optionalMedicalRecord.isPresent());
        verify(dataStoreManager, atLeastOnce()).getMedicalRecords();
    }

    @Test
    void testShouldReturnFalseWhenMedicalRecordIsNotUpdated() {
        MedicalRecord updatedMedicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("Boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build();
        when(dataStoreManager.getMedicalRecords()).thenReturn(new ArrayList<>(Collections.singleton(updatedMedicalRecord)));
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("ben")
                .medications(Collections.singletonList("aznol:350mg"))
                .build();
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.update(medicalRecord);
        assertFalse(optionalMedicalRecord.isPresent());
    }
}