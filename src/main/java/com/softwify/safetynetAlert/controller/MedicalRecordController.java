package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.exceptions.PersonAlreadyExistsException;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/medicalRecords")
public class MedicalRecordController {
    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping
    public List<MedicalRecord> findAll() {
       return medicalRecordService.findAll();
    }

    @GetMapping(value = "/{firstname}/{lastname}")
    public ResponseEntity<MedicalRecord> retrievedMedicalRecord(@PathVariable String firstname, @PathVariable String lastname) {
        try {
            Optional<MedicalRecord> medicalRecord = medicalRecordService.findMedicalRecordByFirstnameAndLastname(firstname, lastname);
            return ResponseEntity.ok(medicalRecord.get());
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping
    public ResponseEntity<MedicalRecord> addMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        try {
            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordService.saveMedicalRecord(medicalRecord);
            return ResponseEntity.ok(optionalMedicalRecord.get());
        } catch (PersonAlreadyExistsException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping
    public ResponseEntity<MedicalRecord> updateMedicalRecord(@RequestBody MedicalRecord medicalRecord) {
        try {
            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordService.updateMedicalRecord(medicalRecord);
            MedicalRecord updatedMedical = optionalMedicalRecord.get();
            return ResponseEntity.ok(updatedMedical);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping(value = "/{firstname}/{lastname}")
    public ResponseEntity<Person> deletePerson(@PathVariable String firstname, @PathVariable String lastname) {
        try {
            medicalRecordService.deleteMedicalRecord(firstname, lastname);
            return ResponseEntity.noContent().build();
        } catch (PersonNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
