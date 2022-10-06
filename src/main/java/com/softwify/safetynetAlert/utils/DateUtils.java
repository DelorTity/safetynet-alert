package com.softwify.safetynetAlert.utils;

import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.Date;

public class DateUtils {
    private DateUtils() {
    }

    public static int calculateAge(Date birthdate) {
        LocalDate birthdateInLocalDate = LocalDate.ofInstant(birthdate.toInstant(), ZoneId.systemDefault());
        return Period.between(birthdateInLocalDate, LocalDate.now()).getYears();
    }
}
