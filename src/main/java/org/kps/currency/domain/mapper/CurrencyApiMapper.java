package org.kps.currency.domain.mapper;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.kps.currency.domain.entity.CurrencyEntity;
import org.kps.currency.domain.dto.CurrencyApiDTO;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Map;

@Component
@Getter
@Setter
@Slf4j
public class CurrencyApiMapper {

    public List<CurrencyEntity> currencyDtoToEntity(CurrencyApiDTO dto) {
        Map<String, BigDecimal> dtoMap = dto.getRates();
        List<CurrencyEntity> resultList = new ArrayList<>();

        for (Map.Entry<String, BigDecimal> entry : dtoMap.entrySet()) {
            try {
                String currencyCode = entry.getKey();
                if (currencyCode != null && !currencyCode.isEmpty()) {
                    Currency currency = Currency.getInstance(currencyCode);

                    CurrencyEntity entity = new CurrencyEntity();
                    entity.setName(currency.getDisplayName());
                    entity.setNumCode(currency.getNumericCode());
                    entity.setCharCode(currency.getCurrencyCode());

                    BigDecimal rate = entry.getValue();

                    entity.setRate(rate);
                    entity.setLastModifiedAt(Instant.now());

                    resultList.add(entity);
                }
            } catch (IllegalArgumentException e) {
                log.error("Can not parse {} ISO code to currency.", entry.getKey());
            }
        }
        return resultList;
    }
}
