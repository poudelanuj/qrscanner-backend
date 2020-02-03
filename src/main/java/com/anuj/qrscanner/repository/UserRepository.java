package com.dallotech.repository;


import com.dallotech.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

     Optional<User> findByEmail(String email);

     Boolean existsByEmail(String email);


}