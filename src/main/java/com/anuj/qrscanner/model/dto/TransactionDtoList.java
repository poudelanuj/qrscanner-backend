package com.anuj.qrscanner.model.dto;

import com.anuj.qrscanner.model.dto.response.TransactionResponseDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDtoList {
    @JsonProperty("sent_transaction")
    private List<TransactionResponseDto> sentTransactionList;
    @JsonProperty("received_transaction")
    private List<TransactionResponseDto> receivedTransacton;
}
