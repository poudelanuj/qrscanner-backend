package com.anuj.qrscanner.model.dto.response;

import com.anuj.qrscanner.constant.TransactionStatus;
import com.anuj.qrscanner.constant.TransactionType;
import com.anuj.qrscanner.model.db.Transaction;
import com.anuj.qrscanner.model.db.User;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDto {
    @JsonProperty("id_transaction")
    private long idTransaction;
    @JsonProperty("transaction_value")
    private double transactionValue;
    @JsonProperty("transaction_status")
    private TransactionStatus transactionStatus;
    @JsonProperty("transaction_type")
    private TransactionType transactionType;
    @JsonProperty("own")
    private boolean own;
    @JsonProperty("sender_phone_number")
    private String senderPhoneNumber;
    @JsonProperty("receiver_phone_number")
    private String receiverPhoneNumber;
    @JsonProperty("transaction_start_time")
    private Date transactionStartDate;
    @JsonProperty("transaction_accept_time")
    private Date transactionAcceptDate;

    public static TransactionDto getTransactionDtoWithUser(Transaction transaction, User user){
        TransactionDto transactionDto = new TransactionDto();
        transactionDto.setIdTransaction(transaction.getIdTransaction());
        transactionDto.setTransactionValue(transaction.getTransactionValue());
        transactionDto.setTransactionStatus(transaction.getTransactionStatus());
        transactionDto.setTransactionType(transaction.getTransactionType());
        transactionDto.setSenderPhoneNumber(transaction.getSourceUser().getPhoneNumber());
        transactionDto.setReceiverPhoneNumber(transaction.getDestinationUser().getPhoneNumber());
        transactionDto.setTransactionStartDate(transaction.getTransactionStartDate());
        transactionDto.setTransactionAcceptDate(transaction.getTransactionAcceptDate());
        if(transaction.getSourceUser().getPhoneNumber().equals(user.getPhoneNumber())){
            transactionDto.setOwn(true);
        }else {
            transactionDto.setOwn(false);
        }
        return transactionDto;

    }

}
