package com.epam.esm.util.formatters;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.*;

class TimeFormatterTest {
    private final TimeFormatter timeFormatter = new TimeFormatter();

    @Test
    void timeToIso8601Test() {
        assertEquals("2023-02-28T15:43:42.1",
                timeFormatter.timeToIso8601(LocalDateTime.parse("2023-02-28T15:43:42.1",
                        DateTimeFormatter.ISO_LOCAL_DATE_TIME)));
    }
}