package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.ecception.PersonAlreadyExitException;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.Person;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PersonServiceImplTest {
    PersonDao personDao = Mockito.mock(PersonDao.class);
    PersonServiceImpl personService = new PersonServiceImpl(personDao);
    @Test
    public void testShouldVerifyTheNumberOfTimeThatPersonDaoIsCallIntoPersonService() {
        personService.findAll();
        verify(personDao, times(1)).findAll();
    }

    @Test
    public void getPersonByFirstnameAndLastnameShouldReturnThePerson() {
        String firstName = "John";
        String lastName = "Boyd";
        Person person = Person.builder().firstName("John").lastName("Boyd").build();
        Optional<Person> optionalPerson = Optional.of(person);
        when(personDao.findPersonByFirstnameAndLastname(firstName, lastName)).thenReturn(optionalPerson);

        Person personRetrieved = personService.findByFirstnameLastname(firstName, lastName);
        assertNotNull(personRetrieved);
        assertEquals("John", personRetrieved.getFirstName());
        assertEquals("Boyd", personRetrieved.getLastName());

        verify(personDao, times(1)).findPersonByFirstnameAndLastname(firstName, lastName);
    }


    @Test
    public void getPersonByFirstnameAndLastnameShouldThrowExceptionWhenThereIsNoPerson() {
        when(personDao.findPersonByFirstnameAndLastname(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.findByFirstnameLastname("Black", "James"));
        verify(personDao, times(1)).findPersonByFirstnameAndLastname("Black", "James");
    }

    @Test
    void saveIfDoesNotExit() {
        Person person = Person.builder().firstName("John").lastName("Boyd").build();
        when(personDao.addNewPerson(person)).thenReturn(person);

        Person save = personService.save(person);
        assertEquals("John", save.getFirstName());
        assertEquals("Boyd", save.getLastName());
        verify(personDao, times(1)).addNewPerson(person);
    }

    @Test
    void doNotSaveIfPersonExit() {
        Person person = Person.builder().firstName("Delor").build();
        assertThrows(PersonAlreadyExitException.class, () -> personService.save(person));
        verify(personDao, times(1)).addNewPerson(person);
    }
}