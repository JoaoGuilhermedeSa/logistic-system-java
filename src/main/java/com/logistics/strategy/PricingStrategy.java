package com.logistics.strategy;

import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import java.math.BigDecimal;

public interface PricingStrategy {

    BigDecimal calculate(BigDecimal volume, BigDecimal size, Rate rate);

    TransportationType getType();
}
