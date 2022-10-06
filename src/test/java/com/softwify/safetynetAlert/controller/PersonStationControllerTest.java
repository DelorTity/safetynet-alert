package com.softwify.safetynetAlert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.dto.PersonStarter;
import com.softwify.safetynetAlert.exceptions.StationNotFoundException;
import com.softwify.safetynetAlert.service.PersonStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

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
    public void testShouldVerifyThatControllerReturnOkStatusWhenExistingStation() throws Exception {
        PersonStarter personStarter = PersonStarter.builder().numberOfAdults(2).build();

        when(personStationService.findPersonByStation(6)).thenReturn(personStarter);

        MvcResult result = mockMvc.perform(get("/firestation?stationNumber=6"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        PersonStarter personStarterRetrieved = new ObjectMapper().readValue(contentAsString, PersonStarter.class);

        assertEquals(2, personStarterRetrieved.getNumberOfAdults());
        verify(personStationService, times(1)).findPersonByStation(6);
    }

    @Test
    public void testShouldVerifyReturningExceptionWhenThereIsNoStation() throws Exception {
        when(personStationService.findPersonByStation(6)).thenThrow(StationNotFoundException.class);

        mockMvc.perform(get("/firestation?stationNumber=6"))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(personStationService, times(1)).findPersonByStation(6);
    }
}
