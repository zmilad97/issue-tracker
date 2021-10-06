package com.github.zmilad97.bugtracker.repository;

import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class TeamRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setActive(true);
        user.setFirstName("FirstName");
        user.setLastName("LastName");
        user.setUsername("test");
        user.setEmail("test@test.com");
        user.setPassword("testPassword");
        userRepository.save(user);


        Team team = new Team();
        team.setTitle("test team title");
        team.setDescription("test team description");
        team.setCreator(user);
        Set<User> members = new HashSet<>();
        members.add(user);
        team.setMembers(members);
        teamRepository.save(team);

        Team team2 = new Team();
        team2.setTitle("test team2 title");
        team2.setDescription("test team2 description");
        team2.setCreator(user);
        Set<User> members2 = new HashSet<>();
        members.add(user);
        team2.setMembers(members);
        teamRepository.save(team2);

        Team team3 = new Team();
        team3.setTitle("test team3 title");
        team3.setDescription("test team3 description");
        team3.setCreator(user);
        Set<User> members3 = new HashSet<>();
        team3.setMembers(members);
        teamRepository.save(team3);

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        projectRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    void findTeamById() {
        Team team = teamRepository.findAll().get(0);
        Team team2 = teamRepository.findTeamById(team.getId());
        assertEquals(team, team2);
    }

    @Test
    void findAllByCreator_Id() {
        User creator = userRepository.findAll().get(0);
        List<Team> teams = teamRepository.findAllByCreator_Id(creator.getId());
        teams.forEach(team -> assertEquals(team.getCreator(), creator));
    }

    @Test
    void findAllByMembersContains() {
        User member = userRepository.findAll().get(0);
        List<Team> teams = teamRepository.findAllByMembersContains(member);
        teams.forEach(team -> {
            boolean b = team.getMembers().contains(member);
            assertTrue(b);
        });

    }
}