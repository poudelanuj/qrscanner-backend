package com.anuj.qrscanner.service;


import com.anuj.qrscanner.constant.RoleName;
import com.anuj.qrscanner.exception.RoleNotFountException;
import com.anuj.qrscanner.model.db.Role;
import com.anuj.qrscanner.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.management.relation.RoleNotFoundException;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getParticularRole(RoleName roleName) throws RoleNotFountException {
        return roleRepository.findByRoleName(roleName).orElseThrow(RoleNotFountException::new);
    }

    public Collection<Role> assignRole(Collection<Role> roles, RoleName roleName) throws RoleNotFoundException{
        Optional<Role> optionalRole=roleRepository.findByRoleName(roleName);
        if(optionalRole.isPresent()){
            if(!roles.contains(optionalRole.get())){
                roles.add(optionalRole.get());
            }
            return roles;
        }
        throw new RoleNotFoundException(roleName.toString()+" Not found");
    }

    public Boolean isRolePresent(RoleName roleName){
        return roleRepository.existsByRoleName(roleName);
    }


    public void addRole(RoleName roleName) {
        Role role = new Role();
        role.setRoleName(roleName);
        roleRepository.save(role);
    }

}
