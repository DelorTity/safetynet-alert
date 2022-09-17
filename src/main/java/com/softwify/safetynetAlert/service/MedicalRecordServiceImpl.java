package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.model.MedicalRecord;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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

    @Override
    public Optional<MedicalRecord> findMedicalRecordByFirstnameAndLastname(String firstName, String lastName) {
        Optional<MedicalRecord> optionalPerson = medicalRecordDao.findMedicalRecordByFirstnameAndLastname(firstName, lastName);
        return Optional.of(optionalPerson.orElseThrow(PersonNotFoundException::new));
    }
}
