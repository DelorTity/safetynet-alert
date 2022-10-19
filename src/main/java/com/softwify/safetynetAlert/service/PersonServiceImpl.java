package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.exceptions.PersonAlreadyExistsException;
import com.softwify.safetynetAlert.exceptions.PersonNotFoundException;
import com.softwify.safetynetAlert.model.Person;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PersonServiceImpl implements PersonService {

    private PersonDao personDao;

    public PersonServiceImpl(PersonDao personDao) {
        this.personDao = personDao;
    }

    @Override
    public List<Person> findAll() {
        return personDao.findAll();
    }

    @Override
    public Person findByFirstnameLastname(String firstName, String lastName) {
        Optional<Person> optionalPerson = personDao.findByFirstnameAndLastname(firstName, lastName);
        return optionalPerson.orElseThrow(PersonNotFoundException::new);
    }

    @Override
    public Optional<Person> savePerson(Person person) {
        Optional<Person> optionalPerson = personDao.addPerson(person);
        if (optionalPerson.isEmpty()) {
            throw new PersonAlreadyExistsException();
        }
        return optionalPerson;
    }

    @Override
    public Optional<Person> updatePerson(Person person) {
        Optional<Person> update = personDao.update(person);
        if (update.isEmpty()) {
            throw new PersonNotFoundException();
        }
        return update;
    }

    @Override
    public Optional<Person> deletePerson(String firstname, String lastname) {
        Optional<Person> delete = personDao.delete(firstname, lastname);
        if (delete.isEmpty()) {
            throw new PersonNotFoundException();
        }
        return delete;
    }

}
