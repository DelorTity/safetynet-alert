package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.ecception.PersonAlreadyExitException;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.model.Person;
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
        Optional<Person> optionalPerson = Optional.of(Person.builder().firstName("John").lastName("Boyd").build());
        Person person = optionalPerson.get();
        when(personDao.addNewPerson(person)).thenReturn(Optional.of(person));

        Optional<Person> savePerson = personService.save(person);

        assertTrue(savePerson.isPresent());
        assertEquals("John", savePerson.get().getFirstName());
        assertEquals("Boyd", savePerson.get().getLastName());

        verify(personDao, times(1)).addNewPerson(person);
    }

    @Test
    public void saveShouldThrowExceptionWhenThePersonIsNotSave() {
        Person person = Person.builder().firstName("John").lastName("Boyd").build();
        assertThrows(PersonAlreadyExitException.class, () -> personService.save(person) );
        verify(personDao, times(1)).addNewPerson(person);
    }

    @Test
    void testShouldVerifyThatReturnUpdatePerson() {
        Person person = Person.builder()
                .firstName("John")
                .lastName("Peppa")
                .zip(345).build();


        when(personDao.update(person)).thenReturn(Optional.of(person));
        Optional<Person> updatePerson = personService.update(person);
        assertTrue(updatePerson.isPresent());
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

        Optional<Person> deletedPerson = personService.delete("John", "pierre");

        assertTrue(deletedPerson.isPresent());
        assertEquals("John", deletedPerson.get().getFirstName());
        verify(personDao, times(1)).delete("John", "pierre");
    }

    @Test
    public void testShouldThrowExceptionWhenThePersonIsNotDelete() {
        assertThrows(PersonNotFoundException.class, () -> personService.delete("joe", "ben") );
        verify(personDao, times(1)).delete("joe", "ben");
    }
}