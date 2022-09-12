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
        Optional<Person> optionalPerson = personDao.findPersonByFirstnameAndLastname(firstName, lastName);
        return optionalPerson.orElseThrow(PersonNotFoundException::new);
    }

    @Override
    public Person savePerson(Person person) {
        Person personSaved = personDao.addPerson(person);
        if (personSaved == null) {
            throw new PersonAlreadyExistsException();
        }
        return personSaved;
    }
}
