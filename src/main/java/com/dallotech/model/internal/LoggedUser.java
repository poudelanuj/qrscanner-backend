package com.dallotech.model.internal;

import com.dallotech.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoggedUser {
    private Long userId;
    private Boolean isUser;
    private Boolean isAdmin;
    private User user;
    private Set<String> roles;


}
