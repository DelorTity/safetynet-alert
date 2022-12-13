package com.softwify.safetynetAlert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.dto.Child;
import com.softwify.safetynetAlert.dto.FloodStation;
import com.softwify.safetynetAlert.dto.PersonFire;
import com.softwify.safetynetAlert.dto.PersonStarter;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.ecception.StationNotFoundException;
import com.softwify.safetynetAlert.service.PersonStationService;
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
    public void testShouldVerifyThatControllerReturnOkStatusWhenExistingAddress() throws Exception {
        List<Child> children = Arrays.asList(Child.builder()
                .firstname("delor")
                .lastname("tity")
                .age(16)
                .build(),
                Child.builder()
                .firstname("moise")
                .lastname("komto")
                .age(6)
                .build()
        );

        when(personStationService.findPersonByAddress("loum")).thenReturn(children);

        MvcResult result = mockMvc.perform(get("/childAlert?address=loum"))
                .andExpect(status().isOk())
                .andReturn();
        result.getResponse().getContentAsString();

        verify(personStationService, times(1)).findPersonByAddress("loum");
    }

    @Test
    public void testShouldVerifyReturningExceptionWhenThereINoPerson() throws Exception {
        when(personStationService.findPersonByAddress("douala")).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(get("/childAlert?address=douala"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusWhenExistingPhoneNumber() throws Exception {
        List<String> phoneNumbers = Arrays.asList("143-34",
                "4564-67"
        );

        when(personStationService.findPhoneNumberByStation(6)).thenReturn(phoneNumbers);

        MvcResult result = mockMvc.perform(get("/phoneAlert?firestation=6"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();

        assertEquals(21, contentAsString.length());
        verify(personStationService, times(1)).findPhoneNumberByStation(6);
    }

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusPhoneNumber() throws Exception {
        List<PersonFire> persons = Arrays.asList(PersonFire.builder()
                .lastname("Tity")
                        .stationNumber(3)
                .phone("124-653")
                        .medications(Collections.singletonList("aznol:350mg"))
                        .allergies(Collections.singletonList("palu"))
                        .age(13)
                .build(),
                PersonFire.builder()
                .lastname("del")
                .phone("124-3")
                        .stationNumber(6)
                        .medications(Collections.singletonList("aznol:350mg"))
                        .allergies(Collections.singletonList("palu"))
                        .age(19)
                .build()
        );

        when(personStationService.findFireStationByAddress("douala")).thenReturn(persons);

        MvcResult result = mockMvc.perform(get("/fire?address=douala"))
                .andExpect(status().isOk())
                .andReturn();
        result.getResponse().getContentAsString();

        verify(personStationService, times(1)).findFireStationByAddress("douala");
    }

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndFirestations() throws Exception {
        List<FloodStation> floodStations = Arrays.asList(FloodStation.builder()
                .lastname("tity")
                .phone("124-653")
                        .medications(Collections.singletonList("aznol:350mg"))
                        .allergies(Collections.singletonList("palu"))
                        .age(13)
                .build(),
                FloodStation.builder()
                .lastname("tity")
                .phone("124-3")
                        .medications(Collections.singletonList("abendazol:350mg"))
                        .allergies(Collections.singletonList("palu"))
                        .age(19)
                .build()
        );
        List<Integer> stationNumbers = Arrays.asList(3,8);
        when(personStationService.findFloodByStationNumber(stationNumbers)).thenReturn(floodStations);

        MvcResult result = mockMvc.perform(get("/flood/station?stations=3,8"))
                .andExpect(status().isOk())
                .andReturn();
        result.getResponse().getContentAsString();

        verify(personStationService, times(1)).findFloodByStationNumber(stationNumbers);
    }
}
