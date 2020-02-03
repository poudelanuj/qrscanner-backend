package com.anuj.qrscanner.service;

import com.anuj.qrscanner.model.db.Transaction;
import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.model.dto.TransactionDto;
import com.anuj.qrscanner.model.dto.TransactionDtoList;
import com.anuj.qrscanner.model.dto.request.TransactionRequestDto;
import com.anuj.qrscanner.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    TransactionRepository transactionRepository;


    public ResponseEntity<?> getAllTransaction(User user){
        List<Transaction> transactionSentList = transactionRepository.findAllBySerderUser(user);
        List<Transaction> transactionReceivedList = transactionRepository.findAllByReceiverUser(user);

        List<TransactionDto> sentTransactionListDto = transactionSentList.stream().map(TransactionDto::getTransactionDto).collect(Collectors.toList());
        List<TransactionDto> receivedTransactionListDto = transactionReceivedList.stream().map(TransactionDto::getTransactionDto).collect(Collectors.toList());

        TransactionDtoList transactionDtoList = new TransactionDtoList();
        transactionDtoList.setSentTransactionList(sentTransactionListDto);
        transactionDtoList.setReceivedTransacton(receivedTransactionListDto);

        return ResponseEntity.ok(transactionDtoList);
    }

    public ResponseEntity<?> createTransaction(User user, TransactionRequestDto transactionRequestDto){
        return null;
    }


}
