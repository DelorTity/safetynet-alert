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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

    @Test
    public void updateReturnIsOKWhenPersonExitsAndHaveBeenModified() throws Exception {
        Optional<Person> optionalPersonSave = Optional.of(Person.builder()
                .firstName("Delor")
                .lastName("Tity")
                .email("ram@gmail.com")
                .build());

        Optional<Person> optionalPersonUpdate = Optional.of(Person.builder()
                .firstName("Delor")
                .lastName("Tity")
                .email("delor@gmail.com")
                .build());
        Person personSave = optionalPersonSave.get();
        Person personUpdate = optionalPersonUpdate.get();

        when(personService.findByFirstnameLastname(anyString(), anyString())).thenReturn(personSave);
        when(personService.update(any(Person.class))).thenReturn(optionalPersonUpdate);
        assertTrue(optionalPersonUpdate.isPresent());

        String inputJson = new ObjectMapper().writeValueAsString(personUpdate);

        mockMvc.perform(put("/persons")
                .contentType(MediaType.APPLICATION_JSON)
                .content(inputJson))
                .andExpect(status().isOk())
                .andDo(print());
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
    public void deleteTestShouldReturnNoContentWhenDeleteSuccessfully() throws Exception{
        Person person = Person.builder()
                .firstName("John")
                .lastName("Boyd")
                .address("douala")
                .build();
        Optional<Person> optionalPerson = Optional.of(person);
        when(personService.delete("John", "Boyd")).thenReturn(optionalPerson);

        mockMvc.perform(delete("/persons/John/Boyd"))
                .andExpect(status().isNoContent());
    }

    @Test
    public void deleteTestShouldReturnNoFoundWhenNotDelete() throws Exception{
        when(personService.delete(anyString(), anyString())).thenThrow(PersonNotFoundException.class);

        mockMvc.perform(delete("/persons/John/Boyd"))
                .andExpect(status().isNotFound())
                .andDo(print());
                .andExpect(status().isNotFound());

    }
}
