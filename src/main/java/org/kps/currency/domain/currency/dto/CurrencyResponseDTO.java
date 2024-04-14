package org.kps.currency.domain.currency.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.sql.Timestamp;

@Getter
@Setter
@ToString
public class CurrencyResponseDTO {

    private String charCode;

    private int numCode;

    private String name;

    private String rate;

    private Timestamp lastModifiedAt;
}
