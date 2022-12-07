package com.softwify.safetynetAlert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.exceptions.FireStationAlreadyExistException;
import com.softwify.safetynetAlert.exceptions.FireStationNotFoundException;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.service.FireStationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {FireStationController.class})
public class FireStationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationService;

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndPerssonLengthIsCorrect() throws Exception {
        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().address("1509 Culver St").station(2).build(),
                FireStation.builder().address("12-dla-M").station(3).build()
        );
        when(fireStationService.getAll()).thenReturn(fireStations);

        MvcResult result = mockMvc.perform(get("/firestations"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        FireStation[] fireStationRetrieved = new ObjectMapper().readValue(contentAsString, FireStation[].class);

        assertEquals(2, fireStationRetrieved.length);
        verify(fireStationService, times(1)).getAll();
    }

    @Test
    public void testShouldVerifyThatOkIsReturnWhenSave() throws Exception {
        FireStation fireStation = FireStation.builder()
                .address("12-doul")
                .build();

        Optional<FireStation> optionalFireStation = Optional.of(fireStation);

        when(fireStationService.addedFireStation(any())).thenReturn(optionalFireStation);
        String content = new ObjectMapper().writeValueAsString(fireStation);
        MockHttpServletRequestBuilder mockRequest = post("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult result = mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andReturn();

        verify(fireStationService, times(1)).addedFireStation(any());
    }

    @Test
    public void testShdVerifyThatBadStatusReturnWhenNotSave() throws Exception {
        FireStation fireStation = FireStation.builder()
                .address("12-doul")
                .build();

        when(fireStationService.addedFireStation(any())).thenThrow(FireStationAlreadyExistException.class);
        String content = new ObjectMapper().writeValueAsString(fireStation);
        MockHttpServletRequestBuilder mockRequest = post("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult result = mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(fireStationService, times(1)).addedFireStation(any());
    }

    @Test
    public void testShouldVerifyThatStatusIsOkAndReturnTheFireStation() throws Exception {
        FireStation fireStation = FireStation.builder()
                .address("12 doul")
                .build();
        when(fireStationService.findFireStationByAddress("12 doul")).thenReturn(Optional.of(fireStation));

        String url = "/firestations/12 doul";
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        FireStation firestationRetrieved = new ObjectMapper().readValue(contentAsString, FireStation.class);

        assertEquals("12 doul", firestationRetrieved.getAddress());
        verify(fireStationService, times(1)).findFireStationByAddress("12 doul");
    }

    @Test
    public void updateReturnIsOKWhenPersonExitsAndHaveBeenModified() throws Exception {
        FireStation fireStation = FireStation.builder()
                .address("143 pk14 sr")
                .station(54)
                .build();

        Optional<FireStation> optionalFireStation = Optional.of(fireStation);

        when(fireStationService.updateFireStation(any(FireStation.class))).thenReturn(optionalFireStation);

        String inputJson = new ObjectMapper().writeValueAsString(fireStation);
        mockMvc.perform(put("/firestations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testShouldVerifyReturningBadStatusWhenNotUpdateFireStation() throws Exception {
        FireStation fireStation = FireStation.builder()
                .address("143 pk14 sr")
                .station(54)
                .build();

        when(fireStationService.updateFireStation(fireStation)).thenThrow(FireStationNotFoundException.class);

        MockHttpServletRequestBuilder mockRequest = put("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deleteTestShouldReturnNoContentWhendeleteSuccessfully() throws Exception {
        FireStation fireStation = FireStation.builder()
                .address("143 pk14 sr")
                .station(54)
                .build();
        when(fireStationService.deleteFireStation("143 pk14 sr")).thenReturn(Optional.of(fireStation));

        mockMvc.perform(delete("/firestations/143 pk14 sr"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteTestShouldReturnNofoundWhenNotdelete() throws Exception {
        when(fireStationService.deleteFireStation(anyString())).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(delete("/firestations/143 pk14 sr"))
                .andExpect(status().isNotFound());
    }
}
