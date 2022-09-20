package com.softwify.safetynetAlert.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FireStation {
    private String address;
    private int station;
}