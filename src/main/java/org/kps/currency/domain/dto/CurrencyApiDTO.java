package org.kps.currency.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Map;

@Getter
@Setter
@ToString
public class CurrencyApiDTO {

    private String disclaimer;

    private String license;

    private Timestamp timestamp;

    private String base;

    private Map<String, BigDecimal> rates;
}
