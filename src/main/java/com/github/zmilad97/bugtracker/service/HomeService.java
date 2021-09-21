package com.github.zmilad97.bugtracker.service;

import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.repository.BugRepository;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeService {
    private final ProjectService projectService;
    private final BugService bugService;
    private final BugRepository bugRepository;

    @Autowired
    public HomeService(ProjectService projectService, BugService bugService, BugRepository bugRepository) {
        this.projectService = projectService;
        this.bugService = bugService;
        this.bugRepository = bugRepository;
    }


    public Map<String, Integer[]> getStatistics() {
        User user = SecurityUtil.getCurrentUser();
        Map<String, Integer[]> statistics = new HashMap<>();
        List<Project> projects = projectService.getProjectByUserParticipated(user);
        projects.forEach(project -> {
            Integer[] stats = new Integer[6];
            for (int i = 0; i < 6; i++) {
                int all = bugRepository.findBugsByProjectAndPriority(project, 1).size();
                stats[i] = all;
                i++;
                int completed = bugRepository.findBugsByProjectAndPriorityAndCompleted(project, 1, true).size();
                stats[i] = completed;
            }
            statistics.put(project.getTitle(), stats);
        });
        return statistics;
    }
}
