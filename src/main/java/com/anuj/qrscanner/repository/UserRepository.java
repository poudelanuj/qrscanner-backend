package com.anuj.qrscanner.repository;


import com.anuj.qrscanner.model.db.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {

    Optional<User> findByPhoneNumber(String phoneNumber);




}
