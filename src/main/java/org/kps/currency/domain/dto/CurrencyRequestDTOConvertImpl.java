package org.kps.currency.domain.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.kps.currency.validation.CodeISO;

@Getter
@Setter
@ToString
public class CurrencyRequestDTOConvertImpl implements CurrencyRequestDTO{

    @CodeISO(message = "The currency code must comply with the ISO 4217 standard")
    private String base;


    @CodeISO(message = "The currency code must comply with the ISO 4217 standard")
    private String quote;

    @Min(value = 1, message = "Value must be greater than 0.")
    @Max(value = 1_000_000_000, message = "Value must be lesser than 1_000_000_000")
    private Long value;
}
