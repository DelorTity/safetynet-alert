package com.softwify.safetynetAlert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.dto.PersonStarter;
import com.softwify.safetynetAlert.dto.PersonStation;
import com.softwify.safetynetAlert.service.PersonStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {PersonStationController.class})
public class PersonStationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonStationService personStationService;

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndPersonLengthIsCorrect() throws Exception {
        List<PersonStation> personStations = Arrays.asList(
                PersonStation.builder().firstname("maria").lastname("anz").address("douala").build(),
                PersonStation.builder().firstname("liti").lastname("anz").address("douala").build()
        );
        when(personStationService.findPersonByStation(6)).thenReturn((PersonStarter) personStations);

        MvcResult result = mockMvc.perform(get("/firestation?stationNumber=6"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        PersonStation[] personStationsRetrieved = new ObjectMapper().readValue(contentAsString, PersonStation[].class);

        assertEquals(2, personStationsRetrieved.length);
        verify(personStationService, times(1)).findPersonByStation(6);
    }
}
