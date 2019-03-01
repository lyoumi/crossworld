package com.crossworld.web.converters.impl;

import com.crossworld.web.converters.ConverterService;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;

@Service
public class ConverterServiceImpl implements ConverterService {

    private final ConversionService conversionService;

    public ConverterServiceImpl(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public <T> T convert(Object source, Class<T> targetType) {
        return conversionService.convert(source, targetType);
    }
}
