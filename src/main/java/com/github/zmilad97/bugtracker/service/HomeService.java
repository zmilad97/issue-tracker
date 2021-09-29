package com.github.zmilad97.bugtracker.service;

import com.github.zmilad97.bugtracker.enums.Status;
import com.github.zmilad97.bugtracker.model.Project;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.repository.BugRepository;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeService {
    private final ProjectService projectService;
    private final BugRepository bugRepository;

    @Autowired
    public HomeService(ProjectService projectService, BugRepository bugRepository) {
        this.projectService = projectService;
        this.bugRepository = bugRepository;
    }


    public Map<String, List<Integer>> getStatistics() {
        User user = SecurityUtil.getCurrentUser();
        Map<String, List<Integer>> statistics = new HashMap<>();

        List<Project> projects = projectService.getProjectByUserParticipated(user);
        projects.forEach(project -> {
            List<Integer> stats = new ArrayList<>();
            for (int i = 0; i < 3; i++) {
                int all = bugRepository.findBugsByProjectAndPriority(project, i + 1).size();
                stats.add(all);
                int completed = bugRepository.findBugsByProjectAndPriorityAndStatus(project, i + 1, Status.COMPLETED).size();
                stats.add(completed);
            }

            statistics.put(project.getTitle(), stats);
        });
        return statistics;
    }
}
