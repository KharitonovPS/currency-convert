package org.kps.currency.controller;

import jakarta.validation.Valid;
import org.kps.currency.domain.dto.CurrencyRequestConvertDTO;
import org.kps.currency.domain.dto.CurrencyRequestGetAllDTO;
import org.kps.currency.domain.dto.CurrencyResponseDTO;
import org.kps.currency.service.CurrencyConverterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class CurrencyController {

    private final CurrencyConverterService currencyConverterService;

    public CurrencyController(CurrencyConverterService currencyConverterService) {
        this.currencyConverterService = currencyConverterService;
    }

    @GetMapping("/pairs")
    public ResponseEntity<List<CurrencyResponseDTO>> getListOfPairs(
            @RequestBody @Valid CurrencyRequestGetAllDTO dto
    ) {
        return currencyConverterService.getAllRatesForQuote(dto);
    }

    @GetMapping("/convert")
    public ResponseEntity<String> convert(
            @RequestBody @Valid CurrencyRequestConvertDTO dto
    ) {
        return currencyConverterService.getRateForQuote(dto);
    }
}
