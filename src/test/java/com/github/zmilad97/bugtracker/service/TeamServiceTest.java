package com.github.zmilad97.bugtracker.service;

import com.github.zmilad97.bugtracker.dtos.TeamDto;
import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.repository.TeamRepository;
import com.github.zmilad97.bugtracker.repository.UserRepository;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @Mock
    private TeamRepository teamRepository;
    @Mock
    private UserRepository userRepository;

    private TeamService teamService;

    private static MockedStatic<SecurityUtil> utilMockedStatic;

    User user;
    Team team;

    public TeamServiceTest() {
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

    }

    @BeforeAll
    static void beforeAll() {
        utilMockedStatic = Mockito.mockStatic(SecurityUtil.class);
    }

    @AfterAll
    static void afterAll() {
        utilMockedStatic.close();
    }

    @BeforeEach
    void setUp() {
        teamService = new TeamService(teamRepository, userRepository);
    }


    @Test
    @DisplayName("This method should take String of ids then return set of users")
    void getUserSetFromString() {
        Mockito.when(userRepository.findUserById(user.getId())).thenReturn(user);
        Set<User> users = teamService.getUserSetFromString("1,");
        assertTrue(users.contains(user));
        assertEquals(users.size(),1);
    }

}