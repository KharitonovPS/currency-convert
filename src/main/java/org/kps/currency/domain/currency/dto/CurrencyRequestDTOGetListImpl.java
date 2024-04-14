package org.kps.currency.domain.currency.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.kps.currency.validation.CodeISO;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CurrencyRequestDTOGetListImpl implements CurrencyRequestDTO {

    @CodeISO
    private String quote;
}
