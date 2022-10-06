package com.softwify.safetynetAlert.personmapper;

import com.softwify.safetynetAlert.dto.PersonStation;
import com.softwify.safetynetAlert.mappers.PersonMapper;
import com.softwify.safetynetAlert.model.Person;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class PersonMapperTest {
    PersonMapper personMapper = mock(PersonMapper.class);
    @Test
    public void testShouldVerifyThatPersonMapCorrectly() {
        Person person = Person.builder()
                .firstName("John")
                .lastName("Boyd")
                .phone("345-33")
                .build();

        PersonStation personStation = PersonMapper.mapToPersonStation(person);

        assertEquals("John", personStation.getFirstname());
        assertEquals("Boyd", personStation.getLastname());
        assertEquals("345-33", personStation.getPhone());
    }

    /*@Test
    public void testShoulVerifyThatPersonMapCorrectly() {
        List<Person> persons = Arrays.asList(
                Person.builder().firstName("john").lastName("pierre").build(),
                Person.builder().firstName("jean").lastName("alfred").build()
        );

        List<PersonStation> personStations = PersonMapper.mapToPersonStations(persons);
        when(PersonMapper.mapToPersonStation(Person.builder().build())).thenReturn(PersonStation.builder().build());
        assertEquals("john", personStations.get(0).getFirstname());
        assertEquals("alfred", personStations.get(1).getLastname());
    }*/
}
