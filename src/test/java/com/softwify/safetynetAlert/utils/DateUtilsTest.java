package com.softwify.safetynetAlert.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtilsTest {

    @Test
    public void TestShouldVerifyThatCalculatedAgeIsCorrect() throws ParseException {
        String dateString = "08/07/1999";
        Date date = new SimpleDateFormat("dd/MM/yyyy").parse(dateString);
        int newAge = DateUtils.calculateAge(date);

        Assertions.assertEquals(23, newAge);
    }
}
