package org.kps.currency.aop;

import org.aspectj.lang.annotation.Pointcut;

public class Pointcuts {

    @Pointcut("execution(* org.kps.currency.service.CurrencyConverterService.get*(..))")
    public void getMethods(){}

    @Pointcut("execution(* org.kps.currency.mapper.CurrencyApiMapper.currencyDtoToEntity(..))")
    public void mappingMethod(){}

}
