package com.anuj.qrscanner.service;

import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;


    public ResponseEntity<?> getAllTransaction(User user) {
        return null;
    }
}
