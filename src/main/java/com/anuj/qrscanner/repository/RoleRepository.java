package com.anuj.qrscanner.repository;

import com.anuj.qrscanner.constant.RoleName;
import com.anuj.qrscanner.model.db.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByRoleName(RoleName roleName);

    Boolean existsByRoleName(RoleName roleName);

}
