package com.anuj.qrscanner.model.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionDecisionRequestDto {
    @JsonProperty("id_transaction")
    private Long idTransaction;
    @JsonProperty("accept")
    private boolean accept;
}
