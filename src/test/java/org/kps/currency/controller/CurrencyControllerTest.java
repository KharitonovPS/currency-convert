package org.kps.currency.controller;

import org.junit.jupiter.api.Test;
import org.kps.currency.domain.CurrencyEntity;
import org.kps.currency.domain.dto.CurrencyRequestGetAllDTO;
import org.kps.currency.domain.dto.CurrencyResponseDTO;
import org.kps.currency.mapper.CurrencyResponseMapper;
import org.kps.currency.service.CurrencyConverterService;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Currency;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurrencyConverterService service;

    @MockBean
    private CurrencyResponseMapper mapper;

    @Test
    void serviceShouldReturnListOfAllQuotes() throws Exception {
        CurrencyRequestGetAllDTO dto = new CurrencyRequestGetAllDTO();
        dto.setQuote("USD");

        CurrencyResponseDTO usdEntity = new CurrencyResponseDTO();
        usdEntity.setRate("1.09520028");
        usdEntity.setName("US Dollar");
        usdEntity.setNumCode(840);
        usdEntity.setCharCode("USD");
        usdEntity.setLastModifiedAt(Instant.now());

        List<CurrencyResponseDTO> mockResponse = Arrays.asList(usdEntity);

        when(service.getAllRatesForQuote(dto)).thenReturn(any());

        this.mockMvc.perform(get("/api/v1/pairs")
                .param("quote", "USD"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(contains("USD")
            ));
    }

    @Test
    void convert() {
    }
}