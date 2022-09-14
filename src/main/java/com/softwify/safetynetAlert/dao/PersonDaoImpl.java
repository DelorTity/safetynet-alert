package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.Person;
import org.springframework.stereotype.Repository;

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
    public Optional<Person> findPersonByFirstnameAndLastname(String firstName, String lastName) {
        List<Person> persons = dataStoreManager.getPersons();
        for (Person person : persons) {
            if (person.getFirstName().equals(firstName) && person.getLastName().equals(lastName)) {
                return Optional.of(person);
            }
        }
        return Optional.empty();
    }

    @Override
    public Person addNewPerson(Person person) {
        List<Person> persons = dataStoreManager.getPersons();
        Optional<Person> personByFirstnameAndLastname = findPersonByFirstnameAndLastname(person.getFirstName(), person.getLastName());
        if(personByFirstnameAndLastname.isPresent()) {
            return null;
        } else {
            persons.add(person);
        }
        return person;
    }

    @Override
    public Optional<Person> update(Person person) {
        Optional<Person> optionalPerson = findPersonByFirstnameAndLastname(person.getFirstName(), person.getLastName());
        if (optionalPerson.isPresent()) {
            Person personExist = optionalPerson.get();

            personExist.setAddress(person.getAddress());
            personExist.setCity(person.getCity());
            personExist.setPhone(person.getPhone());
            personExist.setEmail(person.getEmail());
            personExist.setZip(person.getZip());
            return optionalPerson;
        }
        return Optional.empty();
    }

    @Override
    public Optional<Person> delete(String firstname, String lastname) {
        List<Person> persons = dataStoreManager.getPersons();
        Optional<Person> optionalPerson = findPersonByFirstnameAndLastname(firstname, lastname);
        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            persons.remove(person);
            return optionalPerson;
        }
        return Optional.empty();
    }
}
