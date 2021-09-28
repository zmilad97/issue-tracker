package com.github.zmilad97.bugtracker;

import com.github.zmilad97.bugtracker.service.ToolsService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;


class ToolsServiceTest {


    @Test
    void getPassedDateTestWithNoneEqualYear() {
        ToolsService toolsService = new ToolsService();
        LocalDateTime toDateTime = LocalDateTime.of(2014, 9, 10, 6, 40, 45);
        LocalDateTime fromDateTime = LocalDateTime.of(1984, 12, 16, 7, 45, 55);
        String d = toolsService.getPassedDate(fromDateTime, toDateTime);
        assertEquals(d, "29 years ago");
    }

    @Test
    void getPassedDateTestWithEqualYear() {
        ToolsService toolsService = new ToolsService();
        LocalDateTime toDateTime = LocalDateTime.of(2014, 9, 10, 6, 40, 45);
        LocalDateTime fromDateTime = LocalDateTime.of(2014, 5, 16, 7, 45, 55);
        String d = toolsService.getPassedDate(fromDateTime, toDateTime);
        assertEquals(d, "3 month ago");
    }


}
