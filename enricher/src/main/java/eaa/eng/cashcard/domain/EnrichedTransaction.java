package eaa.eng.cashcard.domain;

public record EnrichedTransaction(Long id, CashCard cashCard, ApprovalStatus approvalStatus, CardHolderData cardHolderData) {
}
