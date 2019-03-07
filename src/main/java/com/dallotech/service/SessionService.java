package com.dallotech.service;

import com.dallotech.exception.AccessDeniedException;
import com.dallotech.exception.UserAuthenticationFailedException;
import com.dallotech.exception.UserEmailNotFound;
import com.dallotech.model.Role;
import com.dallotech.model.User;
import com.dallotech.model.internal.LoggedUser;
import com.dallotech.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
public class SessionService {
    @Autowired
    private UserRepository userRepository;


    public LoggedUser getLoggedInUserDetails() throws UserAuthenticationFailedException {
        // get logged in user
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetails))
            throw new UserAuthenticationFailedException();
        String loggedInUserEmail = ((UserDetails) principal).getUsername();
        if (userRepository.existsByEmail(loggedInUserEmail)) {
            User user = userRepository.findByEmail(loggedInUserEmail).get();
            Set<String> roles = new HashSet<>();

            for (Role a : user.getRoles()) {
                roles.add(a.getRoleName().toString());
            }

            return new LoggedUser(user.getIdUser(),
                    roles.contains(Role.RoleName.USER.toString()),
                    roles.contains(Role.RoleName.ADMIN.toString()),
                    user,
                    roles);
        } else {
            throw new UserEmailNotFound(loggedInUserEmail);
        }
    }

    public LoggedUser verifyAndGetLoggedInUser() {
        try {
            return getLoggedInUserDetails();
        } catch (UserAuthenticationFailedException e) {
            throw new Error(e.getMessage());
        }
    }

    public LoggedUser verifyAndGetLoggedInAdmin() {
         //get logged in admin
        LoggedUser loggedUser = null;
        try {
            loggedUser = getLoggedInUserDetails();
        } catch (UserAuthenticationFailedException e) {
            e.printStackTrace();
        }
        if (!loggedUser.getIsAdmin()) {
            throw new AccessDeniedException("You need to be admin to perform this action.");
        }
         return loggedUser;
    }
}
