package com.softwify.safetynetAlert.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.ecception.PersonAlreadyExitException;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.PersonService;
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

@WebMvcTest(controllers = {PersonController.class})
public class PersonControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PersonService personService;

    @Test
    public void testShouldVerifyThatControllerReturnOkStatusAndPerssonLengthIsCorrect() throws Exception {
        List<Person> persons = Arrays.asList(
                Person.builder().build(),
                Person.builder().firstName("jean").build()
        );
        when(personService.findAll()).thenReturn(persons);

        MvcResult result = mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Person[] personsRetrieved = new ObjectMapper().readValue(contentAsString, Person[].class);

        assertEquals(2, personsRetrieved.length);
        verify(personService, times(1)).findAll();
    }

    @Test
    public void testShouldVerifyThatStatusIsOkAndReturnThePerson() throws Exception {
        Person person = Person.builder()
                .firstName("John")
                .lastName("Boyd")
                .build();
        when(personService.findByFirstnameLastname("John", "Boyd")).thenReturn(person);

        String url = "/persons/John/Boyd";
        MvcResult result = mockMvc.perform(get(url))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Person personRetrieved = new ObjectMapper().readValue(contentAsString, Person.class);

        assertEquals("John", personRetrieved.getFirstName());
        verify(personService, times(1)).findByFirstnameLastname("John", "Boyd");
    }

    @Test
    public void testShouldVerifyReturningExceptionWhenThereIsNoPerson() throws Exception {
        when(personService.findByFirstnameLastname("John", "Boyd")).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(get("/persons/John/Boyd"))
                .andExpect(status().isNotFound())
                .andReturn();

        verify(personService, times(1)).findByFirstnameLastname("John", "Boyd");
    }

    @Test
    public void testShouldVerifyThatOkIsReturnWhenSave() throws Exception {
        Optional<Person> person = Optional.ofNullable(Person.builder()
                .firstName("Jack")
                .lastName("Boyd")
                .address("douala")
                .city("limbe")
                .zip(123)
                .phone("34-32")
                .email("anze@gmail.com")
                .build());

        when(personService.save(any())).thenReturn(person);
        String content = new ObjectMapper().writeValueAsString(person);
        MockHttpServletRequestBuilder mockRequest = post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        MvcResult result = mockMvc.perform(mockRequest)
                .andExpect(status().isOk())
                .andReturn();

        String contentAsString = result.getResponse().getContentAsString();
        Person personRetrieved = new ObjectMapper().readValue(contentAsString, Person.class);

        assertEquals("Jack", personRetrieved.getFirstName());
        assertEquals("Boyd", personRetrieved.getLastName());
        verify(personService, times(1)).save(any(Person.class));
    }

    @Test
    public void testShouldVerifyReturningStatusWhenThereIsNoPersonToSave() throws Exception {
        MockHttpServletRequestBuilder mockRequest = post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void testShouldReturnExceptionWhenThePersonToSaveAlreadyExist() throws Exception {
        Person person = Person.builder()
                .firstName("John")
                .lastName("Boyd")
                .address("douala")
                .city("limbe")
                .zip(123)
                .phone("34-32")
                .email("anze@gmail.com")
                .build();

        when(personService.save(any())).thenThrow(PersonAlreadyExitException.class);

        String content = new ObjectMapper().writeValueAsString(person);
        MockHttpServletRequestBuilder mockRequest = post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());

        verify(personService, times(1)).save(any(Person.class));
    }

    @Test
    public void updateReturnIsOKWhenPersonExitsAndHaveBeenModified() throws Exception {
        Optional<Person> optionalPersonUpdate = Optional.of(Person.builder()
                .firstName("Jojo")
                .lastName("Lola")
                .email("anz@gmail.com")
                .build());
        Person personUpdate = optionalPersonUpdate.get();

        when(personService.update(any(Person.class))).thenReturn(optionalPersonUpdate);
        assertTrue(optionalPersonUpdate.isPresent());

        String inputJson = new ObjectMapper().writeValueAsString(personUpdate);

        mockMvc.perform(put("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(inputJson))
                .andExpect(status().isOk());
    }

    @Test
    public void testShouldVerifyReturningStatusWhenNotUpdatePerson() throws Exception {
        Optional<Person> optionalPersonUpdate = Optional.of(Person.builder().build());
        when(personService.update(optionalPersonUpdate.get())).thenThrow(PersonNotFoundException.class);

        MockHttpServletRequestBuilder mockRequest = put("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    public void deleteTestShouldReturnNoContentWhendeleteSuccessfully() throws Exception {
        Optional<Person> optionalPerson = Optional.of(Person.builder()
                .firstName("John")
                .lastName("Boyd")
                .address("douala")
                .build());
        when(personService.delete("John", "BOyd")).thenReturn(optionalPerson);

        mockMvc.perform(delete("/persons/John/Boyd"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteTestShouldReturnNofoundWhenNotdelete() throws Exception {
        when(personService.delete(anyString(), anyString())).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(delete("/persons/John/Boyd"))
                .andExpect(status().isNotFound());
    }
}