package eaa.eng.cashcard.stream;

import eaa.eng.cashcard.domain.Transaction;
import eaa.eng.cashcard.service.DataSourceService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.function.Supplier;

@Configuration
public class CashCardTransactionStream {

    @Bean
    public Supplier<Transaction> approvalRequest(DataSourceService dataSource){
        return () -> {
            return dataSource.getData();
        };
    }

    // Add this bean
    @Bean
    public DataSourceService dataSourceService() {
        return new DataSourceService();
    }

}
