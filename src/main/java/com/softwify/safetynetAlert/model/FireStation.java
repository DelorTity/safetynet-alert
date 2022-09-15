package com.softwify.safetynetAlert.model;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class FireStation {
    private String address;
    private int station;
}