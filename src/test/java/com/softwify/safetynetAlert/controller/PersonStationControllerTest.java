package com.softwify.safetynetAlert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.dto.Child;
import com.softwify.safetynetAlert.dto.PersonStarter;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.exceptions.StationNotFoundException;
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

    @Test
    public void testShouldVerfyThatControllerReturnOkStatusWhenExistingStation() throws Exception {
        List<Child> children = Arrays.asList(Child.builder()
                .firstname("liticia")
                .lastname("anze")
                .age(16)
                .build(),
                Child.builder()
                .firstname("liti")
                .lastname("akl")
                .age(6)
                .build()
        );

        when(personStationService.findPersonByAddress("loum")).thenReturn(children);

        MvcResult result = mockMvc.perform(get("/childAlert=loum"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Child childrenRetrieved = new ObjectMapper().readValue(contentAsString, Child.class);

        assertEquals(16, childrenRetrieved.getAge());
        verify(personStationService, times(1)).findPersonByAddress("loum");
    }

    @Test
    public void testShouldVerifyReturningExceptionWhenThereINoPerson() throws Exception {
        when(personStationService.findPersonByAddress("douala")).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(get("/childAlert=douala"))
                .andExpect(status().isNotFound())
                .andReturn();
    }
}
