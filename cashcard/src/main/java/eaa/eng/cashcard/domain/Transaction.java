package eaa.eng.cashcard.domain;

public record Transaction(
        Long id,
        CashCard cashCard
) {}
