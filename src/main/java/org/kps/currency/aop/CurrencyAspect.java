package org.kps.currency.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.kps.currency.domain.dto.CurrencyApiDTO;
import org.kps.currency.domain.dto.CurrencyRequestDTOConvertImpl;
import org.kps.currency.domain.dto.CurrencyRequestDTOGetListImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class CurrencyAspect {

    @Around("Pointcuts.getMethods()")
    public Object aroundGetAllRatesAdvice(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        CurrencyRequestDTOGetListImpl quoteDTO;
        CurrencyRequestDTOConvertImpl convertDTO;
        if (methodSignature.getName().equals("getAllRatesForQuote")) {
            log.info("Trying to get all rates from database");
            Object[] arguments = joinPoint.getArgs();
            for (Object arg : arguments) {
                if (arg instanceof CurrencyRequestDTOGetListImpl) {
                    quoteDTO = (CurrencyRequestDTOGetListImpl) arg;
                    log.info("Get all info about currency pairs for -> {} currency", quoteDTO.getQuote());
                }
            }
        } else if (methodSignature.getName().equals("getRateForQuote")) {
            Object[] arguments = joinPoint.getArgs();
            for (Object arg : arguments) {
                if (arg instanceof CurrencyRequestDTOConvertImpl) {
                    convertDTO = (CurrencyRequestDTOConvertImpl) arg;
                    log.info("Convert {} {} to {}",
                            convertDTO.getValue(),
                            convertDTO.getBase(),
                            convertDTO.getQuote()
                    );
                }
            }
        }
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error(e.getMessage(), e);
            result = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return result;
    }

    @Around("Pointcuts.mappingMethod()")
    public Object aroundMappingMethodAdvice(ProceedingJoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        if (methodSignature.getName().equals("currencyDtoToEntity")) {
            Object[] arguments = joinPoint.getArgs();
            for (Object arg : arguments) {
                if (arg instanceof CurrencyApiDTO) {
                    log.info("Mapper takes DTO from client");
                }
            }
        }
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Error in currencyDtoToEntity method: {}", e.getMessage(), e);
        }
        log.info("DTO was mapped to entity {}", result);
        return result;
    }
}