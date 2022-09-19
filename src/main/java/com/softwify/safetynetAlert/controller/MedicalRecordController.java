package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.ecception.PersonAlreadyExitException;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/medicalrecord")
public class MedicalRecordController {

    @Autowired
    private MedicalRecordService medicalRecordService;

    //Récupére la liste des Medical Records
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

    @PutMapping
    public ResponseEntity<MedicalRecord> update(@RequestBody MedicalRecord medicalRecord) throws Exception {
        try {
            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordService.update(medicalRecord);
            MedicalRecord updatedMedicalRecord = optionalMedicalRecord.get();
            return ResponseEntity.ok(updatedMedicalRecord);
        } catch (PersonNotFoundException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
