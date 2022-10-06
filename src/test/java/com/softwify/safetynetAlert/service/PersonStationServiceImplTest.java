package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationDao;
import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.dto.PersonStarter;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.model.Person;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PersonStationServiceImplTest {
    PersonDao personDao = Mockito.mock(PersonDao.class);
    FireStationDao fireStationDao = Mockito.mock(FireStationDao.class);
    MedicalRecordDao medicalRecordDao = Mockito.mock(MedicalRecordDao.class);
    PersonStationServiceImpl personStationService = new PersonStationServiceImpl(fireStationDao, personDao, medicalRecordDao);

    @Test
    public void getPersonByStationNumberShouldReturnPersonsNumberOfAdultsAndChildren() {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").address("douala").build(),
                Person.builder().firstName("jean").address("bafia").build()
        );
        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().address("douala").station(3).build(),
                FireStation.builder().address("12-limbe").station(3).build(),
                FireStation.builder().address("129-Douala-DR").station(2).build()
        );

        long  date = 12/03/2003;
        Date dateFormat = new java.sql.Date(date);
        MedicalRecord medicalRecord = MedicalRecord.builder()
                .birthdate(dateFormat)
                .lastName("joe").build();
        when(personDao.findByAddress("douala")).thenReturn(persons);
        when(fireStationDao.findByStationNumber(3)).thenReturn(fireStations);
        when(medicalRecordDao.findByFirstnameAndLastname(persons.get(0).getFirstName(), persons.get(0).getLastName())).thenReturn(Optional.of(medicalRecord));

        PersonStarter personByStation = personStationService.findPersonByStation(3);

        assertEquals("john", personByStation.getPersons().get(0).getFirstname());
        assertEquals("douala", personByStation.getNumberOfAdults());

        verify(personDao, times(1)).findByAddress("douala");
        verify(fireStationDao, times(1)).findByStationNumber(3);
    }

    @Test
    public void getPersonByStationNumberShouldReturnEmptyList() {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("john").address("douala").build(),
                Person.builder().firstName("jean").address("bafia").build()
        );
        List<FireStation> fireStations = Arrays.asList(
                FireStation.builder().address("douala").station(3).build(),
                FireStation.builder().address("12-limbe").station(3).build(),
                FireStation.builder().address("129-Douala-DR").station(2).build()
        );
        when(personDao.findByAddress("kumba")).thenReturn(persons);
        when(fireStationDao.findByStationNumber(3)).thenReturn(fireStations);

        PersonStarter personByStation = personStationService.findPersonByStation(3);
        assertEquals(10, personByStation.getNumberOfAdults());
        verify(fireStationDao, times(1)).findByStationNumber(3);
    }
}
