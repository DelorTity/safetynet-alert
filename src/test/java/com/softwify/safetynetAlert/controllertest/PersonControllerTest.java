package com.softwify.safetynetAlert.controllertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.exceptions.PersonAlreadyExistsException;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
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
        Person person = Person.builder()
                .firstName("Jack")
                .lastName("Boyd")
                .address("douala")
                .city("limbe")
                .zip(123)
                .phone("34-32")
                .email("anze@gmail.com")
                .build();

        when(personService.savePerson(any())).thenReturn(person);
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
        verify(personService, times(1)).savePerson(any(Person.class));
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

        when(personService.savePerson(any())).thenThrow(PersonAlreadyExistsException.class);

        String content = new ObjectMapper().writeValueAsString(person);
        MockHttpServletRequestBuilder mockRequest = post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(content);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest());

        verify(personService, times(1)).savePerson(any(Person.class));
    }
}
