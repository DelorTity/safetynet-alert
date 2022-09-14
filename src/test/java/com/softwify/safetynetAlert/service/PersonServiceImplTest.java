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
    void saveIfPersonDoesNotExit() {
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

    @Test
    void updateIfPersonExits() {
        Optional<Person> person = Optional.of(Person.builder().firstName("Delor").lastName("Tatus").zip(345).build());

        when(personDao.update(person.get())).thenReturn(person);
        personService.update(person.get());
        person.get().setZip(656);
        assertEquals(656, person.get().getZip());
    }

    @Test
    public void testShouldThrowExceptionWhenThePersonIsNotUpdate() {
        Person person = Person.builder().firstName("John").lastName("Boyd").build();
        assertThrows(PersonNotFoundException.class, () -> personService.update(person) );
        verify(personDao, times(1)).update(person);
    }

    @Test
    public void testShouldCheckThatDeleteProvidedPerson() {
        Person person = Person.builder()
                .firstName("John")
                .lastName("pierre")
                .build();
        when(personDao.delete(anyString(), anyString())).thenReturn(Optional.of(person));

        personService.delete("John", "pierre");
        verify(personDao, times(1)).delete(anyString(), anyString());
    }

    @Test
    public void testShouldThrowExceptionWhenThePersonIsNotDelete() {
        assertThrows(PersonNotFoundException.class, () -> personService.delete("joe", "ben") );
        verify(personDao, times(1)).delete("joe", "ben");
    }
}