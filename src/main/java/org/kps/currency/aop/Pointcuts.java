package org.kps.currency.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* org.kps.currency.domain.service.CurrencyConverterService.get*(..))")
    public void getMethods() {
    }

    @Pointcut("execution(* org.kps.currency.domain.mapper.CurrencyApiMapper.currencyDtoToEntity(..))")
    public void mappingMethod() {
    }

}
