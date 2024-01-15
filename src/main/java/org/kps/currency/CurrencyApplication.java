package org.kps.currency;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@EnableAsync
@EnableScheduling
@SpringBootApplication
@EnableTransactionManagement
public class CurrencyApplication {


    public static void main(String[] args) {
        SpringApplication.run(CurrencyApplication.class, args);
    }
}
