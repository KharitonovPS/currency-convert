package org.kps.currency.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.kps.currency.repository.CurrencyRepo;
import org.springframework.stereotype.Component;

@Component
public class CodeISOValidator implements ConstraintValidator<CodeISO, Object> {

    private final CurrencyRepo currencyRepo;

    public CodeISOValidator(CurrencyRepo currencyRepo) {
        this.currencyRepo = currencyRepo;
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
                return currencyRepo.findByCharCode((String) input).isPresent();
        }
        return false;
    }
}

