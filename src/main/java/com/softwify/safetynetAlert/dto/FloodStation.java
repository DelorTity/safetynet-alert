package com.softwify.safetynetAlert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FloodStation {
    private String lastname;
    private String phone;
    List<Integer> stationNumber;
    int age;
    List<String> medications;
    List<String> allergies;
}
