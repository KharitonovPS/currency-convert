package org.kps.currency.validation;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.kps.currency.domain.currency.entity.CurrencyEntity;
import org.kps.currency.domain.currency.repository.CurrencyRepo;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.HashSet;
import java.util.TreeSet;

@Component
@DependsOn("schemaUpdateService")
@Slf4j
public class CodeISOValidator implements ConstraintValidator<CodeISO, Object> {
    private final CurrencyRepo currencyRepo;
    private final HashSet<String> inMemoryCodes;


    public CodeISOValidator(CurrencyRepo currencyRepo, HashSet<String> inMemoryCodes) {
        this.currencyRepo = currencyRepo;
        this.inMemoryCodes = inMemoryCodes;
    }

    @PostConstruct
    public void initTree() {
        try {
            log.info("Try to load all ISO code values to make \"inMemory\" set...");
            inMemoryCodes.addAll(
                    currencyRepo.findAll()
                            .stream()
                            .map(CurrencyEntity::getCharCode)
                            .toList()
            );
        } catch (Exception e) {
            log.error("Can not load ISO codes from repo -> {}", e.getMessage(), e);
        }
    }

    @Override
    public void initialize(CodeISO codeISO) {
        ConstraintValidator.super.initialize(codeISO);
    }

    @Override
    public boolean isValid(Object input, ConstraintValidatorContext constraintValidatorContext) {

        if (input == null) {
            return false;
        }
        if (input.equals(""))
            return false;

        if (input instanceof Number) {
            return false;
        }
        if (input instanceof String) {
            if (((String) input).matches("^[A-Z]{3}$"))
                return inMemoryCodes.contains(input);
        }
        return false;
    }
}

