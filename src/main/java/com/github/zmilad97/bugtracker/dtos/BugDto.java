package com.github.zmilad97.bugtracker.dtos;


import com.github.zmilad97.bugtracker.annotation.ProjectExist;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

public class BugDto {
    private int id;
    @NotEmpty
    @NotNull
    private String title;
    private String description;
    private String steps;
    private String version;
    private int priority;  //TODO: make it to Enum
    private int assignedId;
    private int creatorId;
    private String time;
    private int team;
    private String teamName;
    private String assignedName;
    private String creatorName;
    @ProjectExist
    private Integer projectId;
    private String projectName;
    private String status;
    private String createdTimePassed;
    private String lastUpdatedTimePassed;
    private List<String> logs;

    public String getCreatorName() {
        return creatorName;
    }

    public void setCreatorName(String creatorName) {
        this.creatorName = creatorName;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSteps() {
        return steps;
    }

    public void setSteps(String steps) {
        this.steps = steps;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public int getAssignedId() {
        return assignedId;
    }

    public void setAssignedId(int assignedId) {
        this.assignedId = assignedId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getTeam() {
        return team;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeam(int team) {
        this.team = team;
    }

    public String getAssignedName() {
        return assignedName;
    }

    public void setAssignedName(String assignedName) {
        this.assignedName = assignedName;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCreatedTimePassed() {
        return createdTimePassed;
    }

    public void setCreatedTimePassed(String createdTimePassed) {
        this.createdTimePassed = createdTimePassed;
    }

    public String getLastUpdatedTimePassed() {
        return lastUpdatedTimePassed;
    }

    public void setLastUpdatedTimePassed(String lastUpdatedTimePassed) {
        this.lastUpdatedTimePassed = lastUpdatedTimePassed;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }
}
