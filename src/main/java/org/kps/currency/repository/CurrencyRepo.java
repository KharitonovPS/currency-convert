package org.kps.currency.repository;

import org.kps.currency.domain.CurrencyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Repository
public interface CurrencyRepo extends JpaRepository<CurrencyEntity, Long> {

    Optional<CurrencyEntity> findByCharCode(String charCode);

    @Modifying
    @Query("UPDATE CurrencyEntity e SET e.rate = :value, " +
            "e.lastModifiedAt = :updatedAt " +
            "WHERE e.charCode = :charCode")
    void updateRateByCharCode(@Param("charCode") String charCode,
                              @Param("value") BigDecimal rate,
                              @Param("updatedAt") Instant lastModifiedAt);
}
