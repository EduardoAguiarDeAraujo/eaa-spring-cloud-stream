package eaa.eng.cashcard.service;

import eaa.eng.cashcard.domain.ApprovalStatus;
import eaa.eng.cashcard.domain.CardHolderData;
import eaa.eng.cashcard.domain.EnrichedTransaction;
import eaa.eng.cashcard.domain.Transaction;

import java.util.UUID;

public class EnrichmentService {
    public EnrichedTransaction enrichTransaction(Transaction transaction) {
        return new EnrichedTransaction(
                transaction.id(),
                transaction.cashCard(),
                ApprovalStatus.APPROVED,
                new CardHolderData(UUID.randomUUID(),
                        transaction.cashCard().owner(),
                        "123 Main street"));
    }
}
