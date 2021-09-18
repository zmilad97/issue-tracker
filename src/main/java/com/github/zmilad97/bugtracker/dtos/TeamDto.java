package com.github.zmilad97.bugtracker.dtos;

import com.github.zmilad97.bugtracker.model.User;

import java.util.ArrayList;
import java.util.List;

public class TeamDto {
    private int id;
    private String title;
    private String description;
    private int creatorId;
    private String members;


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

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getMembers() {
        return members;
    }

    public void setMembers(String members) {
        this.members = members;
    }
}
