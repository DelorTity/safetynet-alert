package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.dto.Child;
import com.softwify.safetynetAlert.dto.PersonStarter;
import com.softwify.safetynetAlert.dto.PersonStation;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.ecception.StationNotFoundException;
import com.softwify.safetynetAlert.mappers.PersonMapper;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.util.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class PersonStationServiceImpl implements PersonStationService {
    private FireStationsDao fireStationDao;
    private PersonDao personDao;
    private MedicalRecordDao medicalRecordDao;


    public PersonStationServiceImpl(FireStationsDao fireStationDao, PersonDao personDao, MedicalRecordDao medicalRecordDao) {
        this.fireStationDao = fireStationDao;
        this.personDao = personDao;
        this.medicalRecordDao = medicalRecordDao;
    }

    @Override
    public PersonStarter findPersonByStation(int stationNumber) {
        List<FireStation> fireStations = fireStationDao.findByStationNumber(stationNumber);
        if (fireStations.isEmpty()) {
            throw new StationNotFoundException();
        }

        List<Person> persons = new ArrayList<>();
        for (FireStation fireStation : fireStations) {
            List<Person> personsFromAddress = personDao.findByAddress(fireStation.getAddress());
            persons.addAll(personsFromAddress);
        }

        int numberOfAdults = 0;
        int numberOfChildren = 0;
        for (Person person : persons) {
            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.findMedicalRecordByFirstnameAndLastname(person.getFirstName(), person.getLastName());
            if (optionalMedicalRecord.isEmpty()) {
                continue;
            }
            Date birthdate = optionalMedicalRecord.get().getBirthdate();
            int age = DateUtils.calculateAge(birthdate);
            if (age < 18) {
                numberOfChildren++;
            } else {
                numberOfAdults++;
            }
        }

        List<PersonStation> personsFromStation = PersonMapper.mapToPersonStations(persons);

        return PersonStarter.builder()
                .persons(personsFromStation)
                .numberOfAdults(numberOfAdults)
                .numberOfChildren(numberOfChildren)
                .build();
    }

    @Override
    public List<Child> findPersonByAddress(String address) {
        List<Person> childByAddress = personDao.findByAddress(address);
        if (childByAddress.isEmpty()) {
            throw new PersonNotFoundException();
        }

        List<Child> childrenList = new ArrayList<>();
        for (Person person : childByAddress) {
            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.findMedicalRecordByFirstnameAndLastname(person.getFirstName(), person.getLastName());
            if (optionalMedicalRecord.isEmpty()) {
                continue;
            }

            Date dateOfBirth = optionalMedicalRecord.get().getBirthdate();
            int age = DateUtils.calculateAge(dateOfBirth);
            if (age < 18) {
                childrenList.add(Child.builder().
                        firstname(person.getFirstName()).
                        lastname(person.getLastName()).
                        age(age).
                        build());
            }
        }
        return childrenList;
    }
}
