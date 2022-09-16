package com.softwify.safetynetAlert.model;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FireStation {
    private String address;
    private int station;
}