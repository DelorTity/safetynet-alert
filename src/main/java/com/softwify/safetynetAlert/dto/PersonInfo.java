package com.softwify.safetynetAlert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PersonInfo {
    String lastname;
    String address;
    String email;
    int age;
    List<String> medication;
    List<String> allergies;

}
