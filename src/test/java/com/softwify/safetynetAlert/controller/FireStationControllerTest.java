package com.softwify.safetynetAlert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.ecception.FireStationNotFoundException;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
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
import static org.mockito.ArgumentMatchers.any;
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

    @Test
    public void testShouldVerifyThatOkIsReturnWhenSave() throws Exception {
        FireStation fireStation = FireStation.builder()
                .address("12-Nkam")
                .build();

        Optional<FireStation> optionalFireStation = Optional.of(fireStation);

        when(fireStationService.addFireStation(any())).thenReturn(optionalFireStation);
        String content = new ObjectMapper().writeValueAsString(fireStation);
        MockHttpServletRequestBuilder mockRequest = post("/firestations")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andReturn();

        verify(fireStationService, times(1)).addFireStation(any());
    }

    @Test
    public void testShouldVerifyThatStatusIsOkAndReturnTheFireStation() throws Exception {
        FireStation fireStation = FireStation.builder()
                .address("12 Douala")
                .build();
        when(fireStationService.findFireStationByAddress("12 Douala")).thenReturn(Optional.of(fireStation));

        String url = "/firestations/12 Douala";
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        FireStation fireStationRetrieved = new ObjectMapper().readValue(contentAsString, FireStation.class);

        assertEquals("12 Douala", fireStationRetrieved.getAddress());
        verify(fireStationService, times(1)).findFireStationByAddress("12 Douala");
    }

    @Test
    public void updateReturnIsOKWhenPersonExitsAndHaveBeenModified() throws Exception {
        FireStation fireStation = FireStation.builder()
                .address("143 pk14 sr")
                .station(54)
                .build();

        Optional<FireStation> optionalFireStation = Optional.of(fireStation);

        when(fireStationService.updateFireStation(any(FireStation.class))).thenReturn(optionalFireStation);
        assertTrue(optionalFireStation.isPresent());

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
                .address("13 koulouloun sr")
                .station(54)
                .build();
        when(fireStationService.deleteFireStation("13 koulouloun sr")).thenReturn(Optional.of(fireStation));

        mockMvc.perform(delete("/firestations/13 koulouloun sr"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteTestShouldReturnNofoundWhenNotdelete() throws Exception {
        when(fireStationService.deleteFireStation(anyString())).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(delete("/firestations/143 pk14 sr"))
                .andExpect(status().isNotFound());
    }

}