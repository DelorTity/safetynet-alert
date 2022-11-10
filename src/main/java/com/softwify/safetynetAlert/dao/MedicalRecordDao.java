package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.MedicalRecord;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface MedicalRecordDao {
    List<MedicalRecord> findAll();

    Optional<MedicalRecord> findMedicalRecordByFirstnameAndLastname(String firstName, String lastName);

    Optional<MedicalRecord> update(MedicalRecord medicalRecord);

    Optional<MedicalRecord> save(MedicalRecord medicalRecord);

    Optional<MedicalRecord> delete(String firstname, String lastname);
}
