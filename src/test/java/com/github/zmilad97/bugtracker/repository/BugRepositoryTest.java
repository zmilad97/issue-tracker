package com.github.zmilad97.bugtracker.repository;

import com.github.zmilad97.bugtracker.enums.Status;
import com.github.zmilad97.bugtracker.model.Bug;
import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class BugRepositoryTest {

    @Autowired
    private BugRepository bugRepository;

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


        Bug bug = new Bug();
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
        bugRepository.save(bug);


        Bug bug1 = new Bug();
        bug1.setCreatedAt(LocalDateTime.now());
        bug1.setSteps("test step 1");
        bug1.setDescription("test description 1");
        bug1.setVersion("V1");
        bug1.setTitle("Test Title 1");
        bug1.setAssigned(user);
        bug1.setCreator(user);
        bug1.setPriority(2);
        bug1.setTeam(team);
        bug1.setProject(project);
        bug1.setStatus(Status.PENDING);
        bug1.setLogs(new ArrayList<>());
        bug1.getLogs().add(user + " Created The bug1");
        bugRepository.save(bug1);


        Bug bug2 = new Bug();
        bug2.setCreatedAt(LocalDateTime.now());
        bug2.setSteps("test step 2");
        bug2.setDescription("test description 2");
        bug2.setVersion("V1");
        bug2.setTitle("Test Title 2");
        bug2.setAssigned(user2);
        bug2.setCreator(user2);
        bug2.setPriority(3);
        bug2.setTeam(team);
        bug2.setProject(project);
        bug2.setStatus(Status.PENDING);
        bug2.setLogs(new ArrayList<>());
        bug2.getLogs().add(user + " Created The bug2");
        bugRepository.save(bug2);

    }

    @AfterEach
    void tearDown() {
        bugRepository.deleteAll();
        userRepository.deleteAll();
        projectRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    void findBugById() {
        List<Bug> bugs = bugRepository.findAll();
        Bug bug = bugRepository.findBugById(bugs.get(0).getId());
        assertEquals(bug, bugs.get(0));
    }

    @Test
    void findBugByCreatorEquals() {
        List<User> users = userRepository.findAll();
        List<Bug> bugs1 = bugRepository.findBugsByCreatorEquals(users.get(0));
        bugs1.forEach(b -> assertEquals(b.getCreator(), users.get(0)));

        List<Bug> bugs2 = bugRepository.findBugsByCreatorEquals(users.get(1));
        bugs2.forEach(b -> assertEquals(b.getCreator(), users.get(1)));

    }

    @Test
    void findBugsByAssignedAndProject() {
        List<User> users = userRepository.findAll();
        List<Project> projects = projectRepository.findAll();
        List<Bug> bugs1 = bugRepository.findBugsByAssignedAndProject(users.get(0), projects.get(0));
        bugs1.forEach(b -> {
            assertEquals(b.getAssigned(), users.get(0));
            assertEquals(b.getProject(), projects.get(0));
        });
    }

    @Test
    void findBugsByTeam() {
        List<Team> teams = teamRepository.findAll();
        List<Bug> bugs = bugRepository.findBugsByTeam(teams.get(0));
        bugs.forEach(bug -> assertEquals(bug.getTeam(), teams.get(0)));
    }

    @Test
    void findBugsByProject() {
        List<Project> projects = projectRepository.findAll();
        List<Bug> bugs = bugRepository.findBugsByProject(projects.get(0));
        bugs.forEach(bug -> assertEquals(bug.getProject(), projects.get(0)));

    }

    @Test
    void findBugsByProjectAndPriorityAndStatus() {
        List<Project> projects = projectRepository.findAll();
        List<Bug> bugs = bugRepository.findBugsByProjectAndPriorityAndStatus(projects.get(0), 1, Status.PENDING);
        bugs.forEach(bug -> {
            assertEquals(bug.getProject(), projects.get(0));
            assertEquals(bug.getPriority(), 1);
            assertEquals(bug.getStatus(), Status.PENDING);
        });
        bugs = bugRepository.findBugsByProjectAndPriorityAndStatus(projects.get(0), 2, Status.PENDING);
        bugs.forEach(bug -> {
            assertEquals(bug.getProject(), projects.get(0));
            assertEquals(bug.getPriority(), 2);
            assertEquals(bug.getStatus(), Status.PENDING);
        });

        bugs = bugRepository.findBugsByProjectAndPriorityAndStatus(projects.get(0), 3, Status.PENDING);
        bugs.forEach(bug -> {
            assertEquals(bug.getProject(), projects.get(0));
            assertEquals(bug.getPriority(), 3);
            assertEquals(bug.getStatus(), Status.PENDING);
        });

        bugs = bugRepository.findBugsByProjectAndPriorityAndStatus(projects.get(0), 2, Status.COMPLETED);
        assertEquals(bugs.size(), 0);
    }

    @Test
    void findBugsByProjectAndPriority() {
        List<Project> projects = projectRepository.findAll();
        List<Bug> bugs = bugRepository.findBugsByProjectAndPriority(projects.get(0), 1);
        bugs.forEach(bug -> assertEquals(bug.getPriority(), 1));

        bugs = bugRepository.findBugsByProjectAndPriority(projects.get(0), 2);
        bugs.forEach(bug -> assertEquals(bug.getPriority(), 2));

        bugs = bugRepository.findBugsByProjectAndPriority(projects.get(0), 3);
        bugs.forEach(bug -> assertEquals(bug.getPriority(), 3));
    }
}