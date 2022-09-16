package com.softwify.safetynetAlert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {MedicalRecordController.class})
public class MedicalRecordControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MedicalRecordService medicalRecordService;

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndPerssonLengthIsCorrect() throws Exception {
        List<MedicalRecord> medicalRecords = Arrays.asList(MedicalRecord.builder()
                .firstName("john")
                .lastName("boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build(),
                MedicalRecord.builder()
                .firstName("pierre")
                .lastName("boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build()
        );
        when(medicalRecordService.findAll()).thenReturn(medicalRecords);

        MvcResult result = mockMvc.perform(get("/medicalRecords"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        MedicalRecord[] fireStationRetrieved = new ObjectMapper().readValue(contentAsString, MedicalRecord[].class);

        assertEquals(2, fireStationRetrieved.length);
        verify(medicalRecordService, times(1)).findAll();
    }
}
