package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.dto.PersonStarter;
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
        when(medicalRecordDao.findMedicalRecordByFirstnameAndLastname("john", "pierre")).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordDao.findMedicalRecordByFirstnameAndLastname("Tity", "Delor")).thenReturn(Optional.of(medicalRecord));
        when(medicalRecordDao.findMedicalRecordByFirstnameAndLastname("joe", "claire")).thenReturn(Optional.of(medicalRecord2));

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
}
