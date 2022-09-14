package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.Person;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonDaoImplTest {
    DataStoreManager dataStoreManager = Mockito.mock(DataStoreManager.class);
    PersonDaoImpl personDao = new PersonDaoImpl(dataStoreManager);

    @Test
    public void getPersonsReturnsExpectedSizeAndPersonFirstName() {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("john").build(),
                Person.builder().firstName("jean").build()
        );
        when(dataStoreManager.getPersons()).thenReturn(persons);

        List<Person> personList = personDao.findAll();
        assertEquals("john", personList.get(0).getFirstName());
        assertEquals(2, personList.size());

        verify(dataStoreManager, times(1)).getPersons();
    }

    @Test
    public void getPersonByFirstnameAndLastnameShouldReturnTrueWhenPersonNotExist() {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").build(),
                Person.builder().firstName("jean").lastName("alfred").build()
        );
        when(dataStoreManager.getPersons()).thenReturn(persons);

        String firstName = "John";
        String lastName = "pierre";
        Optional<Person> person = personDao.findPersonByFirstnameAndLastname(firstName, lastName);
        assertTrue(person.isEmpty());

        verify(dataStoreManager, times(1)).getPersons();
    }

    @Test
    public void getPersonByFirstnameAndLastnameShouldReturnFalseWhenPersonNotExist() {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").build(),
                Person.builder().firstName("jean").lastName("alfred").build()
        );
        when(dataStoreManager.getPersons()).thenReturn(persons);

        Optional<Person> optionalPerson = personDao.findPersonByFirstnameAndLastname("john", "pierre");

        assertFalse(optionalPerson.isEmpty());
        assertEquals("john", optionalPerson.get().getFirstName());
        assertEquals("pierre", optionalPerson.get().getLastName());
        verify(dataStoreManager, times(1)).getPersons();
    }

    @Test
    void addNewPersonIfNotExit() {
        List<Person> personList = Arrays.asList(
                Person.builder().firstName("Delor").build(),
                Person.builder().firstName("Francois").build()
        );
        List<Person> persons = new ArrayList<>(personList);
        when(dataStoreManager.getPersons()).thenReturn(persons);

    Person person1 = Person.builder().firstName("Delor")
            .lastName("Tkf")
            .address("pk14")
            .city("douala")
            .zip(123)
            .phone("12-56-09")
            .email ("anze@gmail.com").build();
    persons.add(person1);
    assertEquals(3,persons.size());
    }

    @Test
    void testUpdateShouldReturnOkIfPersonExitsAndHasBeenUpdated() {
        Person updatedPerson = Person.builder()
                .firstName("Delor")
                .lastName("Tatus")
                .zip(123)
                .build();
        Optional<Person> optionalPerson = Optional.of(updatedPerson);
        Person person = optionalPerson.get();
        when(dataStoreManager.getPersons()).thenReturn(new ArrayList<>(Collections.singleton(updatedPerson)));

        assertTrue(optionalPerson.isPresent());
        personDao.update(optionalPerson.get());
        assertEquals(123, person.getZip());

        verify(dataStoreManager, atLeastOnce()).getPersons();
    }

    @Test
    public void testShouldReturnEmptyIfPersonDoNotExits() {
        Person updatedPerson = Person.builder()
                .firstName("Delor")
                .lastName("Tatus")
                .zip(123)
                .build();
        Optional<Person> person = Optional.of(Person.builder().firstName("Delores").build());
        when(dataStoreManager.getPersons()).thenReturn(new ArrayList<>(Collections.singleton(updatedPerson)));
    }
}