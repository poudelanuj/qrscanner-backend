package com.anuj.qrscanner.model.internal;

import com.anuj.qrscanner.model.db.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class LoggedUser {
    private UUID userId;
    private Boolean isUser;
//    private Boolean isAdmin;
    private User user;
    private Set<String> roles;


}
