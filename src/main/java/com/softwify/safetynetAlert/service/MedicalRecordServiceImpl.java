package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService{
    private MedicalRecordDao medicalRecordDao;

    public MedicalRecordServiceImpl(MedicalRecordDao medicalRecordDao) {
        this.medicalRecordDao = medicalRecordDao;
    }

    @Override
    public List<MedicalRecord> findAll() {
        return medicalRecordDao.findAll();
    }
}
