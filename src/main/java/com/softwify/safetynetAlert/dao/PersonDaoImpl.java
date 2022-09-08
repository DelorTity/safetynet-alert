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
        persons.add(person);
        return person;
    }
}
