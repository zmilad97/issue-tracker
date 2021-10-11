package com.github.zmilad97.bugtracker.service;

import com.github.zmilad97.bugtracker.dtos.ProjectDto;
import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.repository.ProjectRepository;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock
    private ProjectRepository projectRepository;
    @Mock
    private TeamService teamService;


    private ProjectService projectService;

    private static MockedStatic<SecurityUtil> utilMockedStatic;

    User user;
    Project project;
    Team team;

    public ProjectServiceTest() {
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
        project.setTitle("project title");
        project.setDescription("test project description");
        project.setCreator(user);
        project.setCreatedAt(LocalDateTime.now().toString());
        project.setTeam(team);


    }

    @BeforeAll
    static void beforeAll() {
        utilMockedStatic = Mockito.mockStatic(SecurityUtil.class);
    }

    @BeforeEach
    void setUp() {
        projectService = new ProjectService(projectRepository, teamService);
    }

    @AfterAll
    static void afterAll() {
        utilMockedStatic.close();
    }

    @Test
    @DisplayName("This method should return ProjectDto by given Id")
    void getDtoById() {

        Mockito.when(projectRepository.getProjectById(project.getId())).thenReturn(project);
        ProjectDto projectDto = projectService.getDtoById(project.getId());
        verify(projectRepository).getProjectById(project.getId());
        assertEquals(projectDto.getId(), project.getId());
        assertEquals(projectDto.getTeamId(), project.getTeam().getId());
        assertEquals(projectDto.getDescription(), project.getDescription());
        assertEquals(projectDto.getTitle(), project.getTitle());
    }

    @Test
    @DisplayName("This method should save a the project by given ProjectDto")
    void addProject() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setTitle("test");
        projectDto.setTeamId(1);
        projectDto.setDescription("test Description");

        Mockito.when(teamService.getTeamById(1)).thenReturn(team);
        utilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(user);
        projectService.addProject(projectDto);
        ArgumentCaptor<Project> projectArgumentCaptor = ArgumentCaptor.forClass(Project.class);

        verify(projectRepository).save(projectArgumentCaptor.capture());
        Project capturedProject = projectArgumentCaptor.getValue();


        assertEquals(capturedProject.getId(), projectDto.getId());
        assertEquals(capturedProject.getTitle(), projectDto.getTitle());
        assertEquals(capturedProject.getDescription(), projectDto.getDescription());
        assertEquals(capturedProject.getTeam(), team);
    }

    @Test
    @DisplayName("This method should update Project by given ProjectDto")
    void updateProject() {
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(1);
        projectDto.setTitle("test");
        projectDto.setTeamId(1);
        projectDto.setDescription("test Description");
        projectDto.setTeamId(1);
        Mockito.when(projectRepository.getProjectById(project.getId())).thenReturn(project);
        Mockito.when(teamService.getTeamById(team.getId())).thenReturn(team);

        utilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(user);
        projectService.updateProject(projectDto);


        ArgumentCaptor<Project> projectArgumentCaptor = ArgumentCaptor.forClass(Project.class);

        verify(projectRepository).save(projectArgumentCaptor.capture());
        Project capturedProject = projectArgumentCaptor.getValue();

        assertEquals(capturedProject.getId(), projectDto.getId());
        assertEquals(capturedProject.getTitle(), projectDto.getTitle());
        assertEquals(capturedProject.getDescription(), projectDto.getDescription());
        assertEquals(capturedProject.getTeam(), team);

        assertNotEquals(capturedProject.getTitle(), "project title");
    }


    @Test
    @DisplayName("This method should return all project that given user participated")
    void getProjectByUserParticipated() {
        Mockito.when(teamService.getUserTeams(user.getId())).thenReturn(Collections.singletonList(team));
        List<Project> projects = projectService.getProjectByUserParticipated(user);
        projects.forEach(p -> {
            boolean b = p.getTeam().getMembers().contains(user);
            assertTrue(b);
        });
    }


    @Test
    void getProjectById() {
        Mockito.when(projectRepository.getProjectById(project.getId())).thenReturn(project);
        Project p = projectService.getProjectById(project.getId());
        verify(projectRepository).getProjectById(project.getId());
        assertEquals(p, project);
    }
}