package com.softwify.safetynetAlert.dto;

import com.softwify.safetynetAlert.model.Person;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Child {
    List<Person> persons;
}
