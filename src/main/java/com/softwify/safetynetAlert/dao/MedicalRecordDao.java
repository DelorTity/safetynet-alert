package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.MedicalRecord;

import java.util.List;
import java.util.Optional;

public interface MedicalRecordDao {
    List<MedicalRecord> findAll();

    Optional<MedicalRecord> findByFirstnameAndLastname(String firstName, String lastName);

    Optional<MedicalRecord> update(MedicalRecord medicalRecord);

    Optional<MedicalRecord> save(MedicalRecord medicalRecord);

    Optional<MedicalRecord> delete(String firstname, String lastname);
}
