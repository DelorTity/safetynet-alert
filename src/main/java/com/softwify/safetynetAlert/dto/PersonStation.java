package com.softwify.safetynetAlert.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class PersonStation {
    private String firstname;
    private String lastname;
    private String address;
    private String phone;
}
