package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordDao medicalRecordDao;

    public MedicalRecordServiceImpl(MedicalRecordDao medicalRecordDao) {
        this.medicalRecordDao = medicalRecordDao;
    }
    @Override
    public List<MedicalRecord> findAll() {
        return medicalRecordDao.findAll();
    }

    @Override
    public Optional<MedicalRecord> findMedicalRecordByFirstnameAndLastname(String firstName, String lastName) {
        Optional<MedicalRecord> optionalPerson = medicalRecordDao.findMedicalRecordByFirstnameAndLastname(firstName, lastName);
        return Optional.of(optionalPerson.orElseThrow(PersonNotFoundException::new));
    }

    @Override
    public Optional<MedicalRecord> update(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> updatedMedicalRecord = medicalRecordDao.update(medicalRecord);
        if(updatedMedicalRecord.isEmpty()) {
            throw new PersonNotFoundException();
        }
        return updatedMedicalRecord;
    }


}
