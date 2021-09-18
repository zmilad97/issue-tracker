package com.github.zmilad97.bugtracker.service;

import com.github.zmilad97.bugtracker.dtos.TeamDto;
import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.repository.TeamRepository;
import com.github.zmilad97.bugtracker.repository.UserRepository;
import com.github.zmilad97.bugtracker.security.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

@Service
public class TeamService {
    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    @Autowired
    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    public List<TeamDto> getTeamDtoByUser(User user) {
        List<Team> teams = getUserTeams(user.getId());
        List<TeamDto> teamDtos = new ArrayList<>();
        teams.forEach(t -> {
            TeamDto teamDto = getTeamDtoByTeam(t);
            teamDtos.add(teamDto);
        });
        return teamDtos;
    }

    public List<Team> getUserTeams(int id) {
        User user = userRepository.findUserById(id);
        return teamRepository.findAllByMembersContains(user);
    }


    public TeamDto getTeamDtoByTeam(Team team) {
        TeamDto teamDto = new TeamDto();
        teamDto.setId(team.getId());
        teamDto.setTitle(team.getTitle());
        teamDto.setDescription(team.getDescription());
        teamDto.setCreatorId(team.getCreator().getId());
        return teamDto;
    }

    public List<TeamDto> getTeamDtoByCreator(int id) {
        List<Team> teams = teamRepository.findAllByCreator_Id(id);
        List<TeamDto> teamDtos = new ArrayList<>();
        teams.forEach(t -> {
            TeamDto teamDto = getTeamDtoByTeam(t);
            teamDtos.add(teamDto);
        });
        return teamDtos;
    }

    public void save(TeamDto teamDto) {
        Team team = new Team();
        team.setId(teamDto.getId());
        team.setCreator(SecurityUtil.getCurrentUser());
        team.setMembers(getUserSetFromString(teamDto.getMembers()));
        team.setDescription(teamDto.getDescription());
        team.setTitle(teamDto.getTitle());
        teamRepository.save(team);
    }


    public Set<User> getUserSetFromString(String members) {
        String[] memberArray = members.split(",");
        Set<User> memberSet = new HashSet<>();
        String regex = "[0-9]+";
        Pattern p = Pattern.compile(regex);
        if (memberArray.length > 0)
            for (String s : memberArray) {
                if (p.matcher(s).matches()) {
                    User user = userRepository.findUserById(Integer.parseInt(s));
                    memberSet.add(user);
                }
            }
        return memberSet;
    }

    public TeamDto getTeamDtoByTeamId(int id) {
        Team team = getTeamById(id);
        TeamDto teamDto = new TeamDto();
        teamDto.setId(team.getId());
        teamDto.setTitle(team.getTitle());
        teamDto.setDescription(team.getDescription());
        teamDto.setCreatorId(team.getCreator().getId());
        StringBuilder builder = new StringBuilder();
        team.getMembers().forEach(m -> builder.append(m.getId()).append(","));
        teamDto.setMembers(builder.toString());
        return teamDto;
    }

    public Team getTeamById(int id) {
        return teamRepository.findTeamById(id);
    }

    public void saveEdit(TeamDto teamDto) {
        Team team = teamRepository.findTeamById(teamDto.getId());
        if (team.getCreator().equals(SecurityUtil.getCurrentUser())) {
            team.setCreator(SecurityUtil.getCurrentUser());
            team.setMembers(getUserSetFromString(teamDto.getMembers()));
            team.setDescription(teamDto.getDescription());
            team.setTitle(teamDto.getTitle());
            teamRepository.save(team);
        }
    }

    public void deleteTeam(int id) {
        Team team = teamRepository.findTeamById(id);
        if (team.getCreator().equals(SecurityUtil.getCurrentUser())) {
            teamRepository.delete(team);
        }
    }
}
