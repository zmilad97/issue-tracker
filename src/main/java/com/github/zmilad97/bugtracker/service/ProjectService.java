package com.github.zmilad97.bugtracker.service;

import com.github.zmilad97.bugtracker.dtos.ProjectDto;
import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.repository.ProjectRepository;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;
    private final TeamService teamService;

    @Autowired
    public ProjectService(ProjectRepository projectRepository, TeamService teamService) {
        this.projectRepository = projectRepository;
        this.teamService = teamService;
    }

    public ProjectDto getDtoById(int id) {
        Project project = projectRepository.getProjectById(id);
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId());
        projectDto.setTitle(project.getTitle());
        projectDto.setDescription(project.getDescription());
        projectDto.setCreatedAt(project.getCreatedAt());
        if (project.getCreator() != null) {
            projectDto.setCreatorId(project.getCreator().getId());
            projectDto.setCreatorName(project.getCreator().getFirstName() + " " + project.getCreator().getLastName());
        }

        if (project.getTeam() != null) {
            projectDto.setTeamId(project.getTeam().getId());
            projectDto.setTeamName(project.getTeam().getTitle());
        }

        return projectDto;
    }

    public void addProject(ProjectDto projectDto) {
        Project project = new Project();
        project.setCreator(SecurityUtil.getCurrentUser());
        project.setTitle(projectDto.getTitle());
        project.setDescription(projectDto.getDescription());
        project.setCreatedAt(LocalDateTime.now().toString());
        if (projectDto.getTeamId() != 0)
            project.setTeam(teamService.getTeamById(projectDto.getTeamId()));

        projectRepository.save(project);
    }


    public void updateProject(ProjectDto projectDto) {
        Project project = projectRepository.getProjectById(projectDto.getId());
        if (project != null && project.getCreator().equals(SecurityUtil.getCurrentUser())) {
            project.setTitle(projectDto.getTitle());
            project.setDescription(projectDto.getDescription());
            project.setCreatedAt(projectDto.getCreatedAt());
            if (projectDto.getTeamId() != 0)
                project.setTeam(teamService.getTeamById(projectDto.getTeamId()));
            projectRepository.save(project);
        }
    }

    public void deleteProject(int id) {
        Project project = projectRepository.getProjectById(id);
        if (project != null) {
            projectRepository.delete(project);
        }
    }

    public List<Project> getProjectCreatedByUser(User user) {
        return projectRepository.getAllByCreator(user);
    }

    public List<Project> getProjectByUserParticipated(User user) {
        List<Team> teams = teamService.getUserTeams(user.getId());
        List<Project> projects = new ArrayList<>();
        teams.forEach(v -> projects.addAll(projectRepository.getProjectsByTeam(v)));
        return projects;
    }


}
