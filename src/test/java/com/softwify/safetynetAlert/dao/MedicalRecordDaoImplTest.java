package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.MedicalRecord;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
}
