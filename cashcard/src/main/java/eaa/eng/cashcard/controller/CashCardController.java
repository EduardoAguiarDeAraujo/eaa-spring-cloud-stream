package eaa.eng.cashcard.controller;

import eaa.eng.cashcard.domain.Transaction;
import eaa.eng.cashcard.ondemand.CashCardTransactionOnDemand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CashCardController {

    private final CashCardTransactionOnDemand cashCardTransactionOnDemand;

    public CashCardController(@Autowired CashCardTransactionOnDemand cashCardTransactionOnDemand) {
        this.cashCardTransactionOnDemand = cashCardTransactionOnDemand;
    }

    @PostMapping(path = "/publish/txn")
    public void publishTxn(@RequestBody Transaction transaction) {
        this.cashCardTransactionOnDemand.publishOnDemand(transaction);
        System.out.println("POST for Transaction: " + transaction);
    }

}
