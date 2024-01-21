package org.kps.currency.domain.dto;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.kps.currency.validation.CodeISO;

@Getter
@Setter
@ToString
public class CurrencyRequestDTOGetListImpl implements CurrencyRequestDTO {

    @CodeISO
    private String quote;
}
