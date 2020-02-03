package com.anuj.qrscanner.model.dto;

import com.anuj.qrscanner.constant.TransactionType;
import com.anuj.qrscanner.model.db.Transaction;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    @JsonProperty("transaction_value")
    private double transactionValue;
    @JsonProperty("pending")
    private boolean pending;
    @JsonProperty("sender_phone_number")
    private String senderPhoneNumber;
    @JsonProperty("receiver_phone_number")
    private String receiverPhoneNumber;
    @JsonProperty("transaction_start_time")
    private Timestamp transactionStartTime;
    @JsonProperty("transaction_accept_time")
    private Timestamp transactionAcceptTime;

    public static TransactionDto getTransactionDto(Transaction transaction){
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setTransactionValue(transaction.getTransactionValue());
        transactionDto.setPending(transaction.isPending());
        transactionDto.setSenderPhoneNumber(transaction.getSerderUser().getPhoneNumber());
        transactionDto.setReceiverPhoneNumber(transaction.getReceiverUser().getPhoneNumber());
        transactionDto.setTransactionStartTime(transaction.getTransactionStartTime());
        transactionDto.setTransactionAcceptTime(transaction.getTransactionAcceptTime());
        return transactionDto;
    }

}
