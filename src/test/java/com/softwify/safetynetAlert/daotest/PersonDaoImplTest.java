package com.softwify.safetynetAlert.daotest;

import com.softwify.safetynetAlert.dao.DataStoreManager;
import com.softwify.safetynetAlert.dao.PersonDaoImpl;
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
    public void testShouldVerifyThatPersonSizeAddWhenSaveCorrect() {
        List<Person> arrayPersons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").build(),
                Person.builder().firstName("jean").lastName("alfred").build()
        );
        List<Person> persons = new ArrayList<>(arrayPersons);

        when(dataStoreManager.getPersons()).thenReturn(persons);

        Person person = Person.builder().build();

        personDao.addPerson(person);

        assertEquals(3, persons.size());
    }

    @Test
    void testUpdateShouldReturnOkIfPersonExitsAndHasBeenUpdated() {
        Person updatedPerson = Person.builder()
                .firstName("John")
                .lastName("Boyd")
                .zip(123)
                .build();
        when(dataStoreManager.getPersons()).thenReturn(new ArrayList<>(Collections.singleton(updatedPerson)));

        Person person = Person.builder()
                .firstName("John")
                .lastName("Boyd")
                .zip(345)
                .email("anze@gmail.com")
                .build();
        Optional<Person> optionalPerson = personDao.update(person);
        assertFalse(optionalPerson.isEmpty());

        assertEquals(345, optionalPerson.get().getZip());
        assertEquals("anze@gmail.com", optionalPerson.get().getEmail());

        verify(dataStoreManager, atLeastOnce()).getPersons();
    }

    @Test
    void testShouldStopWhenThereIsNoBodyToUpdate() {
        Person updatedPerson = Person.builder()
                .firstName("John")
                .lastName("Boyd")
                .zip(123)
                .build();
        when(dataStoreManager.getPersons()).thenReturn(new ArrayList<>(Collections.singleton(updatedPerson)));

        Person person = Person.builder()
                .firstName("John")
                .lastName("Boydor")
                .zip(345)
                .email("anze@gmail.com")
                .build();
        Optional<Person> optionalPerson = personDao.update(person);
        assertTrue(optionalPerson.isEmpty());
    }

    @Test
    void deletePersonTest() {
        List<Person> arrayPersons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").build(),
                Person.builder().firstName("jean").lastName("alfred").build()
        );
        List<Person> personList = new ArrayList<>(arrayPersons);
        when(dataStoreManager.getPersons()).thenReturn(personList);

        Optional<Person> update = personDao.delete("john", "pierre");
        assertEquals("john", update.get().getFirstName());
        verify(dataStoreManager, atLeastOnce()).getPersons();
    }

    @Test
    void deleteShouldStopWhenNoPersonInTheList() {
        List<Person> arrayPersons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").build(),
                Person.builder().firstName("jean").lastName("alfred").build()
        );
        List<Person> personList = new ArrayList<>(arrayPersons);
        when(dataStoreManager.getPersons()).thenReturn(personList);

        Optional<Person> update = personDao.delete("job", "pierre");
        assertTrue(update.isEmpty());
        verify(dataStoreManager, atLeastOnce()).getPersons();
    }

}
