package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.Person;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class PersonDaoImpl implements PersonDao {
    private final DataStoreManager dataStoreManager;
    public PersonDaoImpl(DataStoreManager dataStoreManager) {
        this.dataStoreManager = dataStoreManager;
    }

    @Override
    public List<Person> findAll() {
        return dataStoreManager.getPersons();
    }

    @Override
    public Optional<Person> findByFirstnameAndLastname(String firstName, String lastName) {
        List<Person> persons = dataStoreManager.getPersons();
        for (Person person : persons) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                return Optional.of(person);
            }
        }
        return Optional.empty();
    }

    @Override
    public Optional<Person> addPerson(Person person) {
        Optional<Person> personOptional = findByFirstnameAndLastname(person.getFirstName(), person.getLastName());
        if (personOptional.isPresent()) {
            return Optional.empty();
        }

        List<Person> persons = dataStoreManager.getPersons();
        persons.add(person);
        return Optional.of(person);
    }

    @Override
    public Optional<Person> update(Person person) {
        Optional<Person> optionalPerson = findByFirstnameAndLastname(person.getFirstName(), person.getLastName());
        if (optionalPerson.isPresent()) {
            Person existingPerson = optionalPerson.get();

            existingPerson.setAddress(person.getAddress());
            existingPerson.setCity(person.getCity());
            existingPerson.setPhone(person.getPhone());
            existingPerson.setEmail(person.getEmail());
            existingPerson.setZip(person.getZip());
            return optionalPerson;
        } else {
            return Optional.empty();
        }
    }

    @Override
    public Optional<Person> delete(String firstname, String lastname) {
        Optional<Person> optionalPerson = findByFirstnameAndLastname(firstname, lastname);
        if (optionalPerson.isPresent()) {
            List<Person> persons = dataStoreManager.getPersons();
            Person person = optionalPerson.get();
            persons.remove(person);
            return optionalPerson;
        }
        return Optional.empty();
    }

    @Override
    public List<Person> findByAddress(String address) {
        List<Person> persons = new ArrayList<>(dataStoreManager.getPersons());
        persons.removeIf(person -> !address.contains(person.getAddress()));
        return persons;
    }

    @Override
    public List<Person> findPersons(String firstName, String lastName) {
        List<Person> persons = dataStoreManager.getPersons();
        List<Person> personList = new ArrayList<>();
        for (Person person : persons) {
            if (person.getFirstName().equals(firstName) || person.getLastName().equals(lastName)) {
                personList.add(person);
            }
        }
        return personList;
    }

    @Override
    public List<Person> findByCity(String city) {
        List<Person> persons = dataStoreManager.getPersons();
        List<Person> personCity = new ArrayList<>();
        for (Person person : persons) {
            if (person.getCity().equals(city)) {
                personCity.add(person);
            }
        }
        return personCity;
    }
}
