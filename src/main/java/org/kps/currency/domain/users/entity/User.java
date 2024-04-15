package org.kps.currency.domain.users.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import java.time.Instant;
import java.util.Map;

@Data
@Entity
@Table(name = "app_user")
public class User {

    @Id
    private Long chatId;

    private String firstName;
    private String lasName;

    @Column(unique = true, nullable = false)
    private String userName;

    @Column(nullable = false)
    private Instant registrationDate;

    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, String> subscription;

    private String status;

}
