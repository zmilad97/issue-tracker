package com.github.zmilad97.bugtracker.controller;

import com.github.zmilad97.bugtracker.dtos.BugDto;
import com.github.zmilad97.bugtracker.enums.Status;
import com.github.zmilad97.bugtracker.model.Bug;
import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.service.BugService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.*;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@RunWith(SpringJUnit4ClassRunner.class)
@ExtendWith(MockitoExtension.class)
class BugApiControllerTest {

    private MockMvc mockMvc;


    private BugApiController bugApiController;

    @Mock
    private BugService bugService;

    User user;
    Team team;
    Project project;
    List<Bug> bugs;


    @BeforeEach
    void setUp() {
        bugApiController = new BugApiController(bugService);
        mockMvc = MockMvcBuilders.standaloneSetup(bugApiController).build();

        bugs = new ArrayList<>();
        user = new User();
        user.setId(1);
        user.setActive(true);
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("testPassword");


        team = new Team();
        team.setId(1);
        team.setTitle("test team title");
        team.setDescription("test team description");
        team.setCreator(user);
        Set<User> members = new HashSet<>();
        members.add(user);
        team.setMembers(members);


        project = new Project();
        project.setId(1);
        project.setTitle("test project title");
        project.setDescription("test project description");
        project.setCreator(user);
        project.setCreatedAt(LocalDateTime.now().toString());
        project.setTeam(team);


        Bug bug = new Bug();
        bug.setId(1);
        bug.setCreatedAt(LocalDateTime.now());
        bug.setSteps("test step 0");
        bug.setDescription("test description 0");
        bug.setVersion("V0");
        bug.setTitle("Test Title 0");
        bug.setAssigned(user);
        bug.setCreator(user);
        bug.setPriority(1);
        bug.setTeam(team);
        bug.setProject(project);
        bug.setStatus(Status.PENDING);
        bug.setLogs(new ArrayList<>());
        bug.getLogs().add(user + " Created The Bug0");

        this.bugs.addAll(Collections.singletonList(bug));
    }

    @Test
    void getBug() throws Exception {
        Bug bug = bugs.get(0);
        Mockito.when(bugService.getBug(1)).thenReturn(bug);
        BugDto bugDto = new BugDto();
        bugDto.setTitle(bug.getTitle());
        Mockito.when(bugService.getBugDto(bug)).thenReturn(bugDto);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/bug/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(MockMvcResultMatchers.content().string(containsString(bug.getTitle())));
    }
}