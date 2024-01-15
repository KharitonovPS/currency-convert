package org.kps.currency.service;

import org.kps.currency.domain.CurrencyEntity;
import org.kps.currency.domain.dto.CurrencyRequestConvertDTO;
import org.kps.currency.domain.dto.CurrencyRequestGetAllDTO;
import org.kps.currency.domain.dto.CurrencyResponseDTO;
import org.kps.currency.mapper.CurrencyResponseMapper;
import org.kps.currency.repository.CurrencyRepo;
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

    public ResponseEntity<String> getRateForQuote(CurrencyRequestConvertDTO dto) {
        CurrencyEntity base = repo.findByCharCode(dto.getBase()).orElseThrow();
        CurrencyEntity quote = repo.findByCharCode(dto.getQuote()).orElseThrow();

        BigDecimal coefficient = new BigDecimal(1);
        BigDecimal multiplyValue = new BigDecimal(dto.getValue());

        BigDecimal dividend = coefficient.divide(base.getRate(), 8, RoundingMode.HALF_UP);
        BigDecimal usdRate = dividend.multiply(multiplyValue);

        BigDecimal result = quote.getRate().multiply(usdRate);
        BigDecimal res1 = result.divide(BigDecimal.valueOf(1), 2, RoundingMode.HALF_UP);

        return ResponseEntity.ok(res1.stripTrailingZeros().toPlainString());
    }

    public ResponseEntity<List<CurrencyResponseDTO>> getAllRatesForQuote(CurrencyRequestGetAllDTO dto) {

        List<CurrencyEntity> entities = repo.findAll();

        CurrencyEntity requestedEntity = entities
                .stream()
                .filter(entity -> entity.getCharCode().equals(dto.getQuote()))
                .findFirst()
                .orElseThrow();

        BigDecimal coefficient = new BigDecimal(1);
        BigDecimal usdRate = coefficient.divide(requestedEntity.getRate(), 8, RoundingMode.HALF_UP);

        List<CurrencyResponseDTO> responseDTOS = entities
                .stream()
                .peek(entity -> entity
                        .setRate(
                                (usdRate.multiply(entity.getRate()))
                        ))
                .map(mapper::entityToResponseDTO)
                .sorted(Comparator.comparing(CurrencyResponseDTO::getCharCode))
                .toList();

        return new ResponseEntity<>(responseDTOS, HttpStatus.OK);
    }
}
