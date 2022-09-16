package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.model.MedicalRecord;

import java.util.List;

public interface MedicalRecordService {
    List<MedicalRecord> findAll();

}
