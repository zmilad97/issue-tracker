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
class ProjectRepositoryTest {

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

        User user2 = new User();
        user2.setActive(true);
        user2.setFirstName("FirstName2");
        user2.setLastName("LastName2");
        user2.setUsername("test2");
        user2.setEmail("test2@test2.com");
        user2.setPassword("test2Password");
        userRepository.save(user2);


        Team team = new Team();
        team.setTitle("test team title");
        team.setDescription("test team description");
        team.setCreator(user);
        Set<User> members = new HashSet<>();
        members.add(user);
        members.add(user2);
        team.setMembers(members);
        teamRepository.save(team);

        Project project = new Project();
        project.setTitle("test project title");
        project.setDescription("test project description");
        project.setCreator(user);
        project.setCreatedAt(LocalDateTime.now().toString());
        project.setTeam(team);
        projectRepository.save(project);

        Project project2 = new Project();
        project2.setTitle("test project2 title");
        project2.setDescription("test project2 description");
        project2.setCreator(user);
        project2.setCreatedAt(LocalDateTime.now().toString());
        project2.setTeam(team);
        projectRepository.save(project2);

        Project project3 = new Project();
        project3.setTitle("test project3 title");
        project3.setDescription("test project3 description");
        project3.setCreator(user2);
        project3.setCreatedAt(LocalDateTime.now().toString());
        project3.setTeam(team);
        projectRepository.save(project);

    }

    @AfterEach
    void tearDown() {
        userRepository.deleteAll();
        projectRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    void getAllByCreator() {
        User creator = userRepository.findAll().get(0);
        List<Project> projects = projectRepository.getAllByCreator(creator);
        projects.forEach(project -> assertEquals(project.getCreator(), creator));
    }

    @Test
    void getProjectById() {
        Project project = projectRepository.findAll().get(0);
        Project found = projectRepository.getProjectById(project.getId());
        assertEquals(found, project);
    }

    @Test
    void getProjectsByTeam() {
        Team team = teamRepository.findAll().get(0);
        List<Project> projects = projectRepository.getProjectsByTeam(team);
        projects.forEach(project -> assertEquals(project.getTeam(),team));
    }
}