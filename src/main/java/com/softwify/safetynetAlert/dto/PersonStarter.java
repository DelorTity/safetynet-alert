package com.softwify.safetynetAlert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PersonStarter {
    List<PersonStation> persons;
    int numberOfAdults;
    int numberOfChildren;
}