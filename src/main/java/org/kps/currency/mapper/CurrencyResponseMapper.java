package org.kps.currency.mapper;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.kps.currency.domain.CurrencyEntity;
import org.kps.currency.domain.dto.CurrencyResponseDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Getter
@Setter
@ToString
@Component
public class CurrencyResponseMapper {

    public CurrencyResponseDTO entityToResponseDTO(CurrencyEntity entity) {
        CurrencyResponseDTO dto = new CurrencyResponseDTO();

        BigDecimal strippedValue = entity.getRate().stripTrailingZeros();
        dto.setRate(strippedValue.toPlainString());

        dto.setName(entity.getName());
        dto.setCharCode(entity.getCharCode());
        dto.setLastModifiedAt(Timestamp.from(entity.getLastModifiedAt()));
        dto.setNumCode(entity.getNumCode());

        return dto;
    }
}
