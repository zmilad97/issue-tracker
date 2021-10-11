package com.github.zmilad97.bugtracker.service;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ToolsServiceTest {


    @Test
    void getPassedDateTestWithNoneEqualYear() {
        ToolsService toolsService = new ToolsService();
        LocalDateTime toDateTime = LocalDateTime.of(2014, 9, 10, 6, 40, 45);
        LocalDateTime fromDateTime = LocalDateTime.of(1984, 12, 16, 7, 45, 55);
        String d = toolsService.getPassedDate(fromDateTime, toDateTime);
        assertEquals(d, "29 Years Ago");
    }

    @Test
    void getPassedDateTestWithEqualYear() {
        ToolsService toolsService = new ToolsService();
        LocalDateTime toDateTime = LocalDateTime.of(2014, 9, 10, 6, 40, 45);
        LocalDateTime fromDateTime = LocalDateTime.of(2014, 5, 16, 7, 45, 55);
        String d = toolsService.getPassedDate(fromDateTime, toDateTime);
        assertEquals(d, "3 Month Ago");
    }


}
