package com.anuj.qrscanner.service;

import com.anuj.qrscanner.constant.TransactionStatus;
import com.anuj.qrscanner.model.db.Transaction;
import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.model.dto.request.TransactionDecisionRequestDto;
import com.anuj.qrscanner.model.dto.response.TransactionResponseDto;
import com.anuj.qrscanner.model.dto.TransactionDtoList;
import com.anuj.qrscanner.model.dto.request.TransactionRequestDto;
import com.anuj.qrscanner.payload.ServerResponse;
import com.anuj.qrscanner.repository.TransactionRepository;
import com.anuj.qrscanner.repository.UserRepository;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MessageService messageService;


    public ResponseEntity<?> getAllTransaction(User user) {
        List<Transaction> transactionSentList = transactionRepository.findAllBySenderUser(user);
        List<Transaction> transactionReceivedList = transactionRepository.findAllByReceiverUser(user);

        List<TransactionResponseDto> sentTransactionListDto = transactionSentList.stream().map(TransactionResponseDto::getTransactionDto).collect(Collectors.toList());
        List<TransactionResponseDto> receivedTransactionListDto = transactionReceivedList.stream().map(TransactionResponseDto::getTransactionDto).collect(Collectors.toList());

        TransactionDtoList transactionDtoList = new TransactionDtoList();
        transactionDtoList.setSentTransactionList(sentTransactionListDto);
        transactionDtoList.setReceivedTransacton(receivedTransactionListDto);

        return ResponseEntity.ok(transactionDtoList);
    }

    public ResponseEntity<?> createTransaction(User user, TransactionRequestDto transactionRequestDto) {
        if (user.getCurrentBalance() > transactionRequestDto.getTransactionAmount()) {
            Optional<User> userOptional = userRepository.findByPhoneNumber(transactionRequestDto.getReceiverPhoneNumber());
            if (userOptional.isPresent()) {
                User receiverUser = userOptional.get();
                Transaction transaction = new Transaction();
                transaction.setTransactionValue(transactionRequestDto.getTransactionAmount());
                transaction.setSenderUser(user);
                transaction.setReceiverUser(receiverUser);
                Date date = new Date();
                transaction.setTransactionStartTime(new Timestamp(date.getTime()));
                transaction.setTransactionStatus(TransactionStatus.PENDING);
                double currentBalance = user.getCurrentBalance();
                user.setCurrentBalance(currentBalance - transactionRequestDto.getTransactionAmount());
                return ResponseEntity.ok(TransactionResponseDto.getTransactionDto(transactionRepository.save(transaction)));
            } else {
                SmsSubmissionResponse response = messageService.sendInvitationSMS(transactionRequestDto.getReceiverPhoneNumber());
                if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
                    return ResponseEntity.ok(new ServerResponse(false, "User not registered to the system. Invitation link has been sent to the mobile number" ));
                } else {
                    return new ResponseEntity<>(new ServerResponse(false, "User not registered, Invitation link cannot be sent." ), HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }
        } else {
            return new ResponseEntity<>(new ServerResponse(false, "Insufficient balance" ), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<?> transactionDecision(User receiverUser, TransactionDecisionRequestDto transactionDecisionRequestDto) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionDecisionRequestDto.getIdTransaction());
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            if (transaction.getReceiverUser().getIdUser().toString().equals(receiverUser.getIdUser().toString())) {
                if (transactionDecisionRequestDto.isAccept()) {
                    transaction.setTransactionStatus(TransactionStatus.COMPLETED);
                    double currentBalance = receiverUser.getCurrentBalance();
                    receiverUser.setCurrentBalance(currentBalance+transaction.getTransactionValue());
                    userRepository.save(receiverUser);
                    transaction = transactionRepository.save(transaction);
                    return ResponseEntity.ok(TransactionResponseDto.getTransactionDto(transaction));
                } else {
                    transaction.setTransactionStatus(TransactionStatus.REJECTED);
                    User senderUser = transaction.getSenderUser();
                    double currentSenderBalance = senderUser.getCurrentBalance();
                    senderUser.setCurrentBalance(currentSenderBalance+transaction.getTransactionValue());
                    userRepository.save(senderUser);
                    transaction = transactionRepository.save(transaction);
                    return ResponseEntity.ok(TransactionResponseDto.getTransactionDto(transaction));
                }
            } else {
                return new ResponseEntity<>(new ServerResponse(false, "This transaction doesn't belong to you"), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ServerResponse(false,"No record of transaction found"), HttpStatus.NOT_FOUND);
        }
    }

}
