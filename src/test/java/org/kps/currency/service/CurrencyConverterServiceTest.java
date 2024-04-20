//package org.kps.currency.service;
//
//import org.junit.jupiter.api.Disabled;
//import org.junit.jupiter.api.Tag;
//import org.junit.jupiter.api.Test;
//import org.junit.runner.RunWith;
//import org.kps.currency.domain.currency.service.CurrencyConverterService;
//import org.kps.currency.domain.currency.entity.CurrencyEntity;
//import org.kps.currency.domain.currency.dto.CurrencyRequestDTOConvertImpl;
//import org.kps.currency.domain.currency.dto.CurrencyRequestDTOGetListImpl;
//import org.kps.currency.domain.currency.repository.CurrencyRepo;
//import org.mockito.Mockito;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.test.context.junit4.SpringRunner;
//
//import java.math.BigDecimal;
//import java.time.Instant;
//import java.util.List;
//import java.util.Objects;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//@Tag("fast")
//@Tag("service")
//@RunWith(SpringRunner.class)
//@SpringBootTest
//class CurrencyConverterServiceTest {
//
//    @MockBean
//    private CurrencyRepo repo;
//
//    @Autowired
//    private CurrencyConverterService converterService;
//
//    @Test
//    @Disabled
//    void getRateForQuote() {
//        CurrencyRequestDTOConvertImpl dto = new CurrencyRequestDTOConvertImpl();
//        dto.setBase("RUB");
//        dto.setQuote("USD");
//        dto.setValue(1000L);
//
//        BigDecimal usdRate = new BigDecimal("1.09520028");
//        BigDecimal rubRate = new BigDecimal("96.33381334319916");
//
//        CurrencyEntity usdEntity = new CurrencyEntity(1L, "USD", 840, "US Dollar", usdRate, Instant.now());
//        CurrencyEntity rubEntity = new CurrencyEntity(3L, "RUB", 643, "Russian Ruble", rubRate, Instant.now());
//
//        Mockito.when(repo.findByCharCode("USD")).thenReturn(Optional.of(usdEntity));
//        Mockito.when(repo.findByCharCode("RUB")).thenReturn(Optional.of(rubEntity));
//
//        ResponseEntity<String> rateForQuote = converterService.getRateForQuote(dto);
//
//        assertAll(
//                () -> assertNotNull(rateForQuote),
//                () -> Mockito.verify(repo, Mockito.times(1)).findByCharCode("RUB"),
//                () -> assertEquals(HttpStatus.OK, rateForQuote.getStatusCode()),
//                () -> assertEquals("11.37", rateForQuote.getBody())
//        );
//    }
//
//    @Test()
//    @Disabled
//    void getRateForQuoteWithException() {
//        CurrencyRequestDTOConvertImpl dto = new CurrencyRequestDTOConvertImpl();
//        dto.setBase("RUB");
//        dto.setQuote("ZZZ");
//        dto.setValue(1000L);
//
//        CurrencyEntity usdEntity = new CurrencyEntity(1L, "USD", 840, "US Dollar", null, Instant.now());
//        CurrencyEntity rubEntity = new CurrencyEntity(3L, "RUB", 643, "Russian Ruble", null, Instant.now());
//
//        Mockito.when(repo.findByCharCode("USD")).thenReturn(Optional.of(usdEntity));
//        Mockito.when(repo.findByCharCode("RUB")).thenReturn(Optional.of(rubEntity));
//
//        ResponseEntity<String> rateForQuote = converterService.getRateForQuote(dto);
//
//        assertNotNull(rateForQuote);
//        Mockito.verify(repo, Mockito.times(1)).findByCharCode("RUB");
//        assertEquals(HttpStatus.BAD_REQUEST, rateForQuote.getStatusCode());
//
//    }
//
//    @Test
//    @Disabled
//    void getAllRatesForQuote() {
//        CurrencyRequestDTOGetListImpl dto = new CurrencyRequestDTOGetListImpl();
//        dto.setQuote("USD");
//        CurrencyEntity usdEntity = new CurrencyEntity(2L, "USD", 840, "US Dollar",
//                new BigDecimal("1.09520028"), Instant.now());
//
//        CurrencyEntity rubEntity = new CurrencyEntity(1L, "RUB", 643, "Russian Ruble",
//                new BigDecimal("96.33381334319916"), Instant.now());
//
//        Mockito.when(repo.findAll()).thenReturn(List.of(usdEntity, rubEntity));
//
//        var result = converterService.getAllRatesForQuote(dto);
//        Mockito.verify(repo, Mockito.times(1)).findAll();
//        assertEquals(HttpStatus.OK, result.getStatusCode());
//        assertEquals(2, Objects.requireNonNull(result.getBody()).size());
//    }
//}