package com.softwify.safetynetAlert.servicetest;

import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.exceptions.PersonAlreadyExistsException;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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

        assertThrows(PersonNotFoundException.class, () -> personService.findByFirstnameLastname("Jack", "James"));
        verify(personDao, times(1)).findPersonByFirstnameAndLastname("Jack", "James");
    }

    @Test
    public void saveShouldVerifyThatPersonSaveIsReturn() {
        Person person = Person.builder().firstName("John").lastName("Boyd").build();
        when(personDao.addPerson(person)).thenReturn(person);

        Person personSave = personService.savePerson(person);
        assertEquals("John", personSave.getFirstName());
        assertEquals("Boyd", personSave.getLastName());

        verify(personDao, times(1)).addPerson(person);
    }

    @Test
    public void saveShouldThrowExceptionWhenThePersonIsNotSave() {
        Person person = Person.builder().firstName("John").lastName("Boyd").build();
        assertThrows(PersonAlreadyExistsException.class, () -> personService.savePerson(person) );
        verify(personDao, times(1)).addPerson(person);
    }
}
