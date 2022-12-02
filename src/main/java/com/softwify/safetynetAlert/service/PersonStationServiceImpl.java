package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.FireStationDao;
import com.softwify.safetynetAlert.dao.MedicalRecordDao;
import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.dto.*;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.exceptions.StationNotFoundException;
import com.softwify.safetynetAlert.mappers.PersonMapper;
import com.softwify.safetynetAlert.model.FireStation;
import com.softwify.safetynetAlert.model.MedicalRecord;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.utils.DateUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class PersonStationServiceImpl implements PersonStationService {
    private FireStationDao fireStationDao;
    private PersonDao personDao;
    private MedicalRecordDao medicalRecordDao;

    public PersonStationServiceImpl(FireStationDao fireStationDao, PersonDao personDao, MedicalRecordDao medicalRecordDao) {
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

    public List<Child> findPersonByAddress(String address) {
        List<Person> personByAddress = personDao.findByAddress(address);
        if (personByAddress.isEmpty()) {
            throw new PersonNotFoundException();
        }

        List<Child> children = new ArrayList<>();
        for (Person person : personByAddress) {
            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.findByFirstnameAndLastname(person.getFirstName(), person.getLastName());
            if (optionalMedicalRecord.isEmpty()) {
                continue;
            }

            Date birthdate = optionalMedicalRecord.get().getBirthdate();
            int age = DateUtils.calculateAge(birthdate);
            if (age < 18) {
                children.add(Child.builder().
                        firstname(optionalMedicalRecord.get().getFirstName()).
                        lastname(optionalMedicalRecord.get().getLastName())
                        .age(age)
                        .build()
                );
            }
        }

        return children;
    }

    @Override
    public List<String> findPhoneNumberByStation(int firestationNumber) {
        List<FireStation> fireStations = fireStationDao.findByStationNumber(firestationNumber);

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
    public List<PersonFire> findPersonFireByAddress(String address) {
        List<Person> personDaoByAddress = personDao.findByAddress(address);

        Optional<FireStation> fireStation = fireStationDao.findByAddress(address);

        List<PersonFire> personFires = new ArrayList<>();
        for (Person person : personDaoByAddress) {
            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.findByFirstnameAndLastname(person.getFirstName(), person.getLastName());
            if (optionalMedicalRecord.isEmpty()) {
                continue;
            }

            Date birthdate = optionalMedicalRecord.get().getBirthdate();
            int age = DateUtils.calculateAge(birthdate);
            personFires.add(PersonFire.builder()
                    .lastname(person.getLastName())
                    .phone(person.getPhone())
                    .stationNumber(fireStation.get().getStation())
                    .age(age)
                    .allergies(optionalMedicalRecord.get().getAllergies())
                    .medications(optionalMedicalRecord.get().getMedications())
                  .build()
          );
        }
        return personFires;
    }

    @Override
    public List<FloodStation> findFloodByStationNumber(List<Integer> stationNumbers) {
        List<FireStation> fireStations = fireStationDao.findByStationNumbers(stationNumbers);

        List<FloodStation> floodStations = new ArrayList<>();
        for (FireStation fireStation : fireStations) {
            List<Person> personDaoByAddress = personDao.findByAddress(fireStation.getAddress());

            for (Person person : personDaoByAddress) {
                Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.findByFirstnameAndLastname(person.getFirstName(), person.getLastName());

                if (optionalMedicalRecord.isEmpty()) {
                    continue;
                }

                Date birthdate = optionalMedicalRecord.get().getBirthdate();
                int age = DateUtils.calculateAge(birthdate);

                floodStations.add(FloodStation.builder()
                                .allergies(optionalMedicalRecord.get().getAllergies())
                                .phone(person.getPhone())
                        .medications(optionalMedicalRecord.get().getMedications())
                        .lastname(optionalMedicalRecord.get().getLastName())
                                .age(age)
                        .build());
            }
        }
        return floodStations;
    }

    @Override
    public List<PersonInfo> findPersonByFirstAndLastName(String firstname, String lastname) {
        List<Person> persons = personDao.findPersons(firstname, lastname);
        List<PersonInfo> personInfos = new ArrayList<>();

        for (Person person : persons) {
            Optional<MedicalRecord> optionalMedicalRecord = medicalRecordDao.findByFirstnameAndLastname(person.getFirstName(), person.getLastName());
            Date birthdate = optionalMedicalRecord.get().getBirthdate();
            if (optionalMedicalRecord.isEmpty()) {
                continue;
            }
            int age = DateUtils.calculateAge(birthdate);

            personInfos.add(PersonInfo.builder()
                    .lastname(optionalMedicalRecord.get().getLastName())
                    .address(person.getAddress())
                    .email(person.getEmail())
                    .age(age)
                    .allergies(optionalMedicalRecord.get().getAllergies())
                    .medications(optionalMedicalRecord.get().getMedications())
                    .build());
        }

        return personInfos;
    }

    @Override
    public List<String> findPersonByCity(String city) {
        List<Person> personByCity = personDao.findByCity(city);
        List<String> emails = new ArrayList<>();

        for (Person person : personByCity) {
            emails.add(person.getEmail());
        }

        return emails;
    }
}
