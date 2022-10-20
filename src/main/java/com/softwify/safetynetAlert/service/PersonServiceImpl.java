package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.ecception.PersonAlreadyExitException;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
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
        Optional<Person> optionalPerson = personDao.findPersonByFirstnameAndLastname(firstName, lastName);
        return optionalPerson.orElseThrow(PersonNotFoundException::new);
    }
    @Override
    public Optional<Person> save(Person person) {
        Optional<Person> addNewPerson = personDao.addNewPerson(person);
        if(addNewPerson.isEmpty()) {
            throw new PersonAlreadyExitException();
        }
        return addNewPerson;
    }
    @Override
    public Optional<Person> update(Person person) {
        Optional<Person> update = personDao.update(person);
        if(update.isEmpty()) {
            throw new PersonNotFoundException();
        }
        return update;
    }

    @Override
    public Optional<Person> delete(String firstname, String lastname) {
        Optional<Person> delete = personDao.delete(firstname, lastname);
        if (delete.isEmpty()) {
            throw new PersonNotFoundException();
        }
        return delete;
    }

    @Override
    public List<Person> findByAddress(String address) {
        return personDao.findByAddress(address);
    }
}
