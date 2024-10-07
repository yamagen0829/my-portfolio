package com.example.nagoyameshi;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class TimeConverter {
	public static LocalTime convertTime(String timeStr) {
        if (timeStr.equals("24:00")) {
            return LocalTime.MIDNIGHT; // これを00:00として扱う
        }

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        try {
            return LocalTime.parse(timeStr, formatter);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid time: " + timeStr);
        }
    }
}