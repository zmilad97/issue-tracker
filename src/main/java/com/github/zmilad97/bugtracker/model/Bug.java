package com.github.zmilad97.bugtracker.model;

import com.github.zmilad97.bugtracker.enums.Status;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class Bug {
    @Id
    @GeneratedValue
    private int id;
    private String title;
    private String description;
    private String steps;
    private String version;
    private int priority;  //TODO: make it to Enum
    @ManyToOne
    private User assigned;
    @ManyToOne
    private User creator;
    @ManyToOne
    private Team team;
    @ManyToOne
    private Project project;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdate;
    private Status status;
    @ElementCollection
    private List<String> logs;

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

    public User getAssigned() {
        return assigned;
    }

    public void setAssigned(User assigned) {
        this.assigned = assigned;
    }

    public User getCreator() {
        return creator;
    }

    public void setCreator(User creator) {
        this.creator = creator;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(LocalDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<String> getLogs() {
        return logs;
    }

    public void setLogs(List<String> logs) {
        this.logs = logs;
    }
}
