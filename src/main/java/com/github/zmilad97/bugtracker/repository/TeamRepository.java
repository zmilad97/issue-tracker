package com.github.zmilad97.bugtracker.repository;

import com.github.zmilad97.bugtracker.model.Team;
import com.github.zmilad97.bugtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TeamRepository extends JpaRepository<Team, Integer> {
    Team findTeamById(int id);

    List<Team> findAllByCreator_Id(int id);

    List<Team> findAllByMembersContains(User user);

    boolean existsById(Integer id);
}
