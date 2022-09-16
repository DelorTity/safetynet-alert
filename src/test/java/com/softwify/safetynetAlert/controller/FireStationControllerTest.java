package com.softwify.safetynetAlert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FireStationController.class})
class FireStationControllerTest {
        @Autowired
        private MockMvc mockMvc;

        @MockBean
        private FireStationService fireStationService;

        @Test
        public void testShouldVerifyThatControllerStatusReturnOkAndFireStationSizeIsCorrect() throws Exception {
            List<FireStation> fireStations = Arrays.asList(
                    FireStation.builder().station(343).build(),
                    FireStation.builder().station(565).build()
            );
            when(fireStationService.findAll()).thenReturn(fireStations);

            MvcResult result = mockMvc.perform(get("/firestations"))
                    .andExpect(status().isOk())
                    .andReturn();
            String contentAsString = result.getResponse().getContentAsString();
            FireStation[] fireStationRetrieved = new ObjectMapper().readValue(contentAsString, FireStation[].class);

            assertEquals(2, fireStationRetrieved.length);
        }


    }