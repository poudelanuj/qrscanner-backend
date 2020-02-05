package com.anuj.qrscanner.component;

import com.anuj.qrscanner.constant.TransactionStatus;
import com.anuj.qrscanner.constant.TransactionType;
import com.anuj.qrscanner.model.db.Transaction;
import com.anuj.qrscanner.model.db.User;
import com.anuj.qrscanner.repository.TransactionRepository;
import com.anuj.qrscanner.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Component
public class TransactionScheduler {

    @Autowired
    TransactionRepository transactionRepository;
    @Autowired
    UserRepository userRepository;

    @Scheduled(fixedRate = 1000*60*60)
    public void transactionPendingStatusCheck() {
        List<Transaction> pendingTransactionList = transactionRepository.findAllByTransactionStatus(TransactionStatus.PENDING);
        System.out.println("Scheduler is scheduling now");

        for(Transaction transaction : pendingTransactionList){
            Date transactionStartDate = transaction.getTransactionStartDate();
            System.out.println("Pending Found");
            Calendar c = Calendar.getInstance();
            c.setTime(transactionStartDate);
            c.add(Calendar.MINUTE, 1);
            Date transactionExpireDate = c.getTime();
            if(transactionExpireDate.before(Date.from(Instant.now()))){
                //todo remove pending transaction
                System.out.println("Expired");

                transaction.setTransactionStatus(TransactionStatus.EXPIRED);
                if(transaction.getTransactionType()== TransactionType.SENT){
                    User sourceUser = transaction.getSourceUser();
                    double currentBalance = transaction.getSourceUser().getCurrentBalance();
                    sourceUser.setCurrentBalance(currentBalance+ transaction.getTransactionValue());
                    userRepository.save(sourceUser);
                }
                transactionRepository.save(transaction);
            }
        }

    }

}
