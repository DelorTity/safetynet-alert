package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.model.MedicalRecord;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordService {
    List<MedicalRecord> findAll();

    Optional<MedicalRecord> findMedicalRecordByFirstnameAndLastname(String firstName, String lastName);

    Optional<MedicalRecord> update(MedicalRecord medicalRecord);
}
