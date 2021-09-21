package com.github.zmilad97.bugtracker.repository;

import com.github.zmilad97.bugtracker.model.Bug;
import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BugRepository extends JpaRepository<Bug, Integer> {
    Bug findBugById(int id);

    List<Bug> findBugByCreatorEquals(User user);

    List<Bug> findBugsByAssigned(User user);

    List<Bug> findBugsByTeam(Team team);

    List<Bug> findBugsByProject(Project project);

    List<Bug> findBugsByProjectAndPriorityAndCompleted(Project project, int priority, boolean isCompleted);

    List<Bug> findBugsByProjectAndPriority(Project project, int priority);
}
