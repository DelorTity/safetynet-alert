package com.softwify.safetynetAlert.mappers;

import com.softwify.safetynetAlert.dto.PersonStation;
import com.softwify.safetynetAlert.model.Person;

import java.util.ArrayList;
import java.util.List;

public class PersonMapper {

    private PersonMapper() {
    }

    public static PersonStation mapToPersonStation(Person person) {
        PersonStation personStation = PersonStation.builder()
                .firstname(person.getFirstName())
                .lastname(person.getLastName())
                .address(person.getAddress())
                .phone(person.getPhone())
                .build();
        return personStation;
    }

    public static List<PersonStation> mapToPersonStations(List<Person> persons) {
        List<PersonStation> personStations = new ArrayList<>();
        for (Person person : persons) {
            PersonStation personStation = mapToPersonStation(person);
            personStations.add(personStation);
        }

        return personStations;
       /* return persons.stream()
                .map(person -> mapToPersonStation(person))
                .collect(Collectors.toList());*/
    }
}
