package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.MedicalRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MedicalRecordDaoImplTest {
    DataStoreManager dataStoreManager = Mockito.mock(DataStoreManager.class);

    MedicalRecordDaoImpl medicalRecordDao = new MedicalRecordDaoImpl(dataStoreManager);
    @Test
    public void getMedicalRecordReturnsExpectedSize() {
        List<MedicalRecord> medicalRecords = Arrays.asList(MedicalRecord.builder()
                .firstName("john")
                .lastName("boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build()
        );

        when(dataStoreManager.getMedicalRecords()).thenReturn(medicalRecords);

        List<MedicalRecord> medicalRecordList = medicalRecordDao.findAll();
        assertEquals("john", medicalRecordList.get(0).getFirstName());
        assertEquals("boyd", medicalRecordList.get(0).getLastName());
        assertEquals(1, medicalRecordList.size());

        verify(dataStoreManager, times(1)).getMedicalRecords();
    }

    @Test
    public void getByFirstnameAndLastnameShouldReturnTrueWhenMedicalRecordNotExist(){
        List<MedicalRecord> medicalRecords = Arrays.asList(MedicalRecord.builder()
                .firstName("john")
                .lastName("boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build()
        );
        when(dataStoreManager.getMedicalRecords()).thenReturn(medicalRecords);
        Optional<MedicalRecord> medicalRecord = medicalRecordDao.findMedicalRecordByFirstnameAndLastname("John", "pierre");
        assertTrue(medicalRecord.isEmpty());

        verify(dataStoreManager, times(1)).getMedicalRecords();
    }

    @Test
    public void getByFirstnameAndLastnameShouldReturnFalseWhenMedicalRecordNotExist() {
        List<MedicalRecord> medicalRecords = Arrays.asList(MedicalRecord.builder()
                .firstName("john")
                .lastName("boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build()
        );
        when(dataStoreManager.getMedicalRecords()).thenReturn(medicalRecords);

        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.findMedicalRecordByFirstnameAndLastname("john", "boyd");

        assertFalse(optionalMedicalRecord.isEmpty());
        assertEquals("john", optionalMedicalRecord.get().getFirstName());
        assertEquals("boyd", optionalMedicalRecord.get().getLastName());
        verify(dataStoreManager, times(1)).getMedicalRecords();
    }

    @Test
    public void testShouldVerifyThatMedicalRecordSizeAddWhenSaveCorrect() {
        List<MedicalRecord> arrayMedicalRecords = Arrays.asList(MedicalRecord.builder()
                .firstName("john")
                .lastName("boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build()
        );
        List<MedicalRecord> medicalRecords = new ArrayList<>(arrayMedicalRecords);

        when(dataStoreManager.getMedicalRecords()).thenReturn(medicalRecords);

        MedicalRecord medicalRecord = MedicalRecord.builder().build();
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.save(medicalRecord);

        assertEquals(2, medicalRecords.size());
    }

    @Test
    public void testShouldReturnEmptyWhenNotSavingPerson() {
        List<MedicalRecord> arrayMedicalRecords = Arrays.asList(MedicalRecord.builder()
                .firstName("john")
                .lastName("boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build()
        );
        List<MedicalRecord> medicalRecords = new ArrayList<>(arrayMedicalRecords);
        when(dataStoreManager.getMedicalRecords()).thenReturn(medicalRecords);

        MedicalRecord medicalRecord = MedicalRecord.builder().firstName("john").lastName("boyd").build();
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.save(medicalRecord);
        assertTrue(optionalMedicalRecord.isEmpty());
    }

    @Test
    void testShouldStopWhenTheMedicalRecordToUpdateNotExist() {
        MedicalRecord updatedMedicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("Boyd")
                .medications(Collections.singletonList("bendazol:350mg"))
                .build();
        when(dataStoreManager.getMedicalRecords()).thenReturn(new ArrayList<>(Collections.singleton(updatedMedicalRecord)));

        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("Boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build();
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.update(medicalRecord);
        assertTrue(optionalMedicalRecord.isPresent());
        verify(dataStoreManager, atLeastOnce()).getMedicalRecords();
    }

    @Test
    void testShouldReturnUpdatedMedicalRecord() {
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
        assertTrue(optionalMedicalRecord.isEmpty());
    }

    @Test
    void testShoulReturnDeletedMedicalRecord() {
        List<MedicalRecord> arrayMedicalRecords = Arrays.asList(MedicalRecord.builder()
                .firstName("john")
                .lastName("boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build()
        );
        List<MedicalRecord> medicalRecords = new ArrayList<>(arrayMedicalRecords);
        when(dataStoreManager.getMedicalRecords()).thenReturn(medicalRecords);

        Optional<MedicalRecord> delete = medicalRecordDao.delete("john", "boyd");
        assertEquals("john", delete.get().getFirstName());
        verify(dataStoreManager, atLeastOnce()).getMedicalRecords();
    }

    @Test
    void deleteShouldStopWhenNoMedicalRecordInTheList() {
        List<MedicalRecord> arrayMedicalRecords = Arrays.asList(MedicalRecord.builder()
                .firstName("john")
                .lastName("boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build()
        );
        List<MedicalRecord> medicalRecords = new ArrayList<>(arrayMedicalRecords);
        when(dataStoreManager.getMedicalRecords()).thenReturn(medicalRecords);

        Optional<MedicalRecord> delete = medicalRecordDao.delete("john", "ben");
        assertTrue(delete.isEmpty());
        verify(dataStoreManager, atLeastOnce()).getMedicalRecords();
    }
}
