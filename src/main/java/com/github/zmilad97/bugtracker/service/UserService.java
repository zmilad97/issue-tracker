package com.github.zmilad97.bugtracker.service;

import com.github.zmilad97.bugtracker.dtos.UserDto;
import com.github.zmilad97.bugtracker.exception.UserAlreadyExistException;
import com.github.zmilad97.bugtracker.model.User;
import com.github.zmilad97.bugtracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TeamService teamService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, TeamService teamService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.teamService = teamService;
    }

    public User registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException {
        if (emailExist(userDto.getEmail())) {
            throw new UserAlreadyExistException("There is an account with that email address: "
                    + userDto.getEmail());
        } else if (usernameExist(userDto.getUsername())) {
            throw new UserAlreadyExistException("There is an account with that Username : "
                    + userDto.getUsername());
        }
        User user = new User();
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setUsername(userDto.getUsername());
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        user.setEmail(userDto.getEmail());
        user.setActive(true);
        return userRepository.save(user);
    }

    public UserDto getUserDtoByUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getLastName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        return userDto;
    }

    private boolean emailExist(String email) {
        return userRepository.findByEmail(email) != null;
    }

    private boolean usernameExist(String username) {
        return userRepository.findByUsername(username) != null;
    }
}
