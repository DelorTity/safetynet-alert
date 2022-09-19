package com.softwify.safetynetAlert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.ecception.PersonAlreadyExitException;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.service.MedicalRecordService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        when(medicalRecordService.findByFirstnameAndLastname("John", "Boyd")).thenReturn(Optional.of(medicalRecord));

        String url = "/medicalrecord/John/Boyd";
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        MedicalRecord medicalRetrieved = new ObjectMapper().readValue(contentAsString, MedicalRecord.class);

        assertEquals("John", medicalRetrieved.getFirstName());
        verify(medicalRecordService, times(1)).findByFirstnameAndLastname("John", "Boyd");
    }

    @Test
    public void testShouldVerifyReturningExceptionWhenThereIsNoMedicalRecord() throws Exception {
        when(medicalRecordService.findByFirstnameAndLastname("John", "Boyd")).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(get("/medicalrecord/John/Boyd"))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(medicalRecordService, times(1)).findByFirstnameAndLastname("John", "Boyd");
    }

    @Test
    public void testShouldVerifyThatOkIsReturnWhenSave() throws Exception {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("Boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build();
        when(medicalRecordService.save(any())).thenReturn(Optional.of(medicalRecord));

        String content = new ObjectMapper().writeValueAsString(medicalRecord);
        MockHttpServletRequestBuilder mockRequest = post("/medicalrecord")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult result = mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        MedicalRecord medicalRetrieved = new ObjectMapper().readValue(contentAsString, MedicalRecord.class);

        assertEquals("John", medicalRetrieved.getFirstName());
        assertEquals("Boyd", medicalRetrieved.getLastName());
        verify(medicalRecordService, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    public void testShouldVerifyReturningStatusWhenThereIsNoMedicalRecordToSave() throws Exception {
        MockHttpServletRequestBuilder mockRequest = post("/medicalrecord")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testShouldReturnExceptionWhenTheMedicalRecordToSaveAlreadyExist() throws Exception {
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .firstName("John")
                .lastName("Boyd")
                .medications(Collections.singletonList("aznol:350mg"))
                .build();
        when(medicalRecordService.save(any())).thenThrow(PersonAlreadyExitException.class);

        String content = new ObjectMapper().writeValueAsString(medicalRecord);
        MockHttpServletRequestBuilder mockRequest = post("/medicalrecord")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());

        verify(medicalRecordService, times(1)).save(any(MedicalRecord.class));
    }

    @Test
    public void testShouldReturnNotFoundWhenNotDelete() throws Exception {
        when(medicalRecordService.delete(anyString(), anyString())).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(delete("/medicalrecord/John/Boyd"))
                .andExpect(status().isNotFound());
    }
}