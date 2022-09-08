package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.Person;

import java.util.List;

public interface PersonDao {
    List<Person> findAll();
}
