package eaa.eng.cashcard.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import eaa.eng.cashcard.domain.CashCard;
import eaa.eng.cashcard.domain.Transaction;
import eaa.eng.cashcard.ondemand.CashCardTransactionOnDemand;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.stream.binder.test.OutputDestination;
import org.springframework.cloud.stream.binder.test.TestChannelBinderConfiguration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Import({TestChannelBinderConfiguration.class, CashCardTransactionOnDemand.class})
public class CashCardControllerTests {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void postShouldSendTransactionAsAMessage(@Autowired OutputDestination outputDestination) throws IOException {
        Transaction postedTransaction = new Transaction(123L, new CashCard(1L, "kumar2", 1.00));
        ResponseEntity<Transaction> response = this.restTemplate.postForEntity(
                "http://localhost:" + port + "/publish/txn",
                postedTransaction, Transaction.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        Message<byte[]> result = outputDestination.receive(5000, "approvalRequest-out-0");
        assertThat(result).isNotNull();
        ObjectMapper objectMapper = new ObjectMapper();
        Transaction transactionFromMessage = objectMapper.readValue(result.getPayload(), Transaction.class);
        assertThat(transactionFromMessage.id()).isEqualTo(postedTransaction.id());

    }

    @SpringBootApplication
    public static class App {

    }

}
