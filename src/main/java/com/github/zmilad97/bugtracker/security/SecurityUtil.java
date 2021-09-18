package com.github.zmilad97.bugtracker.security;

import com.github.zmilad97.bugtracker.model.User;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static User user;

    public static User getCurrentUser() {
        return ((UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser();
    }

}
