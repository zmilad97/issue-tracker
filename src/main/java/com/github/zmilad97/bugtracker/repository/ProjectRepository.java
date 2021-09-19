package com.github.zmilad97.bugtracker.repository;

import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
    List<Project> getAllByCreator(User user);

    Project getProjectById(int id);

    List<Project> getProjectsByTeam(Team team);

}
