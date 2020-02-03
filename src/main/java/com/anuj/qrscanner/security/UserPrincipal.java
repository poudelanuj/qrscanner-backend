package com.anuj.qrscanner.security;


import com.anuj.qrscanner.model.db.Role;
import com.anuj.qrscanner.model.db.User;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


// custom UserDetails class
// This is the class whose instances will be returned from our custom UserDetailsService
@Data
public class UserPrincipal implements UserDetails {
    private User user;
    private Map<String, Object> attributes;
    public UserPrincipal(User user){
        this.user = user;
    }
    public UserPrincipal(User user,Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        getPrivileges(user.getRoles())
                .forEach(privilege ->
                        authorities.add(new SimpleGrantedAuthority(privilege))
                );
        return authorities;
    }

    @Override
    public String getPassword() {
        return user.getPhoneNumber();
    }

    @Override
    public String getUsername() {
        return user.getPhoneNumber();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    private List<String> getPrivileges(Collection<Role> roles) {
        List<String> privileges = new ArrayList<>();
//        roles
//                .forEach(role -> {
//                            role.getPrivileges()
//                                    .forEach(privilege ->
//                                            privileges.add(privilege.getName())
//                                    );
//                            privileges.add(role.getName());
//                        }
//                );
        return privileges;
    }
}
