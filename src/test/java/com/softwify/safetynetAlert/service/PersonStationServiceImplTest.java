package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.dto.*;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.ecception.StationNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.util.DateUtils;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PersonStationServiceImplTest {
    PersonDao personDao = Mockito.mock(PersonDao.class);
    FireStationsDao fireStationDao = Mockito.mock(FireStationsDao.class);
    MedicalRecordDao medicalRecordDao = Mockito.mock(MedicalRecordDao.class);
    PersonStationServiceImpl personStationService = new PersonStationServiceImpl(fireStationDao, personDao, medicalRecordDao);

    @Test
    public void getPersonByStationNumberShouldReturnPersonsNumberOfAdultsAndChildren() throws ParseException {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").address("douala").build(),
                Person.builder().firstName("Tity").lastName("Delor").address("douala").build(),
                Person.builder().firstName("joe").lastName("claire").address("douala").build()
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
        when(medicalRecordDao.findByFirstnameAndLastname("Tity", "Delor")).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordDao.findByFirstnameAndLastname("joe", "claire")).thenReturn(Optional.of(medicalRecord2));

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
        assertThrows(StationNotFoundException.class, () -> personStationService.findPersonByStation(1));
    }

    @Test
    public void getChildByAddressShouldReturnChildrenFoundInAddress() throws ParseException {
        List<Person> personList = Arrays.asList(
                Person.builder().firstName("Delor").lastName("Tity").address("Maroua").build(),
                Person.builder().firstName("Francois").lastName("Komto").address("Maroua").build(),
                Person.builder().firstName("Leo").lastName("Dim").address("Maroua").build()
        );
        String dateString = "09/09/2006";
        Date dateOne = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .birthdate(dateOne)
                .build();
        String dateString1 = "01/07/2011";
        Date dateTwo = new SimpleDateFormat("dd/MM/yyyy").parse(dateString1);
        MedicalRecord medicalRecord1 = MedicalRecord.builder()
                .birthdate(dateTwo)
                .build();

        when(personDao.findByAddress("Maroua")).thenReturn(personList);
        when(medicalRecordDao.findByFirstnameAndLastname("Delor", "Tity")).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordDao.findByFirstnameAndLastname("Francois", "Komto")).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordDao.findByFirstnameAndLastname("Leo", "Dim")).thenReturn(Optional.of(medicalRecord1));

        List<Child> children = personStationService.findPersonByAddress("Maroua");

        assertNotNull(children);
        assertEquals(3, children.size());
        assertEquals(11, children.get(2).getAge());

        verify(personDao, times(1)).findByAddress("Maroua");

    }

    @Test
    public void getPersonByAddressShouldThrowExceptionWhenReturnEmptyList() {
        assertThrows(PersonNotFoundException.class, () -> personStationService.findPersonByAddress("yaounde"));
    }

    @Test
    public void getPhoneNumberByStationNumberShouldReturnPhoneNumbersAtThatStation() {
        List<Person> personList = Arrays.asList(
                Person.builder().firstName("delor").phone("695 62 46 91").build(),
                Person.builder().firstName("tity").phone("677 06 14 06").build(),
                Person.builder().firstName("leticia").phone("651 95 54 71").build()
        );

        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().address("douala").station(3).build(),
                FireStation.builder().address("12-limbe").station(5).build()
        );

        when(fireStationDao.findByStationNumber(3)).thenReturn(fireStations);
        when(personDao.findByAddress("douala")).thenReturn(personList);

        List<String> phoneNumberByStation = personStationService.findPhoneNumberByStation(3);

        assertNotNull(phoneNumberByStation);
        assertEquals(3, personList.size());
        assertEquals("677 06 14 06", personList.get(1).getPhone());

        verify(fireStationDao, times(1)).findByStationNumber(3);
        verify(personDao, times(1)).findByAddress("douala");
    }

    @Test
    public void getPersonsByAddressShouldReturnPersons() throws ParseException {
        List<Person> personList = Arrays.asList(
                Person.builder().firstName("Delor").lastName("Tity").address("Douala").phone("657-876").build(),
                Person.builder().lastName("Komto").address("Douala").phone("777-676").build(),
                Person.builder().lastName("Kouam").address("Douala").build()
        );

        FireStation fireStation = FireStation.builder().address("Douala").station(6).build();

        String date = "16/03/2006";
        Date dateOne = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .medications(Collections.singletonList("para"))
                .allergies(Collections.singletonList("bread"))
                .birthdate(dateOne)
                .build();

        String date1 = "18/06/2010";
        Date dateTwo = new SimpleDateFormat("dd/MM/yyy").parse(date1);
        MedicalRecord medicalRecord1 = MedicalRecord.builder()
                .medications(Collections.singletonList("Efferagant"))
                .allergies(Collections.singletonList("Lime"))
                .birthdate(dateTwo)
                .build();

        when(personDao.findByAddress("Douala")).thenReturn(personList);
        when(fireStationDao.findByAddress("Douala")).thenReturn(Optional.of(fireStation));
        when(medicalRecordDao.findByFirstnameAndLastname("Delor", "Tity")).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordDao.findByFirstnameAndLastname("", "Komto")).thenReturn(Optional.of(medicalRecord1));

        List<PersonFire> personFires = personStationService.findFireStationByAddress("Douala");
        assertNotNull(personFires);
        assertEquals(1, personFires.size());
        assertEquals("657-876", personFires.get(0).getPhone());

        verify(personDao, times(1)).findByAddress("Douala");
        verify(fireStationDao, times(1)).findByAddress("Douala");
    }

    @Test
    public void getPersonsByAddressShouldReturnEmptyList() throws ParseException {
        List<PersonFire> personFires = personStationService.findFireStationByAddress("Douala");
        assertEquals(0, personFires.size());

        verify(personDao, times(1)).findByAddress("Douala");
        verify(fireStationDao, times(1)).findByAddress("Douala");
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
        when(fireStationDao.findByStationNumber(numbers.get(0))).thenReturn(fireStations);
        when(medicalRecordDao.findByFirstnameAndLastname("john", "pierre")).thenReturn(Optional.of(medicalRecord));

        List<FloodStation> floodByStationNumber = personStationService.findFloodByStationNumber(numbers);
        assertEquals(2, floodByStationNumber.size());
        assertEquals("pierre", floodByStationNumber.get(1).getLastname());
        assertEquals("22-56", floodByStationNumber.get(0).getPhone());

        verify(fireStationDao, times(1)).findByStationNumber(numbers.get(0));
    }

    @Test
    public void getPersonByStationNumbersShouldReturnEmptyListWhenIncorrectNumbers() throws ParseException {
        List<Integer> numbers = Arrays.asList(9, 1);
        List<FloodStation> floodByStationNumber = personStationService.findFloodByStationNumber(numbers);

        assertEquals(0, floodByStationNumber.size());
        verify(fireStationDao, times(1)).findByStationNumber(numbers.size());
    }

    @Test
    public void getPersonByFirstNameAndLastNameShouldReturnPersonInfo() throws ParseException {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("luc").lastName("pierre").address("douala").email("el@gmail.com").phone("22-56").build(),
                Person.builder().firstName("john").lastName("pierre").address("douala").email("").phone("22-56").build(),
                Person.builder().firstName("job").lastName("pierre").address("douala").email("del@email.com").build()
        );
        String dateString = "03/12/1999";
        Date date1 = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .birthdate(date1)
                .lastName(persons.get(0).getLastName())
                .medications(Collections.singletonList("paracetamol"))
                .allergies(Collections.singletonList("coffee"))
                .build();

        when(personDao.findPersons(anyString(), anyString())).thenReturn(persons);
        when(medicalRecordDao.findByFirstnameAndLastname(anyString(), anyString())).thenReturn(Optional.ofNullable(medicalRecord));

        List<PersonInfo> personInfoList = personStationService.findPersonByFirstNameAndLastName("", "pierre");
        assertEquals(3, personInfoList.size());
        assertEquals("el@gmail.com", personInfoList.get(0).getEmail());
        assertEquals(23, personInfoList.get(1).getAge());

        verify(personDao, times(1)).findPersons("", "pierre");
    }

    @Test
    public void getPersonByFirstLastNameShouldReturnEmptyList() throws ParseException {
        List<PersonInfo> personInfo = personStationService.findPersonByFirstNameAndLastName("job", "pierre");
        assertEquals(0, personInfo.size());

        verify(personDao, times(1)).findPersons("job", "pierre");
    }

    @Test
    public void getPersonEmailByCityShouldReturnMailList() throws ParseException {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("delor").lastName("tity").city("doul").email("gmail.com").build(),
                Person.builder().firstName("del").lastName("luc").city("doul").email("yahoo.fr").build(),
                Person.builder().firstName("pierre").lastName("tity").city("doul").email("yahoo.com").build()
        );

        when(personDao.findByCity(anyString())).thenReturn(persons);

        List<String> personEmail = personStationService.findBYCity("doul");
        assertEquals(3, personEmail.size());
        assertEquals("yahoo.fr", personEmail.get(1));

        verify(personDao, times(1)).findByCity("doul");
    }

    @Test
    public void getPersonByCityShouldReturnEmptyListWhenWrongCity() throws ParseException {
        List<String> personByCity = personStationService.findBYCity("douala");
        assertEquals(0, personByCity.size());

        verify(personDao, times(1)).findByCity("douala");
    }
}
