package org.kps.currency.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.LastModifiedDate;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "currency_data", indexes = @Index(columnList = "currency_code",
        name = "char_code_index", unique = true))
public class CurrencyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "currency_code", unique = true, length = 3)
    private String charCode;

    @Column(name = "numeric_code", unique = true, length = 5)
    private int numCode;

    @Column(name = "display_name", length = 30)
    private String name;

    @Column(name = "rate_to_USD", precision = 38, scale = 10)
    private BigDecimal rate;

    @LastModifiedDate
    @Column(name = "last_modified_at", columnDefinition = "TIMESTAMP")
    private Instant lastModifiedAt;
}
