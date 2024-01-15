package org.kps.currency.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class CurrencyResponseDTO {

    private String charCode;

    private int numCode;

    private String name;

    private String rate;

    private Instant lastModifiedAt;
}
