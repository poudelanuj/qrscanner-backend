package com.anuj.qrscanner.model.dto;

import com.anuj.qrscanner.model.dto.response.TransactionDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionListResponseData {
    @JsonProperty("transactions")
    private List<TransactionDto> transactionDtoList;

}
