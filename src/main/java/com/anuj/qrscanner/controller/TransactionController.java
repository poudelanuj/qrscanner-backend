package com.anuj.qrscanner.controller;

import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.model.dto.request.TransactionDecisionRequestDto;
import com.anuj.qrscanner.model.dto.request.TransactionInitiateDto;
import com.anuj.qrscanner.model.dto.request.TransactionRequestDto;
import com.anuj.qrscanner.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.anuj.qrscanner.constant.Urls.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @GetMapping(value = "/transactions")
    public ResponseEntity<?> getTransactions(User user){
        return transactionService.getAllTransaction(user);
    }

    @PostMapping(value = "/transaction")
    public ResponseEntity<?> postTransactionDto(User user, TransactionInitiateDto transactionInitiateDto){
        return transactionService.createTransaction(user,transactionInitiateDto);
    }

    @PostMapping(value = "/receiver_transaction_decision")
    public ResponseEntity<?> receiverTransactionDecision(User user, TransactionDecisionRequestDto transactionDecisionRequestDto){
        return transactionService.receiverTransactionDecision(user, transactionDecisionRequestDto);
    }

    @PostMapping(value = "/request_transaction")
    public ResponseEntity<?> requestTransaction(User requestingUser, TransactionRequestDto transactionRequestDto){
        return transactionService.requestTransaction(requestingUser, transactionRequestDto);
    }

    @PostMapping(value = "/sender_transaction_decision")
    public ResponseEntity<?> senderTransactionDecision(User user,TransactionDecisionRequestDto transactionDecisionRequestDto){
        return transactionService.senderTransactionDecision(user, transactionDecisionRequestDto);
    }
}
