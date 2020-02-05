package com.anuj.qrscanner.controller;

import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.model.dto.request.TransactionDecisionRequestDto;
import com.anuj.qrscanner.model.dto.request.TransactionInitiateDto;
import com.anuj.qrscanner.model.dto.request.TransactionRequestDto;
import com.anuj.qrscanner.model.dto.response.TransactionListResponseDto;
import com.anuj.qrscanner.model.dto.response.TransactionResponseDto;
import com.anuj.qrscanner.payload.ErrorResponse;
import com.anuj.qrscanner.payload.LoginResponse;
import com.anuj.qrscanner.payload.ValidationError;
import com.anuj.qrscanner.service.TransactionService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import static com.anuj.qrscanner.constant.Urls.BASE_URL;

@RestController
@RequestMapping(BASE_URL)
public class TransactionController {

    @Autowired
    TransactionService transactionService;

    @ApiOperation(value = "Get All transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transaction list", response = TransactionListResponseDto.class),
    })
    @GetMapping(value = "/transactions")
    public ResponseEntity<?> getTransactions(@ApiIgnore User user) {
        return transactionService.getAllTransaction(user);
    }

    @ApiOperation(value = "Create new transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "New Transaction created successfully", response = TransactionResponseDto.class),
            @ApiResponse(code = 404, message = "User not registered to the system. Invitation link has been sent to the mobile number", response = ErrorResponse.class),
            @ApiResponse(code = 406, message = "Insufficient balance", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "User not registered, Invitation link cannot be sent.", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Invalid Mpin", response = ErrorResponse.class)
    })
    @PostMapping(value = "/transaction")
    public ResponseEntity<?> postTransactionDto(@ApiIgnore User user, @RequestBody TransactionInitiateDto transactionInitiateDto) {
        if (user.getMpin().equals(transactionInitiateDto.getMpin())) {
            return transactionService.createTransaction(user, transactionInitiateDto);
        } else {
            return new ResponseEntity<>(new ErrorResponse("Invalid Mpin", new ValidationError()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Transaction Decision From Receiver")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transaction accept/reject response", response = TransactionResponseDto.class),
            @ApiResponse(code = 403, message = "Transaction already closed", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "This transaction doesn't belong to you", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "No record of transaction found", response = ErrorResponse.class)

    })
    @PostMapping(value = "/receiver_transaction_decision")
    public ResponseEntity<?> receiverTransactionDecision(@ApiIgnore User user, @RequestBody TransactionDecisionRequestDto transactionDecisionRequestDto) {
        return transactionService.receiverTransactionDecision(user, transactionDecisionRequestDto);
    }

    @ApiOperation(value = "Request new transaction")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transaction request successfully sent.", response = TransactionResponseDto.class),
            @ApiResponse(code = 404, message = "User not registered to the system. Invitation link has been sent to the mobile number", response = ErrorResponse.class),
            @ApiResponse(code = 500, message = "User not registered, Invitation link cannot be sent.", response = ErrorResponse.class),
            @ApiResponse(code = 401, message = "Invalid Mpin", response = ErrorResponse.class)
    })
    @PostMapping(value = "/request_transaction")
    public ResponseEntity<?> requestTransaction(@ApiIgnore User requestingUser, @RequestBody TransactionRequestDto transactionRequestDto) {
        if (requestingUser.getMpin().equals(transactionRequestDto.getMpin())) {
            return transactionService.requestTransaction(requestingUser, transactionRequestDto);
        } else {
            return new ResponseEntity<>(new ErrorResponse("Invalid Mpin", new ValidationError()), HttpStatus.BAD_REQUEST);
        }
    }

    @ApiOperation(value = "Transaction Decision From Sender")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Transaction accept/reject response", response = TransactionResponseDto.class),
            @ApiResponse(code = 403, message = "Transaction already closed", response = ErrorResponse.class),
            @ApiResponse(code = 400, message = "This transaction doesn't belong to you", response = ErrorResponse.class),
            @ApiResponse(code = 406, message = "Insufficient balance", response = ErrorResponse.class),
            @ApiResponse(code = 404, message = "No record of transaction found", response = ErrorResponse.class)

    })
    @PostMapping(value = "/sender_transaction_decision")
    public ResponseEntity<?> senderTransactionDecision(@ApiIgnore User user, @RequestBody TransactionDecisionRequestDto transactionDecisionRequestDto) {
        return transactionService.senderTransactionDecision(user, transactionDecisionRequestDto);
    }
}
