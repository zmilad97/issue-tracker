package com.github.zmilad97.bugtracker.repository;

import com.github.zmilad97.bugtracker.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    User findUserById(int id);



    User findByUsername(String s);

    User findByEmail(String email);

}
