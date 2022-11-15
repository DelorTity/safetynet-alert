package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.ecception.PersonAlreadyExitException;
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
    public Optional<MedicalRecord> findByFirstnameAndLastname(String firstName, String lastName) {
        Optional<MedicalRecord> optionalPerson = medicalRecordDao.findByFirstnameAndLastname(firstName, lastName);
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

    @Override
    public Optional<MedicalRecord> save(MedicalRecord medicalRecord) {
        Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.save(medicalRecord);
        if (optionalMedicalRecord.isEmpty()) {
            throw new PersonAlreadyExitException();
        }
        return optionalMedicalRecord;
    }

    @Override
    public Optional<MedicalRecord> delete(String firstname, String lastname) {
        Optional<MedicalRecord> delete = medicalRecordDao.delete(firstname, lastname);
        if (delete.isEmpty()) {
            throw new PersonNotFoundException();
        }
        return delete;
    }
}
