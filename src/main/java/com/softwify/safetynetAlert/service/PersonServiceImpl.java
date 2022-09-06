package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.model.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonServiceImpl implements PersonService {

    @Autowired
    private PersonDao personDao;

    @Override
    public List<Person> findAll() {
        return personDao.findAll();
    }
}
