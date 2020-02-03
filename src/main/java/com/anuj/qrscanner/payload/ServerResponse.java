package com.anuj.qrscanner.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServerResponse {
    private boolean success;
    private String message;
}