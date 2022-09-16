package com.softwify.safetynetAlert.controller;

import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.service.MedicalRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/medicalRecords")
public class MedicalRecordController {
    @Autowired
    private MedicalRecordService medicalRecordService;

    @GetMapping
    public List<MedicalRecord> findAll() {
       return medicalRecordService.findAll();
    }
}
