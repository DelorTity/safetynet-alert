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
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

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
                Person.builder().firstName("Yvan").build(),
                Person.builder().firstName("Pierre").build()
        );
        when(personService.findAll()).thenReturn(persons);

        MvcResult result = mockMvc.perform(get("/persons"))
                .andExpect(status().isOk())
                .andReturn();
        String contentAsString = result.getResponse().getContentAsString();
        Person[] personsRetrieved = new ObjectMapper().readValue(contentAsString, Person[].class);

        assertEquals(2, personsRetrieved.length);
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
    void saveIfPersonDoNotExit() throws Exception {
        Person person = Person.builder()
                .firstName("Delor")
                .lastName("Tity")
                .build();

        when(personService.save(person)).thenReturn(person);

        String content = new ObjectMapper().writeValueAsString(person);
        this.mockMvc
                .perform(post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(MockMvcResultMatchers.status().isOk());

        assertEquals("Delor", person.getFirstName());
        assertEquals("Tity", person.getLastName());
    }

    @Test
    public void savePersonReturnBadRequestWhenPersonExit() throws Exception {
        Person person = Person.builder()
                .firstName("Delor")
                .lastName("Tity")
                .build();
        when(personService.save(any(Person.class))).thenThrow(PersonAlreadyExitException.class);

        String content = new ObjectMapper().writeValueAsString(person);
        mockMvc
                .perform(post("/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(content))
                .andExpect(status().isBadRequest())
                .andReturn();

        verify(personService, times(1)).save(any(Person.class));
    }

    @Test
    public void savePersonIsNull() throws Exception {
        MockHttpServletRequestBuilder mockRequest = post("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON);
        mockMvc.perform(mockRequest)
                .andExpect(status().isBadRequest())
                .andReturn();
    }
}
