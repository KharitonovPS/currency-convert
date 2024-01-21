package org.kps.currency;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.*;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doNothing;

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

    @Autowired
    private EntityManager entityManager;
    CurrencyClientTestImpl clientTest;


    @MockBean
    SchemaUpdateService updateService;

    @MockBean
    SchemaInitService initService;

    private static final CurrencyEntity USD = new CurrencyEntity(1L, "USD", 840,
            "US Dollar", new BigDecimal("1.0952002800"), Instant.now());
    private static final CurrencyEntity EUR = new CurrencyEntity(2L, "EUR", 978,
            "Euro", new BigDecimal("0.999999995661"), Instant.now());

    @BeforeEach
    void setUp() {
        repo.deleteAll();
        log.info("Preloading " + repo.save(USD));
        log.info("Preloading " + repo.save(EUR));

        if (clientTest == null) {
            clientTest = new CurrencyClientTestImpl(port, 1);
        }
        doNothing().when(initService).initSchemaOnStartup();
        doNothing().when(updateService).checkForUpdate();

    }

    @AfterAll
    static void afterAll() {
        POSTGRES.stop();
    }

    @Test
    @Tag("repository")
    void initializeRepo() {
        List<CurrencyEntity> all = repo.findAll();
        assertAll(
                () -> assertThat(repo).isNotNull(),
                () -> assertThat(all).hasSize(2),
                () -> assertThat(all).usingElementComparatorIgnoringFields("lastModifiedAt", "rate")
                        .containsExactlyInAnyOrder(USD, EUR)
        );
    }

    @Test
    @Tag("repository")
    void shouldGetCurrencyByCode() {
        CurrencyEntity entity = repo.findByCharCode("USD").orElseThrow();

        assertAll(
                () -> assertThat(entity).isNotNull(),
                () -> assertThat(entity.getNumCode()).isEqualTo(840),
                () -> assertThat(entity.getName()).isEqualTo("US Dollar")
        );

    }

    //TODO fix bug, fail if start all at once, pass if start single
    @Test
    @Transactional
    @Disabled
    @Tag("repository")
    void shouldUpdateRateAndTimestamp() {
        repo.updateRateByCharCode("USD", BigDecimal.ONE, Instant.now());
        entityManager.clear();
        CurrencyEntity currency = repo.findByCharCode("USD").orElseThrow();
        assertAll(
                () -> assertThat(currency.getRate().stripTrailingZeros()).isEqualTo("1"),
                () -> assertThat(currency.getLastModifiedAt()).isGreaterThan(USD.getLastModifiedAt())
        );
    }

    @Test
    @Tag("controller")
    void shouldGetListOfCurrencyFromController() {
        CurrencyRequestDTOGetListImpl dto = new CurrencyRequestDTOGetListImpl();
        dto.setQuote("USD");

        ResponseEntity<List<CurrencyResponseDTO>> listForQuote = clientTest.getListForQuote(dto);

        assertAll(
                () -> assertThat(listForQuote).isNotNull(),
                () -> assertThat(listForQuote.getBody()).isNotEmpty(),
                () -> assertThat(listForQuote.getBody()).hasSize(2),
                () -> assertThat(listForQuote.getStatusCode().toString()).isEqualTo("200 OK")
        );
    }

    @Test
    @Tag("controller")
    void shouldConvertCurrency() {
        CurrencyRequestDTOConvertImpl dto = new CurrencyRequestDTOConvertImpl();
        dto.setQuote("EUR");
        dto.setBase("USD");
        dto.setValue(1000L);

        ResponseEntity<String> value = clientTest.convert(dto);

        assertAll(
                () -> assertThat(value).isNotNull(),
                () -> assertThat(value.getStatusCode().toString()).isEqualTo("200 OK"),
                () -> assertThat(value.getBody()).isEqualTo("913.07")
        );
    }

    @Test
    @Tag("controller")
    void throwExceptionIfISOCodeIsNotValid() {
        CurrencyRequestDTOGetListImpl dto = new CurrencyRequestDTOGetListImpl();
        dto.setQuote("ZZZ");

        assertAll(
                () -> {
                    RuntimeException validationEx = assertThrows(
                            RuntimeException.class, () -> clientTest.getListForQuote(dto)
                    );
                    assertTrue(validationEx.getMessage().contains("The input must be CodeISO"));
                },
                () -> assertThrows(RuntimeException.class, () -> clientTest.getListForQuote(null))
        );
    }
}
