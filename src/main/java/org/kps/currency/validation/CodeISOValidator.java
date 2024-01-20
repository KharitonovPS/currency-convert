package org.kps.currency.validation;

import jakarta.annotation.PostConstruct;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.kps.currency.domain.CurrencyEntity;
import org.kps.currency.repository.CurrencyRepo;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;

import java.util.TreeSet;

@Component
@DependsOn("schemaUpdateService")
@Slf4j
public class CodeISOValidator implements ConstraintValidator<CodeISO, Object> {
    private final CurrencyRepo currencyRepo;
    private final TreeSet<String> inMemoryCodes;


    public CodeISOValidator(CurrencyRepo currencyRepo, TreeSet<String> inMemoryCodes) {
        this.currencyRepo = currencyRepo;
        this.inMemoryCodes = inMemoryCodes;
    }

    @PostConstruct
    public void initTree() {
        try {
            log.info("Try to load all ISO code data to make \"inMemory\" set...");
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
                return inMemoryCodes.contains((String) input);
        }
        return false;
    }
}

