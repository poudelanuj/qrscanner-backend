package com.anuj.qrscanner.model.dto.response;

import com.anuj.qrscanner.constant.TransactionStatus;
import com.anuj.qrscanner.constant.TransactionType;
import com.anuj.qrscanner.model.db.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    @JsonProperty("id_transaction")
    private long idTransaction;
    @JsonProperty("transaction_value")
    private double transactionValue;
    @JsonProperty("transaction_status")
    private TransactionStatus transactionStatus;
    @JsonProperty("transaction_type")
    private TransactionType transactionType;
    @JsonProperty("sender_phone_number")
    private String senderPhoneNumber;
    @JsonProperty("receiver_phone_number")
    private String receiverPhoneNumber;
    @JsonProperty("transaction_start_time")
    private Date transactionStartDate;
    @JsonProperty("transaction_accept_time")
    private Date transactionAcceptDate;

    public static TransactionResponseDto getTransactionDto(Transaction transaction){
        TransactionResponseDto transactionDto = new TransactionResponseDto();
        transactionDto.setIdTransaction(transaction.getIdTransaction());
        transactionDto.setTransactionValue(transaction.getTransactionValue());
        transactionDto.setTransactionStatus(transaction.getTransactionStatus());
        transactionDto.setTransactionType(transaction.getTransactionType());
        transactionDto.setSenderPhoneNumber(transaction.getSourceUser().getPhoneNumber());
        transactionDto.setReceiverPhoneNumber(transaction.getDestinationUser().getPhoneNumber());
        transactionDto.setTransactionStartDate(transaction.getTransactionStartDate());
        transactionDto.setTransactionAcceptDate(transaction.getTransactionAcceptDate());
        return transactionDto;
    }

}
