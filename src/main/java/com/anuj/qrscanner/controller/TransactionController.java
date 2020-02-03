package com.anuj.qrscanner.controller;

import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.anuj.qrscanner.constant.Urls.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions(User user){
        return transactionService.getAllTransaction(user);
    }

}
