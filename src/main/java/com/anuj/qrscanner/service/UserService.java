package com.anuj.qrscanner.service;

import com.anuj.qrscanner.model.dto.UserDto;
import com.anuj.qrscanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;



}
