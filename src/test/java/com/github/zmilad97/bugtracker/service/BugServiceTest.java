package com.github.zmilad97.bugtracker.service;

import com.github.zmilad97.bugtracker.dtos.BugDto;
import com.github.zmilad97.bugtracker.dtos.UserDto;
import com.github.zmilad97.bugtracker.enums.Status;
import com.github.zmilad97.bugtracker.model.Bug;
import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.repository.BugRepository;
import com.github.zmilad97.bugtracker.repository.UserRepository;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class BugServiceTest {

    @Mock
    private BugRepository bugRepository;
    @Mock
    private TeamService teamService;
    @Mock
    private ProjectService projectService;
    @Mock
    private UserRepository userRepository;

    @Mock
    private BugService bugService;

    private static MockedStatic<SecurityUtil> utilMockedStatic;

    User user;
    Team team;
    Project project;
    List<Bug> bugs;

    public BugServiceTest() {
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


        Bug bug1 = new Bug();
        bug1.setId(2);
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


        Bug bug2 = new Bug();
        bug2.setId(3);
        bug2.setCreatedAt(LocalDateTime.now());
        bug2.setSteps("test step 2");
        bug2.setDescription("test description 2");
        bug2.setVersion("V1");
        bug2.setTitle("Test Title 2");
        bug2.setAssigned(new User());
        bug2.setCreator(new User());
        bug2.setPriority(3);
        bug2.setTeam(team);
        bug2.setProject(project);
        bug2.setStatus(Status.PENDING);
        bug2.setLogs(new ArrayList<>());
        bug2.getLogs().add(user + " Created The bug2");
        this.bugs.addAll(Arrays.asList(bug, bug1, bug2));
    }

    @BeforeAll
    static void beforeAll() {
        utilMockedStatic = Mockito.mockStatic(SecurityUtil.class);
    }

    @BeforeEach
    void setUp() {
        ToolsService toolsService = new ToolsService();
        bugService = new BugService(bugRepository, userRepository, teamService, projectService, toolsService);
    }


    @AfterEach
    void tearDown() {
    }

    @Test
    @DisplayName("This method should return all the bugs that created by user")
    void retrieveBugs() {
        Mockito.when(bugRepository.findBugByCreatorEquals(user)).thenReturn(
                bugs.stream().filter(bug -> bug.getCreator().equals(user)).collect(Collectors.toList()));
        List<BugDto> bugDtos = bugService.retrieveBugDtosByUserCreated(user);
        verify(bugRepository).findBugByCreatorEquals(user);
        assertEquals(bugDtos.size(), 2);
        assertEquals(bugDtos.get(0).getAssignedId(), user.getId());
        assertEquals(bugDtos.get(1).getAssignedId(), user.getId());
    }

    @Test
    @DisplayName("This method should return the bug by the given Id")
    void getBug() {
        for (int i = 0; i < bugs.size(); i++) {
            Mockito.when(bugRepository.findBugById(i)).thenReturn(bugs.get(i));

            Bug bug = bugService.getBug(i);
            verify(bugRepository).findBugById(i);
            assertEquals(bug.getId(), bugs.get(i).getId());
            assertEquals(bug.getCreator(), bugs.get(i).getCreator());
            assertEquals(bug.getTeam(), bugs.get(i).getTeam());
            assertEquals(bug.getProject(), bugs.get(i).getProject());
        }

    }

    @Test
    @DisplayName("This method should save the given BugDto")
    void save() {
        BugDto bugDto = new BugDto();
        bugDto.setId(2);
        bugDto.setSteps("test step 1");
        bugDto.setDescription("test description 1");
        bugDto.setVersion("V1");
        bugDto.setTitle("Test Title 1");
        bugDto.setAssignedId(user.getId());
        bugDto.setCreatorId(user.getId());
        bugDto.setPriority(2);
        bugDto.setTeam(team.getId());
        bugDto.setProjectId(project.getId());

        Mockito.when(userRepository.findUserById(1)).thenReturn(user);

        Mockito.when(projectService.getProjectById(1)).thenReturn(project);
        bugService.save(bugDto);
        verify(userRepository, times(2)).findUserById(1);
        verify(projectService).getProjectById(1);

        ArgumentCaptor<Bug> bugArgumentCaptor = ArgumentCaptor.forClass(Bug.class);

        verify(bugRepository).save(bugArgumentCaptor.capture());
        Bug capturedBug = bugArgumentCaptor.getValue();

        assertEquals(capturedBug.getCreator().getId(), bugDto.getCreatorId());
        assertEquals(capturedBug.getProject().getId(), bugDto.getProjectId());
        assertEquals(capturedBug.getTeam().getId(), bugDto.getTeam());
        assertEquals(capturedBug.getAssigned().getId(), bugDto.getAssignedId());
        assertEquals(capturedBug.getTitle(), bugDto.getTitle());
        assertEquals(capturedBug.getStatus(), Status.PENDING);
    }

    @Test
    @DisplayName("This method Should Return the BugDto of given Bug")
    void getBugDto() {
        Bug bug = bugs.get(0);
        BugDto bugDto = bugService.getBugDto(bug);
        assertEquals(bugDto.getId(), bug.getId());
        assertEquals(bugDto.getAssignedId(), bug.getAssigned().getId());
        assertEquals(bugDto.getAssignedName(), bug.getAssigned().getFirstName() + " " + bug.getAssigned().getLastName());
        assertEquals(bugDto.getCreatorId(), bug.getCreator().getId());
        assertEquals(bugDto.getCreatorName(), bug.getCreator().getFirstName() + " " + bug.getCreator().getLastName());
        assertEquals(bugDto.getDescription(), bug.getDescription());
        assertEquals(bugDto.getTitle(), bug.getTitle());
        assertEquals(bugDto.getLogs(), bug.getLogs());
        assertEquals(bugDto.getPriority(), bug.getPriority());
        assertEquals(bugDto.getSteps(), bug.getSteps());
        assertEquals(bugDto.getVersion(), bug.getVersion());
    }

    @Test
    @DisplayName("This method Should Update the bug from given BugDto")
    void update() {
        utilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(user);


        BugDto bugDto = new BugDto();
        bugDto.setId(2);
        bugDto.setSteps("test step 1");
        bugDto.setDescription("test description 1");
        bugDto.setVersion("V1");
        bugDto.setTitle("Test Title 1");
        bugDto.setAssignedId(user.getId());
        bugDto.setCreatorId(user.getId());
        bugDto.setPriority(2);
        bugDto.setTeam(team.getId());
        bugDto.setProjectId(project.getId());

        final Bug[] bug = new Bug[1];
        bugs.forEach(b -> {
            if (b.getId() == bugDto.getId())
                bug[0] = b;
        });


        Mockito.when(bugRepository.findBugById(bugDto.getId())).thenReturn(bug[0]);
        Mockito.when(userRepository.findUserById(bugDto.getAssignedId())).thenReturn(user);
        Mockito.when(projectService.getProjectById(bugDto.getProjectId())).thenReturn(project);
        bugService.update(bugDto);

        ArgumentCaptor<Bug> bugArgumentCaptor = ArgumentCaptor.forClass(Bug.class);
        verify(bugRepository).save(bugArgumentCaptor.capture());
        Bug capturedBug = bugArgumentCaptor.getValue();

        assertEquals(capturedBug.getCreator().getId(), bugDto.getCreatorId());
        assertEquals(capturedBug.getProject().getId(), bugDto.getProjectId());
        assertEquals(capturedBug.getTeam().getId(), bugDto.getTeam());
        assertEquals(capturedBug.getAssigned().getId(), bugDto.getAssignedId());
        assertEquals(capturedBug.getTitle(), bugDto.getTitle());
        assertEquals(capturedBug.getStatus(), Status.PENDING);
        assertNotEquals(capturedBug.getLogs().size(), 0);
        capturedBug.getLogs().forEach(System.out::println);

    }

    @Test
    @DisplayName("This method should delete the bug by id")
    void deleteBug() {
        utilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(user);
        Bug bug = bugs.get(0);
        bugService.deleteBug(bug);
        verify(bugRepository).delete(bug);
    }

    @Test
    @DisplayName("this method should assign bug to given user id")
    void saveAssign() {
        Bug bug = bugs.get(1);
        bug.setAssigned(null);
        utilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(user);
        Mockito.when(bugRepository.findBugById(bug.getId())).thenReturn(bug);
        Mockito.when(userRepository.findUserById(bug.getCreator().getId())).thenReturn(user);
        Mockito.when(teamService.getUserTeams(user.getId())).thenReturn(Collections.singletonList(team));

        bugService.saveAssign(bug.getId(), user.getId());
        verify(bugRepository).findBugById(bug.getId());
        verify(userRepository).findUserById(bug.getCreator().getId());
        verify(teamService).getUserTeams(user.getId());
        assertEquals(bug.getAssigned(), user);

    }

    @Test
    @DisplayName("This Method should Assign bug to current User")
    void assignToMe() {
        Bug bug = bugs.get(1);
        bug.setAssigned(null);
        utilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(user);
        Mockito.when(bugRepository.findBugById(bug.getId())).thenReturn(bug);

        bugService.assignToMe(bug.getId());
        verify(bugRepository).findBugById(bug.getId());
        assertEquals(bug.getAssigned(), user);
    }

    @Test
    @DisplayName("This method should return List of UserDtos that are in team members list")
    void getUserDtosInBugTeamByBugId() {
        Bug bug = bugs.get(1);
        Mockito.when(bugRepository.findBugById(bug.getId())).thenReturn(bug);
        List<UserDto> userDtos = bugService.getUserDtosInBugTeamByBugId(bug.getId());
        verify(bugRepository).findBugById(bug.getId());
        assertEquals(userDtos.size(), 1);
        assertEquals(userDtos.get(0).getId(), user.getId());
    }

    @Test
    @DisplayName("This method should return bugDto by given id if the assignee is equal to current user")
    void getAssignedBug() {
        Bug bug = bugs.get(1);
        Mockito.when(bugRepository.findBugById(bug.getId())).thenReturn(bug);
        utilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(user);
        BugDto assignedBug = bugService.getAssignedBug(bug.getId());

        assertEquals(assignedBug.getAssignedId(), user.getId());


        //When not equal
        Bug bug2 = bugs.get(2);
        bug2.setAssigned(null);
        Mockito.when(bugRepository.findBugById(bug.getId())).thenReturn(bug2);
        BugDto assignedBug2 = bugService.getAssignedBug(bug.getId());

        assertNotEquals(assignedBug2.getAssignedId(), user.getId());

    }


    @Test
    @DisplayName("This method should return list of bugs by given project id, " +
            " if current user does not access to this project should return empty ArrayList")
    void getBugsByProjectId() {
        Mockito.when(projectService.getProjectById(project.getId())).thenReturn(project);
        Mockito.when(bugRepository.findBugsByProject(project)).thenReturn(bugs);
        utilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(user);
        List<Bug> bugList = bugService.getBugsByProjectId(project.getId());
        verify(projectService).getProjectById(project.getId());
        verify(bugRepository).findBugsByProject(project);

        assertEquals(bugList.size(), 3);

    }

    @Test
    void setStatus() {
        Bug bug = bugs.get(0);
        Mockito.when(bugRepository.findBugById(bug.getId())).thenReturn(bug);
        utilMockedStatic.when(SecurityUtil::getCurrentUser).thenReturn(user);
        bugService.setStatus(bug.getId(),"completed");

        ArgumentCaptor<Bug> bugArgumentCaptor = ArgumentCaptor.forClass(Bug.class);
        verify(bugRepository).save(bugArgumentCaptor.capture());

        Bug capturedBug = bugArgumentCaptor.getValue();

        assertEquals(capturedBug.getStatus(),Status.COMPLETED);
    }

}