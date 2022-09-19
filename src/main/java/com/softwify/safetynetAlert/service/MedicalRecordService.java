package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.model.MedicalRecord;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordService {
    List<MedicalRecord> findAll();
    Optional<MedicalRecord> findMedicalRecordByFirstnameAndLastname(String firstName, String lastName);
    Optional<MedicalRecord> saveMedicalRecord(MedicalRecord medicalRecord);
    Optional<MedicalRecord> updateMedicalRecord(MedicalRecord medicalRecord);
    Optional<MedicalRecord> deleteMedicalRecord(String firstname, String lastname);
}
