package org.kps.currency.common.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* org.kps.currency.domain.currency.service.CurrencyConverterService.get*(..))")
    public void getMethods() {
    }

    @Pointcut("execution(* org.kps.currency.domain.currency.mapper.CurrencyApiMapper.currencyDtoToEntity(..))")
    public void mappingMethod() {
    }

}
