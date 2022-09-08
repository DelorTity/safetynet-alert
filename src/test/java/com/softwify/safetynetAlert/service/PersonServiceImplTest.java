package com.softwify.safetynetAlert.service;

import com.softwify.safetynetAlert.dao.PersonDao;
import com.softwify.safetynetAlert.ecception.PersonNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
public class PersonServiceImplTest {
    PersonDao personDao = Mockito.mock(PersonDao.class);
    PersonServiceImpl personService = new PersonServiceImpl(personDao);
    @Test
    public void testShouldVerifyTheNumberOfTimeThatPersonDaoIsCallIntoPersonService() {
        personService.findAll();
        verify(personDao, times(1)).findAll();
    }

    @Test
    public void getPersonByFirstnameAndLastnameShouldThrowExceptionWhenThereIsNoPerson() {
        when(personDao.findPersonByFirstnameAndLastname(anyString(), anyString())).thenReturn(Optional.empty());

        assertThrows(PersonNotFoundException.class, () -> personService.findByFirstnameLastname("Black", "James"));
        verify(personDao, times(1)).findPersonByFirstnameAndLastname("Black", "James");
    }
}