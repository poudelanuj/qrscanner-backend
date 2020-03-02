package com.anuj.qrscanner.component;


import com.anuj.qrscanner.constant.RoleName;
import com.anuj.qrscanner.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.management.relation.RoleNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Component to update users role and privilege when added or removed.
 */
@Component
@Slf4j
public class UpdateRolesAndPrivileges {
    private final RoleService roleService;

    @Autowired
    public UpdateRolesAndPrivileges(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostConstruct
    public void init() {
        log.info("Checking and Updating roles and privileges in the database.");

        // create privileges if not found

        // create roles wth privileges if not found
        Arrays.stream(RoleName.values())
                .forEach(roleName -> {
                    if (!roleService.isRolePresent(roleName)) {
                        roleService.addRole(roleName);
                        log.info("Created new row for {} role.", roleName);
                    }
                });
    }
}

