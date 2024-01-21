package org.kps.currency.controller;

import jakarta.validation.Valid;
import org.kps.currency.domain.dto.CurrencyRequestDTOConvertImpl;
import org.kps.currency.domain.dto.CurrencyRequestDTOGetListImpl;
import org.kps.currency.domain.dto.CurrencyResponseDTO;
import org.kps.currency.service.CurrencyConverterService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
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

    @PostMapping("/pairs")
    public ResponseEntity<List<CurrencyResponseDTO>> getListForQuote(
            @RequestBody @Valid CurrencyRequestDTOGetListImpl dto
    ) {
        return currencyConverterService.getAllRatesForQuote(dto);
    }

    @PostMapping("/convert")
    public ResponseEntity<String> convert(
            @RequestBody @Valid CurrencyRequestDTOConvertImpl dto
    ) {
        return currencyConverterService.getRateForQuote(dto);
    }
}
