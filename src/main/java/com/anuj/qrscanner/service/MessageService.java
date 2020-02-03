package com.anuj.qrscanner.service;

import com.anuj.qrscanner.model.db.VerificationToken;
import com.anuj.qrscanner.payload.ServerResponse;
import com.nexmo.client.HttpConfig;
import com.nexmo.client.NexmoClient;
import com.nexmo.client.sms.MessageStatus;
import com.nexmo.client.sms.SmsSubmissionResponse;
import com.nexmo.client.sms.messages.TextMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    HttpConfig httpConfig = HttpConfig.defaultConfig();
    NexmoClient client = NexmoClient
                            .builder()
                            .apiKey("652b9a56")
                            .apiSecret("0oSFuscWWKu2B9Ym")
                            .build();


    public ResponseEntity<?> sendMessageWithVerificationCode(VerificationToken verificationToken){

        SmsSubmissionResponse response = sendSMS(verificationToken);
        if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
            return ResponseEntity.ok(new ServerResponse(true, "Verification Token Sent successfully"));
        } else {
            return new ResponseEntity<>(new ServerResponse(false, "Please Try again later"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public SmsSubmissionResponse sendSMS(VerificationToken verificationToken){
        TextMessage message = new TextMessage("QR Wallet",
                verificationToken.getUser().getPhoneNumber(),
                "Verification Token: "+ verificationToken.getToken()
        );
        return  client.getSmsClient().submitMessage(message);
    }

    public SmsSubmissionResponse sendInvitationSMS(String receiverPhoneNumber){
        TextMessage message = new TextMessage("QR Wallet",
                receiverPhoneNumber,
                "Invitation Link: Please install QR wallet to receive digital cash"
        );
        return  client.getSmsClient().submitMessage(message);
    }




}
