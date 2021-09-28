package com.github.zmilad97.bugtracker.service;

import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Service
public class ToolsService {


    public String getPassedDate(LocalDateTime fromDateTime, LocalDateTime toDateTime) {

        long years = ChronoUnit.YEARS.between(fromDateTime, toDateTime);
        long months = ChronoUnit.MONTHS.between(fromDateTime, toDateTime);
        long days = ChronoUnit.DAYS.between(fromDateTime, toDateTime);
        long hours = ChronoUnit.HOURS.between(fromDateTime, toDateTime);

        if (years > 0)
            return years + " Years Ago";

        else if (months > 0)
            return months + " Month Ago";

        else if (days > 0)
            return days + " Days Ago";

        else if (hours > 0)
            return hours + " Hours Ago";
        else
            return "Less Than An Hour Ago";

    }

}
