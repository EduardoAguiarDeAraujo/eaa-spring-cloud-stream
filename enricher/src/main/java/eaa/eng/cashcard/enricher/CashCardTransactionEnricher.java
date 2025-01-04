package eaa.eng.cashcard.enricher;

import eaa.eng.cashcard.domain.EnrichedTransaction;
import eaa.eng.cashcard.domain.Transaction;
import eaa.eng.cashcard.service.EnrichmentService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Function;

@Configuration
public class CashCardTransactionEnricher {

    @Bean
    EnrichmentService enrichmentService(){
        return new EnrichmentService();
    }

    @Bean
    public Function<Transaction, EnrichedTransaction> enrichTransaction(EnrichmentService enrichmentService) {
        return transaction -> {
            return enrichmentService.enrichTransaction(transaction);
        };
    }

}
