package com.softwify.safetynetAlert.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class FireStationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FireStationService fireStationServie;

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndPerssonLengthIsCorrect() throws Exception {
        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().address("1509 Culver St").station(2).build(),
                FireStation.builder().address("12-dla-M").station(3).build()
        );
        when(fireStationServie.getAll()).thenReturn(fireStations);

        MvcResult result = mockMvc.perform(get("/firestation"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        FireStation[] fireStationRetrieved = new ObjectMapper().readValue(contentAsString, FireStation[].class);

        assertEquals(2, fireStationRetrieved.length);
        verify(fireStationServie, times(1)).getAll();
    }

    @Test
    public void testShouldVerifyThatOkIsReturnWhenSave() throws Exception {
        FireStation fireStation = FireStation.builder()
                .address("12-doul")
                .build();

        Optional<FireStation> optionalFireStation = Optional.of(fireStation);

        when(fireStationServie.addedFireStation(any())).thenReturn(optionalFireStation);
        String content = new ObjectMapper().writeValueAsString(fireStation);
        MockHttpServletRequestBuilder mockRequest = post("/firestation")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult result = mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andReturn();

        verify(fireStationServie, times(1)).addedFireStation(any());
    }
}
