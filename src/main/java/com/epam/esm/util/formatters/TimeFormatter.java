package com.epam.esm.util.formatters;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class TimeFormatter {
    public String timeToIso8601(LocalDateTime date){
        return date.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}
