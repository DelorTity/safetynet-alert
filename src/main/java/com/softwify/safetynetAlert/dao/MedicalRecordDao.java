package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordDao {
    List<MedicalRecord> findAll();

}
