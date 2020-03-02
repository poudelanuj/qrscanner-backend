package com.anuj.qrscanner.model.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionResponseDto {
    @JsonProperty("data")
    private TransactionResponseData transactionResponseData;
}
