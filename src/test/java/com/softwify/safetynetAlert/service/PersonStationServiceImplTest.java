package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationDao;
import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.dto.*;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.exceptions.StationNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.model.Person;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonStationServiceImplTest {
    PersonDao personDao = Mockito.mock(PersonDao.class);
    FireStationDao fireStationDao = Mockito.mock(FireStationDao.class);
    MedicalRecordDao medicalRecordDao = Mockito.mock(MedicalRecordDao.class);
    PersonStationServiceImpl personStationService = new PersonStationServiceImpl(fireStationDao, personDao, medicalRecordDao);

    @Test
    public void getPersonByStationNumberShouldReturnPersonsNumberOfAdultsAndChildren() throws ParseException {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").address("douala").build(),
                Person.builder().firstName("liticia").lastName("kouam").address("douala").build(),
                Person.builder().firstName("job").lastName("ben").address("douala").build()
        );
        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().address("douala").station(3).build(),
                FireStation.builder().address("12-limbe").station(3).build()
        );

        String dateString = "03/12/2003";
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .birthdate(date1)
                .build();

        String dateString1 = "31/12/2013";
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(dateString1);
       MedicalRecord medicalRecord2 = MedicalRecord.builder()
                .birthdate(date2)
                .build();

        when(personDao.findByAddress("douala")).thenReturn(persons);
        when(fireStationDao.findByStationNumber(3)).thenReturn(fireStations);
        when(medicalRecordDao.findByFirstnameAndLastname("john", "pierre")).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordDao.findByFirstnameAndLastname("liticia", "kouam")).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordDao.findByFirstnameAndLastname("job", "ben")).thenReturn(Optional.of(medicalRecord2));

        PersonStarter personByStation = personStationService.findPersonByStation(3);

        assertNotNull(personByStation);
        assertEquals(3, personByStation.getPersons().size());
        assertEquals("john", personByStation.getPersons().get(0).getFirstname());
        assertEquals("pierre", personByStation.getPersons().get(0).getLastname());
        assertEquals(2, personByStation.getNumberOfAdults());
        assertEquals(1, personByStation.getNumberOfChildren());

        verify(personDao, times(1)).findByAddress("douala");
        verify(fireStationDao, times(1)).findByStationNumber(3);
    }

    @Test
    public void getPersonByStationNumberShouldThrowExceptionWhenReturnEmptyList() {
        assertThrows(StationNotFoundException.class, () -> {
            personStationService.findPersonByStation(1);
        });
    }

    @Test
    public void getPesonByAddressShouldReturnChildren() throws ParseException {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").address("douala").build(),
                Person.builder().firstName("liticia").lastName("kouam").address("douala").build(),
                Person.builder().firstName("job").lastName("ben").address("douala").build()
        );

        String dateString = "03/12/2003";
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .birthdate(date1)
                .build();

        String dateString1 = "01/01/2013";
        Date date2 = new SimpleDateFormat("dd/MM/yyyy").parse(dateString1);
        MedicalRecord medicalRecord2 = MedicalRecord.builder()
                .birthdate(date2)
                .build();

        when(personDao.findByAddress("douala")).thenReturn(persons);
        when(medicalRecordDao.findByFirstnameAndLastname("john", "pierre")).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordDao.findByFirstnameAndLastname("liticia", "kouam")).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordDao.findByFirstnameAndLastname("job", "ben")).thenReturn(Optional.of(medicalRecord2));

        List<Child> childByAddress = personStationService.findPersonByAddress("douala");

        assertNotNull(childByAddress);
        assertEquals(1, childByAddress.size());
        assertEquals(9, childByAddress.get(0).getAge());

        verify(personDao, times(1)).findByAddress("douala");
    }

    @Test
    public void getPersonByAddressShouldThrowExceptionWhenReturnEmptyList() {
        assertThrows(PersonNotFoundException.class, () -> {
            personStationService.findPersonByAddress("yaounde");
        });
    }

    @Test
    public void getFirestationByStationNumberShouldReturnPhoneNumber() throws ParseException {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").address("douala").phone("23-997").build(),
                Person.builder().firstName("liticia").lastName("kouam").address("bafang").build(),
                Person.builder().firstName("job").lastName("ben").address("yde").phone("111-03").build()
        );

        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().address("douala").station(3).build(),
                FireStation.builder().address("12-limbe").station(8).build()
        );

        when(fireStationDao.findByStationNumber(3)).thenReturn(fireStations);
        when(personDao.findByAddress("douala")).thenReturn(persons);

        List<String> phoneNumberByStation = personStationService.findPhoneNumberByStation(3);

        assertNotNull(phoneNumberByStation);
        assertEquals(3, phoneNumberByStation.size());
        assertEquals("23-997", phoneNumberByStation.get(0));
        assertEquals("111-03", phoneNumberByStation.get(2));

        verify(personDao, times(1)).findByAddress("douala");
        verify(fireStationDao, times(1)).findByStationNumber(3);
    }

    @Test
    public void getPersonByAddressShouldReturnPersons() throws ParseException {
        List<Person> persons = Arrays.asList(
                Person.builder().lastName("pierre").address("douala").phone("22-56").build(),
                Person.builder().firstName("john").lastName("pierre").address("douala").phone("22-56").build(),
                Person.builder().firstName("job").lastName("ben").address("douala").build()
        );
        FireStation fireStation = FireStation.builder().address("douala").station(3).build();

        String dateString = "03/12/2003";
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .birthdate(date1)
                .build();

        when(personDao.findByAddress("douala")).thenReturn(persons);
        when(fireStationDao.findByAddress("douala")).thenReturn(Optional.of(fireStation));
        when(medicalRecordDao.findByFirstnameAndLastname("john", "pierre")).thenReturn(Optional.of(medicalRecord));

        List<PersonFire> personFireByAddress = personStationService.findPersonFireByAddress("douala");
        assertNotNull(personFireByAddress);
        assertEquals(1, personFireByAddress.size());
        assertEquals(3, personFireByAddress.get(0).getStationNumber());
        assertEquals("pierre", personFireByAddress.get(0).getLastname());
        assertEquals("22-56", personFireByAddress.get(0).getPhone());

        verify(personDao, times(1)).findByAddress("douala");
        verify(fireStationDao, times(1)).findByAddress("douala");
    }

    @Test
    public void getPersonByAddressShouldReturnEmptyList() throws ParseException {
        List<PersonFire> personFireByAddress = personStationService.findPersonFireByAddress("douala");
        assertEquals(0, personFireByAddress.size());

        verify(personDao, times(1)).findByAddress("douala");
        verify(fireStationDao, times(1)).findByAddress("douala");
    }

   @Test
    public void getPersonByStationNumbersShouldReturnInformations() throws ParseException {
        List<Person> persons = Arrays.asList(
                Person.builder().lastName("pierre").address("douala").phone("22-56").build(),
                Person.builder().firstName("john").lastName("pierre").address("douala").phone("22-56").build(),
                Person.builder().firstName("john").lastName("pierre").address("douala").build()
        );
        List<FireStation> fireStations = Arrays.asList(
               FireStation.builder().address("douala").station(3).build(),
               FireStation.builder().address("12-limbe").station(8).build(),
               FireStation.builder().address("12-limbe").station(2).build()
        );
        List<Integer> numbers = Arrays.asList(3, 8);

        String dateString = "03/12/2003";
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .birthdate(date1)
                .lastName(persons.get(0).getLastName())
                .build();

        when(personDao.findByAddress("douala")).thenReturn(persons);
        when(fireStationDao.findByStationNumbers(numbers)).thenReturn(fireStations);
        when(medicalRecordDao.findByFirstnameAndLastname("john", "pierre")).thenReturn(Optional.of(medicalRecord));

        List<FloodStation> floodByStationNumber = personStationService.findFloodByStationNumber(numbers);
        assertEquals(2, floodByStationNumber.size());
        assertEquals("pierre", floodByStationNumber.get(1).getLastname());
        assertEquals("22-56", floodByStationNumber.get(0).getPhone());

        verify(fireStationDao, times(1)).findByStationNumbers(numbers);
    }

    @Test
    public void getPersonByStationNumbersShouldReturnEmptyListWhenIncorrectNumbers() throws ParseException {
        List<Integer> numbers = Arrays.asList(9, 1);
        List<FloodStation> floodByStationNumber = personStationService.findFloodByStationNumber(numbers);

        assertEquals(0, floodByStationNumber.size());
        verify(fireStationDao, times(1)).findByStationNumbers(numbers);
    }

    @Test
    public void getPersonByFirstLastNameShouldReturnPersonsInformations() throws ParseException {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").address("douala").phone("22-56").build(),
                Person.builder().firstName("job").lastName("pierre").address("douala").build()
        );

        String dateString = "03/12/1999";
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .birthdate(date1)
                .build();

        when(personDao.findPersons("john", "pierre")).thenReturn(persons);
        when(medicalRecordDao.findByFirstnameAndLastname(anyString(), anyString())).thenReturn(Optional.of(medicalRecord));

        List<PersonInfo> personInfos = personStationService.findPersonByFirstAndLastName("john", "pierre");

        assertEquals(2, personInfos.size());
        assertEquals(22, personInfos.get(0).getAge());
        assertEquals("douala", personInfos.get(1).getAddress());

        verify(personDao, times(1)).findPersons("john", "pierre");
    }

    @Test
    public void getPersonByFirstLastNameShouldReturnEmptyList() throws ParseException {
        List<PersonInfo> personInfos = personStationService.findPersonByFirstAndLastName("job", "pierre");
        assertEquals(0, personInfos.size());

        verify(personDao, times(1)).findPersons("job", "pierre");
    }

    @Test
    public void getPersonByCityShouldReturnPersonsEmails() throws ParseException {
        List<Person> persons = Arrays.asList(
                Person.builder().city("limbe").email("ab@email.com").build(),
                Person.builder().city("limbe").email("b@email.com").build(),
                Person.builder().city("limbe").email("acb@email.com").build()
        );

        when(personDao.findByCity(anyString())).thenReturn(persons);
        List<String> personByCity = personStationService.findPersonByCity("limbe");
        assertEquals(3, personByCity.size());
        assertEquals("b@email.com", personByCity.get(1));
        assertEquals("acb@email.com", personByCity.get(2));

        verify(personDao, times(1)).findByCity("limbe");
    }

    @Test
    public void getPersonByCityShouldReturnEmptyListWhenWrongCity() throws ParseException {
        List<String> personByCity = personStationService.findPersonByCity("bamenda");
        assertEquals(0, personByCity.size());

        verify(personDao, times(1)).findByCity("bamenda");
    }
}
