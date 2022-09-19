package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class MedicalRecordDaoImpl implements MedicalRecordDao {

    private final DataStoreManager dataStoreManager;

    public MedicalRecordDaoImpl(DataStoreManager dataStoreManager) {
        this.dataStoreManager = dataStoreManager;
    }

    @Override
    public List<MedicalRecord> findAll() {
        return dataStoreManager.getMedicalRecords();
    }

    @Override
    public Optional<MedicalRecord> findMedicalRecordByFirstnameAndLastname(String firstName, String lastName) {
        List<MedicalRecord> medicalRecords = dataStoreManager.getMedicalRecords();
        for (MedicalRecord medicalRecord: medicalRecords) {
            if (medicalRecord.getFirstName().equals(firstName) && medicalRecord.getLastName().equals(lastName)) {
                return Optional.of(medicalRecord);
            }
        }
        return Optional.empty();
    }
}
