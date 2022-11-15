package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.dto.Child;
import com.softwify.safetynetAlert.dto.PersonStarter;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.ecception.StationNotFoundException;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.model.Person;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

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
}
