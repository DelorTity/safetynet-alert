package com.softwify.safetynetAlert.servicetest;

import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.model.Person;
import com.softwify.safetynetAlert.service.PersonServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest
public class PersonServiceImplTest {
    PersonDao personDao = Mockito.mock(PersonDao.class);
    PersonServiceImpl personService = new PersonServiceImpl(personDao);

    @Test
    public void testShouldVerifyTheNumberOfTimeThatPersonDaoIsCallIntoPersonService() {
        List<Person> persons = personService.findAll();
        verify(personDao, times(1)).findAll();
    }
}
