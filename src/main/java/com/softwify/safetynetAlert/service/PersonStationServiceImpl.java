package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationsDao;
import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.dto.*;
import com.softwify.safetynetAlert.ecception.FireStationNotFoundException;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import com.softwify.safetynetAlert.ecception.StationNotFoundException;
import com.softwify.safetynetAlert.mappers.PersonMapper;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.util.DateUtils;
import org.springframework.stereotype.Service;

import java.util.*;

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
            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.findByFirstnameAndLastname(person.getFirstName(), person.getLastName());
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
            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.findByFirstnameAndLastname(person.getFirstName(), person.getLastName());
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

    @Override
    public List<String> findPhoneNumberByStation(int fireStationNumber) {
        List<FireStation> fireStations = fireStationDao.findByStationNumber(fireStationNumber);
        if (fireStations.isEmpty()) {
            throw new FireStationNotFoundException();
        }

        List<String> phoneAlerts = new ArrayList<>();
        for (FireStation fireStation : fireStations) {
            List<Person> personByAddress = personDao.findByAddress(fireStation.getAddress());

            for (Person person : personByAddress) {
                phoneAlerts.add(person.getPhone());
            }
        }
        return phoneAlerts;
    }

    @Override
    public List<PersonFire> findFireStationByAddress(String address) {
        List<Person> persons = personDao.findByAddress(address);

        Optional<FireStation> fireStations = fireStationDao.findByAddress(address);

        List<PersonFire> personFireList = new ArrayList<>();
        for (Person person : persons) {
            Optional<MedicalRecord> medicalRecord = medicalRecordDao.findByFirstnameAndLastname(person.getFirstName(), person.getLastName());
            if (medicalRecord.isEmpty()){
                continue;
            }

            Date dateOfBirth = medicalRecord.get().getBirthdate();
            int age = DateUtils.calculateAge(dateOfBirth);
            personFireList.add(PersonFire.builder()
                    .stationNumber(fireStations.get().getStation())
                    .lastname(person.getLastName())
                    .medications(medicalRecord.get().getMedications())
                    .allergies(medicalRecord.get().getAllergies())
                    .age(age)
                    .phone(person.getPhone())
                    .build()

            );
        }
        return personFireList;
    }

    public List<FloodStation> findFloodByStationNumber(List<Integer> stationNumber) {
        List<FireStation> fireStations = fireStationDao.findByStationNumbers(stationNumber);

        List<FloodStation> floodStations = new ArrayList<>();
        for (FireStation fireStation : fireStations) {
            List<Person> persons = personDao.findByAddress(fireStation.getAddress());
            for (Person person : persons) {
                Optional<MedicalRecord> medicalRecord = medicalRecordDao.findByFirstnameAndLastname(person.getFirstName(), person.getLastName());
                if (medicalRecord.isEmpty()) {
                    continue;
                }
                Date dateOfBirth = medicalRecord.get().getBirthdate();
                int age = DateUtils.calculateAge(dateOfBirth);
                floodStations.add(FloodStation.builder()
                        .lastname(person.getLastName())
                        .medications(medicalRecord.get().getMedications())
                        .allergies(medicalRecord.get().getAllergies())
                        .age(age)
                        .phone(person.getPhone())
                        .build()

                );
            }
        }
        return floodStations;
    }

    @Override
    public List<PersonInfo> findPersonByFirstNameAndLastName(String firstName, String lastName) {
        List<Person> personList = personDao.findPersons(firstName, lastName);

        List<PersonInfo> personInfoList = new ArrayList<>();
        for (Person person : personList) {
            Optional<MedicalRecord> medicalRecord = medicalRecordDao.findByFirstnameAndLastname(person.getFirstName(), person.getLastName());
            if (medicalRecord.isEmpty()){
                continue;
            }
            Date dateOfBirth = medicalRecord.get().getBirthdate();
            int age = DateUtils.calculateAge(dateOfBirth);
            personInfoList.add(PersonInfo.builder()
                    .email(person.getEmail())
                    .lastname(person.getLastName())
                    .age(age)
                    .allergies(medicalRecord.get().getAllergies())
                    .medication(medicalRecord.get().getMedications())
                    .build());
        }
        return personInfoList;
    }

    @Override
    public List<String> findBYCity(String city) {
        List<Person> personsByCity = personDao.findByCity(city);
        List<String> email = new ArrayList<>();

        for (Person person : personsByCity) {
            email.add(person.getEmail());
        }
        return email;
    }
}
