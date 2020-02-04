package com.anuj.qrscanner.service;

import com.anuj.qrscanner.constant.TransactionStatus;
import com.anuj.qrscanner.constant.TransactionType;
import com.anuj.qrscanner.model.db.Transaction;
import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.model.dto.TransactionDtoList;
import com.anuj.qrscanner.model.dto.request.TransactionDecisionRequestDto;
import com.anuj.qrscanner.model.dto.request.TransactionInitiateDto;
import com.anuj.qrscanner.model.dto.request.TransactionRequestDto;
import com.anuj.qrscanner.model.dto.response.TransactionResponseDto;
import com.anuj.qrscanner.payload.ErrorResponse;
import com.anuj.qrscanner.payload.ServerResponse;
import com.anuj.qrscanner.payload.ValidationError;
import com.anuj.qrscanner.repository.TransactionRepository;
import com.anuj.qrscanner.repository.UserRepository;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
        List<Transaction> transactionSentList = transactionRepository.findAllBySourceUser(user);
        List<Transaction> transactionReceivedList = transactionRepository.findAllByDestinationUser(user);

        List<TransactionResponseDto> sentTransactionListDto = transactionSentList.stream().map(TransactionResponseDto::getTransactionDto).collect(Collectors.toList());
        List<TransactionResponseDto> receivedTransactionListDto = transactionReceivedList.stream().map(TransactionResponseDto::getTransactionDto).collect(Collectors.toList());

        TransactionDtoList transactionDtoList = new TransactionDtoList();
        transactionDtoList.setSentTransactionList(sentTransactionListDto);
        transactionDtoList.setReceivedTransacton(receivedTransactionListDto);

        return ResponseEntity.ok(transactionDtoList);
    }

    public ResponseEntity<?> createTransaction(User user, TransactionInitiateDto transactionInitiateDto) {
        if (user.getCurrentBalance() > transactionInitiateDto.getTransactionAmount()) {
            Optional<User> userOptional = userRepository.findByPhoneNumber(transactionInitiateDto.getReceiverPhoneNumber());
            if (userOptional.isPresent()) {
                User receiverUser = userOptional.get();
                Transaction transaction = new Transaction();
                transaction.setTransactionValue(transactionInitiateDto.getTransactionAmount());
                transaction.setSourceUser(user);
                transaction.setDestinationUser(receiverUser);
                transaction.setTransactionStartDate(Date.from(Instant.now()));
                transaction.setTransactionStatus(TransactionStatus.PENDING);
                transaction.setTransactionType(TransactionType.SENT);
                double currentBalance = user.getCurrentBalance();
                user.setCurrentBalance(currentBalance - transactionInitiateDto.getTransactionAmount());
                return ResponseEntity.ok(TransactionResponseDto.getTransactionDto(transactionRepository.save(transaction)));
            } else {
                return sendInvitation(transactionInitiateDto.getReceiverPhoneNumber());
            }
        } else {
            return new ResponseEntity<>(new ErrorResponse("Insufficient balance",new ValidationError()), HttpStatus.BAD_REQUEST);
        }
    }


    public ResponseEntity<?> receiverTransactionDecision(User receiverUser, TransactionDecisionRequestDto transactionDecisionRequestDto) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionDecisionRequestDto.getIdTransaction());
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            if (transaction.getDestinationUser().getIdUser().toString().equals(receiverUser.getIdUser().toString())) {
                if (transactionDecisionRequestDto.isAccept()) {
                    transaction.setTransactionStatus(TransactionStatus.COMPLETED);
                    double currentBalance = receiverUser.getCurrentBalance();
                    transaction.setTransactionAcceptDate(Date.from(Instant.now()));
                    receiverUser.setCurrentBalance(currentBalance + transaction.getTransactionValue());
                    userRepository.save(receiverUser);
                    transaction = transactionRepository.save(transaction);
                    return ResponseEntity.ok(TransactionResponseDto.getTransactionDto(transaction));
                } else {
                    transaction.setTransactionStatus(TransactionStatus.REJECTED);
                    User senderUser = transaction.getSourceUser();
                    double currentSenderBalance = senderUser.getCurrentBalance();
                    senderUser.setCurrentBalance(currentSenderBalance + transaction.getTransactionValue());
                    userRepository.save(senderUser);
                    transaction = transactionRepository.save(transaction);
                    return ResponseEntity.ok(TransactionResponseDto.getTransactionDto(transaction));
                }
            } else {
                return new ResponseEntity<>(new ErrorResponse("This transaction doesn't belong to you",new ValidationError()), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ErrorResponse("No record of transaction found",new ValidationError()), HttpStatus.NOT_FOUND);
        }
    }

    public ResponseEntity<?> requestTransaction(User requestingUser, TransactionRequestDto transactionRequestDto) {
        Optional<User> sourceUserOptional = userRepository.findByPhoneNumber(transactionRequestDto.getSenderPhoneNumber());
        if (sourceUserOptional.isPresent()) {
            User sourceUser = sourceUserOptional.get();
            Transaction transaction = new Transaction();
            transaction.setSourceUser(sourceUser);
            transaction.setDestinationUser(requestingUser);
            transaction.setTransactionValue(transactionRequestDto.getTransactionAmount());
            transaction.setTransactionType(TransactionType.REQUESTED);
            transaction.setTransactionStatus(TransactionStatus.PENDING);
            transaction.setTransactionStartDate(Date.from(Instant.now()));
            transaction = transactionRepository.save(transaction);
            return ResponseEntity.ok(TransactionResponseDto.getTransactionDto(transaction));
        } else {
            return sendInvitation(transactionRequestDto.getSenderPhoneNumber());
        }
    }

    public ResponseEntity<?> sendInvitation(String phoneNumber) {
        SmsSubmissionResponse response = messageService.sendInvitationSMS(phoneNumber);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            return ResponseEntity.ok(new ErrorResponse("User not registered to the system. Invitation link has been sent to the mobile number", new ValidationError()));
        } else {
            return new ResponseEntity<>(new ErrorResponse( "User not registered, Invitation link cannot be sent.", new ValidationError()), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> senderTransactionDecision(User senderUser, TransactionDecisionRequestDto transactionDecisionRequestDto) {
        Optional<Transaction> transactionOptional = transactionRepository.findById(transactionDecisionRequestDto.getIdTransaction());
        if (transactionOptional.isPresent()) {
            Transaction transaction = transactionOptional.get();
            if (transaction.getSourceUser().getIdUser().toString().equals(senderUser.getIdUser().toString())) {
                if (transaction.getTransactionValue() < senderUser.getCurrentBalance()) {
                    if (transactionDecisionRequestDto.isAccept()) {
                        transaction.setTransactionStatus(TransactionStatus.COMPLETED);
                        double currentBalance = senderUser.getCurrentBalance();
                        senderUser.setCurrentBalance(currentBalance - transaction.getTransactionValue());
                        userRepository.save(senderUser);
                        User receiverUser = transaction.getDestinationUser();
                        double receiverUserBalance = receiverUser.getCurrentBalance();
                        receiverUser.setCurrentBalance(receiverUserBalance + transaction.getTransactionValue());
                        userRepository.save(receiverUser);
                        transaction = transactionRepository.save(transaction);
                        return ResponseEntity.ok(TransactionResponseDto.getTransactionDto(transaction));
                    } else {
                        transaction.setTransactionStatus(TransactionStatus.REJECTED);
                        userRepository.save(senderUser);
                        transaction = transactionRepository.save(transaction);
                        return ResponseEntity.ok(TransactionResponseDto.getTransactionDto(transaction));
                    }
                } else {
                    return new ResponseEntity<>(new ErrorResponse("Insufficient balance",new ValidationError()), HttpStatus.BAD_REQUEST);
                }
            } else {
                return new ResponseEntity<>(new ErrorResponse("This transaction doesn't belong to you",new ValidationError() ), HttpStatus.BAD_REQUEST);
            }
        } else {
            return new ResponseEntity<>(new ErrorResponse("No record of transaction found", new ValidationError()), HttpStatus.NOT_FOUND);
        }
    }
}
