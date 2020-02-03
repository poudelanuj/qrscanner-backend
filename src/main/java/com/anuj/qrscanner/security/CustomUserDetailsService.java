package com.anuj.qrscanner.security;


import com.anuj.qrscanner.exception.UserNotFoundException;
import com.anuj.qrscanner.model.User;
import com.anuj.qrscanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String phoneNumber)
            throws UsernameNotFoundException {
        User user = userRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with email : " + phoneNumber)
                );

        return new UserPrincipal(user);
    }

    @Transactional
    public UserDetails loadUserById(UUID id) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findById(id);
        if(userOptional.isPresent()){
            return new UserPrincipal(userOptional.get());
        }
        throw new UserNotFoundException();
    }
}