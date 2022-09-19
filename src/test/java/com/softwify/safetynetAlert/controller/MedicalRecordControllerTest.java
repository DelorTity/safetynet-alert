package com.softwify.safetynetAlert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = {MedicalRecordController.class})
class MedicalRecordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndMedicalRecordLengthIsCorrect() throws Exception {
        List<MedicalRecord> medicalRecords = Arrays.asList(
                MedicalRecord.builder().medications(Collections.singletonList("paracetamol")).build(),
                MedicalRecord.builder().firstName("jean").build()
        );
        when(medicalRecordService.findAll()).thenReturn(medicalRecords);

        MvcResult result = mockMvc.perform(get("/medicalrecord"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        MedicalRecord[] medicalRecordsRetrieved = new ObjectMapper().readValue(contentAsString, MedicalRecord[].class);

        assertEquals(2, medicalRecordsRetrieved.length);
        verify(medicalRecordService, times(1)).findAll();
    }

    @Test
    public void testShouldVerifyThatStatusIsOkAndReturnTheMedicalRecord() throws Exception {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("Boyd")
                .build();
        when(medicalRecordService.findMedicalRecordByFirstnameAndLastname("John", "Boyd")).thenReturn(Optional.of(medicalRecord));

        String url = "/medicalRecords/John/Boyd";
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        MedicalRecord medicalRetrieved = new ObjectMapper().readValue(contentAsString, MedicalRecord.class);

        assertEquals("John", medicalRetrieved.getFirstName());
        verify(medicalRecordService, times(1)).findMedicalRecordByFirstnameAndLastname("John", "Boyd");
    }

    @Test
    public void testShouldVerifyReturningExceptionWhenThereIsNoMedicalRecord() throws Exception {
        when(medicalRecordService.findMedicalRecordByFirstnameAndLastname("John", "Boyd")).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(get("/medicalRecords/John/Boyd"))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(medicalRecordService, times(1)).findMedicalRecordByFirstnameAndLastname("John", "Boyd");
    }
}