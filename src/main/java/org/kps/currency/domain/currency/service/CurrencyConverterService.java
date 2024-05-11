package org.kps.currency.domain.currency.service;

import org.kps.currency.domain.currency.dto.CurrencyRequestDTOConvertImpl;
import org.kps.currency.domain.currency.dto.CurrencyRequestDTOGetListImpl;
import org.kps.currency.domain.currency.dto.CurrencyResponseDTO;
import org.kps.currency.domain.currency.entity.CurrencyEntity;
import org.kps.currency.domain.currency.mapper.CurrencyResponseMapper;
import org.kps.currency.domain.currency.repository.CurrencyRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Comparator;
import java.util.List;

@Service
public class CurrencyConverterService {

    private final CurrencyRepo repo;
    private final CurrencyResponseMapper mapper;

    public CurrencyConverterService(
            CurrencyRepo repo,
            CurrencyResponseMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    public ResponseEntity<String> getRateForQuote(CurrencyRequestDTOConvertImpl dto) {
        CurrencyEntity base = repo.findByCharCode(dto.getBase()).orElseThrow();
        CurrencyEntity quote = repo.findByCharCode(dto.getQuote()).orElseThrow();

        BigDecimal coefficient = BigDecimal.ONE;

        BigDecimal usdRate = coefficient.divide(base.getRate(), 8, RoundingMode.HALF_UP)
                .multiply(new BigDecimal(dto.getValue()));

        BigDecimal result = quote.getRate()
                .multiply(usdRate).setScale(2, RoundingMode.HALF_UP);

        return ResponseEntity.ok(result.toPlainString());
    }

    public ResponseEntity<List<CurrencyResponseDTO>> getAllRatesForQuote(
            CurrencyRequestDTOGetListImpl dto
    ) {

        List<CurrencyEntity> entities = repo.findAll();

        CurrencyEntity requestedEntity = entities
                .stream()
                .filter(entity -> entity.getCharCode().equals(dto.getQuote()))
                .findFirst()
                .orElseThrow();

        BigDecimal usdRate = BigDecimal.ONE.divide(requestedEntity.getRate(), 8, RoundingMode.HALF_UP);

        List<CurrencyResponseDTO> responseDTOS = entities
                .stream()
                .peek(entity -> entity
                        .setRate(usdRate.multiply(entity.getRate())
                                .setScale(2, RoundingMode.HALF_UP)
                        ))
                .map(mapper::entityToResponseDTO)
                .sorted(Comparator.comparing(CurrencyResponseDTO::getCharCode))
                .toList();

        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }
}
