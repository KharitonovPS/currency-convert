package org.kps.currency.service;

import lombok.extern.slf4j.Slf4j;
import org.kps.currency.domain.currency.entity.CurrencyEntity;
import org.kps.currency.domain.currency.repository.CurrencyRepo;
import org.kps.currency.web.client.CurrencyClientImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
@Slf4j
@Transactional
public class SchemaUpdateService {

    private final CurrencyRepo repo;
    private final CurrencyClientImpl client;

    @Value("${update_service.fixed_delay}")
    private String fixedDelay;

    public SchemaUpdateService(
            CurrencyRepo repo,
            CurrencyClientImpl client
    ) {
        this.repo = repo;
        this.client = client;
    }


    @Scheduled(initialDelayString = "${update_service.initial_delay}", fixedDelayString = "${update_service.fixed_delay}")
    public void checkForUpdate() {

        log.info("Rates will be update every -> {} millis.", fixedDelay);

        Optional<CurrencyEntity> entity = repo.findById(new Random().nextLong(100));
        if (entity.isPresent() &&
                entity.get().getLastModifiedAt().isBefore(
                        Instant.now().minus(1, ChronoUnit.HOURS)
                )) {
            log.info("Need to update data, send request to API...");
            try {
                List<CurrencyEntity> currencyList = client.getDataFromAPI();
                for (CurrencyEntity currency : currencyList) {
                    repo.updateRateByCharCode(currency.getCharCode(), currency.getRate(), Instant.now());
                }
            } catch (RuntimeException exception) {
                log.error("Some trouble with API here -> {}, {}",
                        getClass(),
                        exception.getMessage()
                );
            }

        }
        if (repo.count() != 0 && !entity.get().getLastModifiedAt().isBefore(
                Instant.now().minus(1, ChronoUnit.HOURS)
        )) {
            log.info("Data in schema \"currency_data\" is valid");
        } else
            log.error("Data in schema \"currency_data\" is not valid");
    }
}

