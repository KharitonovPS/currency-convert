package org.kps.currency.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.kps.currency.client.CurrencyClientImpl;
import org.kps.currency.repository.CurrencyRepo;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Slf4j
public class SchemaInitService {

    private final CurrencyRepo repo;
    private final CurrencyClientImpl client;
    private final ExecutorService executorService;

    public SchemaInitService(
            CurrencyRepo repo,
            CurrencyClientImpl client
    ) {
        this.repo = repo;
        this.client = client;
        this.executorService = Executors.newSingleThreadExecutor();
    }


    @PostConstruct
    public void initSchemaOnStartup() {
        executorService.execute(this::bootData);
    }

    private void bootData() {
        if (repo.count() == 0) {
            try {
                log.info("\"currency_data\" is empty, send request to API...");
                repo.saveAll(client.getDataFromAPI());
                log.info("Data was save in \"currency_data\" schema, current size is -> {}.",
                        repo.count());
                executorService.shutdown();
            } catch (RuntimeException e) {
                log.info("The client failed to request data from API.");
            }
        } else
            log.info("Values in \"currency_data\" schema already set, SchemaUpdateService will check it relevance");
    }
}
