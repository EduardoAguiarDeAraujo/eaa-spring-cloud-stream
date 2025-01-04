package eaa.eng.cashcard.enricher;

import com.fasterxml.jackson.databind.ObjectMapper;
import eaa.eng.cashcard.domain.CashCard;
import eaa.eng.cashcard.domain.Transaction;
import eaa.eng.cashcard.service.EnrichmentService;
import eaa.eng.cashcard.domain.ApprovalStatus;
import eaa.eng.cashcard.domain.CardHolderData;
import eaa.eng.cashcard.domain.EnrichedTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.binder.test.InputDestination;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;

import java.util.UUID;
import static org.mockito.BDDMockito.given;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Import(TestChannelBinderConfiguration.class)
public class CashCardTransactionEnricherTests {

    @MockBean
    private EnrichmentService enrichmentService;

    @Test
    void enrichmentServiceShouldAddDataToTransactions(
            @Autowired InputDestination inputDestination,
            @Autowired OutputDestination outputDestination) throws IOException {

        Transaction transaction = new Transaction(1L, new CashCard(123L, "Kumar Patel", 1.00));

        EnrichedTransaction enrichedTransaction = new EnrichedTransaction(
                transaction.id(),
                transaction.cashCard(),
                ApprovalStatus.APPROVED,
                new CardHolderData(UUID.randomUUID(), transaction.cashCard().owner(), "123 Main Street"));
        given(enrichmentService.enrichTransaction(transaction)).willReturn(enrichedTransaction);

        Message<Transaction> message = MessageBuilder.withPayload(transaction).build();
        inputDestination.send(message, "enrichTransaction-in-0");

        Message<byte[]> result = outputDestination.receive(5000, "enrichTransaction-out-0");
        assertThat(result).isNotNull();

        ObjectMapper objectMapper = new ObjectMapper();
        EnrichedTransaction receivedData = objectMapper.readValue(result.getPayload(), EnrichedTransaction.class);
        assertThat(receivedData).isEqualTo(enrichedTransaction);

    }

    @SpringBootApplication
    public static class App {

    }
}
