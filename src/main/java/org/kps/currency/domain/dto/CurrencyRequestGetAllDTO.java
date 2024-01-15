package org.kps.currency.domain.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.kps.currency.validation.CodeISO;

@Getter
@Setter
@ToString
public class CurrencyRequestGetAllDTO {

    @CodeISO
    private String quote;
}
