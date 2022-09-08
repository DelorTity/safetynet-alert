package com.softwify.safetynetAlert.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.PersonService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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

            Assertions.assertEquals(2, personsRetrieved.length);
        }


        @Test
        public void testShouldVerifyReturningExceptionWhenThereIsNoPerson() throws Exception {
            when(personService.findByFirstnameLastname("John", "Boyd")).thenThrow(PersonNotFoundException.class);

            mockMvc.perform(get("/persons/John/Boyd"))
                    .andExpect(status().isNotFound())
                    .andReturn();

            verify(personService, times(1)).findByFirstnameLastname("John", "Boyd");
        }
}
