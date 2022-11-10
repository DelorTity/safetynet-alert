package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public Optional<MedicalRecord> update(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> optionalMedicalRecord = findMedicalRecordByFirstnameAndLastname(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (optionalMedicalRecord.isPresent()) {
            MedicalRecord existingMedicalRecord = optionalMedicalRecord.get();
            existingMedicalRecord.setBirthdate(medicalRecord.getBirthdate());
            existingMedicalRecord.setMedications(medicalRecord.getMedications());
            existingMedicalRecord.setAllergies(medicalRecord.getAllergies());
            return Optional.of(medicalRecord);
        }
        return Optional.empty();
    }

    @Override
    public Optional<MedicalRecord> save(MedicalRecord medicalRecord) {

        Optional<MedicalRecord> optionalMedicalRecord = findMedicalRecordByFirstnameAndLastname(medicalRecord.getFirstName(), medicalRecord.getLastName());
        if (optionalMedicalRecord.isPresent()) {
            return Optional.empty();
        }
        List<MedicalRecord> medicalRecords = dataStoreManager.getMedicalRecords();
        medicalRecords.add(medicalRecord);
        return Optional.of(medicalRecord);
    }

    @Override
    public Optional<MedicalRecord> delete(String firstname, String lastname) {
        Optional<MedicalRecord> optionalMedicalRecord = findMedicalRecordByFirstnameAndLastname(firstname, lastname);
        if (optionalMedicalRecord.isPresent()) {
            List<MedicalRecord> medicalRecords = dataStoreManager.getMedicalRecords();
            MedicalRecord medicalRecord = optionalMedicalRecord.get();
            medicalRecords.remove(medicalRecord);
            return optionalMedicalRecord;
        }
        return Optional.empty();
    }
}
