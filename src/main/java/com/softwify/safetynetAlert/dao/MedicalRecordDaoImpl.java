package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.MedicalRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class MedicalRecordDaoImpl implements MedicalRecordDao{
    private final DataStoreManager dataStoreManager;

    public MedicalRecordDaoImpl(DataStoreManager dataStoreManager) {
        this.dataStoreManager = dataStoreManager;
    }

    @Override
    public List<MedicalRecord> findAll() {
        return dataStoreManager.getMedicalRecords();
    }
}
