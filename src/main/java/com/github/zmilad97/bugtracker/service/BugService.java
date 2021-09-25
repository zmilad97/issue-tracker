package com.github.zmilad97.bugtracker.service;

import com.github.zmilad97.bugtracker.dtos.BugDto;
import com.github.zmilad97.bugtracker.dtos.ProjectDto;
import com.github.zmilad97.bugtracker.dtos.UserDto;
import com.github.zmilad97.bugtracker.model.Bug;
import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.repository.BugRepository;
import com.github.zmilad97.bugtracker.repository.UserRepository;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class BugService {
    private final BugRepository bugRepository;
    private final UserRepository userRepository;
    private final TeamService teamService;
    private final ProjectService projectService;

    @Autowired
    public BugService(BugRepository bugRepository, UserRepository userRepository, TeamService teamService, ProjectService projectService) {
        this.bugRepository = bugRepository;
        this.userRepository = userRepository;
        this.teamService = teamService;
        this.projectService = projectService;
    }


    public List<BugDto> retrieveBugs() {
        List<Bug> bugs = bugRepository.findBugByCreatorEquals(SecurityUtil.getCurrentUser());
        List<BugDto> bugDtos = new ArrayList<>();
        bugs.forEach(bug -> {
            BugDto bugDto = getBugDto(bug.getId());
            bugDtos.add(bugDto);
        });
        return bugDtos;
    }

    public Bug getBug(int id) {
        User user = SecurityUtil.getCurrentUser();
        Bug bug = bugRepository.findBugById(id);
        if (bug != null && bug.getCreator().equals(user)) {
            return bug;
        } else
            return new Bug();
    }

    public void save(BugDto bugDto) {
        Bug bug = new Bug();
        bug.setCreatedAt(LocalDateTime.now().toString());
        bug.setSteps(bugDto.getSteps());
        bug.setDescription(bugDto.getDescription());
        bug.setVersion(bugDto.getVersion());
        bug.setTitle(bugDto.getTitle());
        bug.setAssigned(userRepository.findUserById(bugDto.getAssignedId()));
        bug.setCreator(SecurityUtil.getCurrentUser());
        bug.setPriority(bugDto.getPriority());
        if (bugDto.getProjectId() != -1) {
            bug.setProject(projectService.getProjectById(bugDto.getProjectId()));
            bug.setTeam(bug.getProject().getTeam());
        }
        bugRepository.save(bug);
    }

    public BugDto getBugDto(int id) {
        Bug bug = getBug(id);
        BugDto bugDto = new BugDto();
        bugDto.setId(bug.getId());
        bugDto.setDescription(bug.getDescription());
        bugDto.setTitle(bug.getTitle());
        bugDto.setTime(bug.getCreatedAt());
        bugDto.setPriority(bug.getPriority());
        if (bug.getAssigned() != null)
            bugDto.setAssignedId(bug.getAssigned().getId());
        if (bug.getTeam() != null)
            bugDto.setTeam(bug.getTeam().getId());
        if (bug.getCreator() != null) {
            bugDto.setCreatorName(bug.getCreator().getFirstName() + " " + bug.getCreator().getLastName());
            bugDto.setCreatorId(bug.getCreator().getId());
        }
        if (bug.getProject() != null) {
            bugDto.setProjectId(bug.getProject().getId());
            bugDto.setProjectName(bug.getProject().getTitle());

        }
        bugDto.setSteps(bug.getSteps());
        bugDto.setVersion(bug.getVersion());

        if (bug.getTeam() != null)
            bugDto.setTeamName(bug.getTeam().getTitle());
        else
            bugDto.setTeamName("No team please set a team first");
        if (bug.getAssigned() != null)
            bugDto.setAssignedName(bug.getAssigned().getFirstName() + " " + bug.getAssigned().getLastName());
        else
            bugDto.setAssignedName("No One");
        bugDto.setCompleted(bug.isCompleted());
        return bugDto;
    }

    public void update(BugDto bugDto) {
        Bug bug = bugRepository.findBugById(bugDto.getId());
        if (bug != null && bug.getCreator().equals(SecurityUtil.getCurrentUser())) {
            save(bugDto);
        }
    }

    public void deleteBug(int id) {
        Bug bug = bugRepository.findBugById(id);
        if (bug.getCreator().equals(SecurityUtil.getCurrentUser())) {
            bugRepository.delete(bug);
        }


    }

    public void saveAssign(int bugId, int userId) {
        Bug bug = bugRepository.findBugById(bugId);
        if (bug != null && bug.getCreator().equals(SecurityUtil.getCurrentUser())) {
            User user = userRepository.findUserById(userId);
            if (user != null && bug.getTeam().getMembers().contains(user)) {
                List<Team> usersTeam = teamService.getUserTeams(user.getId());
                if (usersTeam.contains(bug.getTeam())) {
                    bug.setAssigned(user);
                    bugRepository.save(bug);
                }
            }
        }
    }


    public void assignToMe(int bugId) {
        Bug bug = bugRepository.findBugById(bugId);
        if (bug != null && bug.getAssigned() == null) {
            bug.setAssigned(SecurityUtil.getCurrentUser());
            bugRepository.save(bug);
        }
    }


    public List<UserDto> assignUser(int id) {
        Bug bug = bugRepository.findBugById(id);
        List<UserDto> userDtos = new ArrayList<>();
        if (bug != null && bug.getTeam() != null && bug.getProject() != null) {
            Set<User> users = bug.getProject().getTeam().getMembers();
            users.forEach(user -> {
                UserDto userDto = new UserDto();
                userDto.setId(user.getId());
                userDto.setUsername(user.getUsername());
                userDto.setFirstName(user.getFirstName());
                userDto.setLastName(user.getLastName());
                userDtos.add(userDto);
            });

        }

        return userDtos;
    }

    public List<BugDto> assignedToMe() {
        List<Bug> bugs = getAssignedToUserBugs(SecurityUtil.getCurrentUser());
        List<BugDto> bugDtos = new ArrayList<>();
        bugs.forEach(bug -> {
            BugDto bugDto = getBugDto(bug.getId());
            bugDtos.add(bugDto);
        });
        return bugDtos;
    }

    private List<Bug> getAssignedToUserBugs(User user) {
        return bugRepository.findBugsByAssigned(user);
    }

    public BugDto getAssignedBug(int id) {
        Bug bug = bugRepository.findBugById(id);
        if (bug.getAssigned().equals(SecurityUtil.getCurrentUser()))
            return getBugDto(bug.getId());
        else
            return new BugDto();
    }

    public List<BugDto> getBugDtosByProjectId(int id) {
        List<BugDto> bugDtos = new ArrayList<>();
        List<Bug> bugs = getBugsByProjectId(id);
        bugs.forEach(bug -> {
            BugDto bugDto = getBugDto(bug.getId());
            bugDtos.add(bugDto);
        });
        return bugDtos;
    }


    public List<Bug> getBugsByProjectId(int id) {
        Project project = projectService.getProjectById(id);
        User user = SecurityUtil.getCurrentUser();
        if (project.getTeam().getMembers().contains(user)) {
            return bugRepository.findBugsByProject(project);
        }
        return new ArrayList<>();
    }

    public void complete(int id, String condition) {
        Bug bug = bugRepository.findBugById(id);
        if (bug.getCreator().equals(SecurityUtil.getCurrentUser()) || bug.getAssigned().equals(SecurityUtil.getCurrentUser())) {
            bug.setCompleted(condition.equals("true"));
            bugRepository.save(bug);
        }
    }

    public Set<ProjectDto> getProjectDtoSetFromBugDto(List<BugDto> bugs) {
        Set<Project> projects = new HashSet<>();
        Set<ProjectDto> projectDtos = new HashSet<>();
        bugs.forEach(bugDto -> {
            if (bugDto.getProjectId() != 0)
                projects.add(projectService.getProjectById(bugDto.getProjectId()));
        });
        projects.forEach(project -> projectDtos.add(projectService.getDtoById(project.getId())));
        return projectDtos;
    }
}
