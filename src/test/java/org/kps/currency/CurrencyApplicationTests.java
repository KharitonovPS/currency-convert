package org.kps.currency;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.kps.currency.client.CurrencyClientImpl;
import org.kps.currency.client.CurrencyClientTestImpl;
import org.kps.currency.domain.CurrencyEntity;
import org.kps.currency.domain.dto.CurrencyRequestDTOConvertImpl;
import org.kps.currency.domain.dto.CurrencyRequestDTOGetListImpl;
import org.kps.currency.domain.dto.CurrencyResponseDTO;
import org.kps.currency.repository.CurrencyRepo;
import org.kps.currency.service.CurrencyConverterService;
import org.kps.currency.service.SchemaInitService;
import org.kps.currency.service.SchemaUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@RequiredArgsConstructor
class CurrencyApplicationTests extends AbstractIntegrationServiceTest {

    @LocalServerPort
    private int port;

    @Autowired
    CurrencyRepo repo;

    @Autowired
    CurrencyConverterService service;

    @Autowired
    CurrencyClientImpl client;

    CurrencyClientTestImpl clientTest;

    @Autowired
    SchemaUpdateService updateService;

    @Autowired
    SchemaInitService initService;

    @BeforeEach
    void setUp() {
        repo.deleteAll();

        log.info("Preloading " + repo.save(new CurrencyEntity(1L, "USD", 840, "US Dollar", new BigDecimal("1.09520028"), Instant.now())));
        log.info("Preloading " + repo.save(new CurrencyEntity(2L, "EUR", 978, "Euro", new BigDecimal("0.999999995661"), Instant.now())));
        log.info("Preloading " + repo.save(new CurrencyEntity(3L, "RUB", 643, "Russian Ruble", new BigDecimal("96.33381334319916"), Instant.now())));
        log.info("Preloading " + repo.save(new CurrencyEntity(4L, "UAH", 980, "Ukrainian Hryvnia", new BigDecimal("41.39568254086164"), Instant.now())));
        log.info("Preloading " + repo.save(new CurrencyEntity(5L, "JPY", 392, "Japanese Yen", new BigDecimal("158.55104933532"), Instant.now())));

        if (clientTest == null) {
            clientTest = new CurrencyClientTestImpl(port, 1);
        }
    }

    @AfterAll
    static void afterAll() {
        POSTGRES.stop();
    }

    @Test
    void initializeRepo() {
        assertEquals(5, repo.count());
    }

    @Test
    void shouldGetCurrencyByCode() {
        CurrencyEntity entity = repo.findByCharCode("USD").orElseThrow();

        assertEquals(840, entity.getNumCode());
        assertEquals("US Dollar", entity.getName());
    }

    @Test
    @Disabled
    @Transactional
    void shouldUpdateRateAndTimestamp() {
        CurrencyEntity entity = repo.findByCharCode("UAH").orElseThrow();
        log.info("Before update: {}", entity.getRate());
        repo.updateRateByCharCode("UAH", new BigDecimal(1), Instant.now());

        CurrencyEntity entity2 = repo.findByCharCode("UAH").orElseThrow();
        log.info("After update: {}", entity2.getRate());
        assertEquals(new BigDecimal(1), entity2.getRate());
    }

    @Test
    void shouldGetListOfCurrencyFromController() {
        CurrencyRequestDTOGetListImpl dto = new CurrencyRequestDTOGetListImpl();
        dto.setQuote("USD");
        ResponseEntity<List<CurrencyResponseDTO>> listForQuote = clientTest.getListForQuote(dto);
        log.info(String.valueOf(listForQuote));
        assertEquals(5, Objects.requireNonNull(listForQuote.getBody()).size());
    }

    @Test
    void shouldConvertCurrency() {
        CurrencyRequestDTOConvertImpl dto = new CurrencyRequestDTOConvertImpl();
        dto.setQuote("RUB");
        dto.setBase("USD");
        dto.setValue(1000L);

        ResponseEntity<String> value = clientTest.convert(dto);
        log.info(value.toString());
        assertTrue(Objects.requireNonNull(value.getBody()).contains("87960"));
    }

}
