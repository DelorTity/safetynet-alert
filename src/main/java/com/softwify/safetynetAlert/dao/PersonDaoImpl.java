package com.softwify.safetynetAlert.dao;

import com.softwify.safetynetAlert.model.Person;
import org.springframework.stereotype.Repository;

import java.util.List;

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
}
