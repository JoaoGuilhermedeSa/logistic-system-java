package com.logistics.strategy;

import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TruckPricingStrategyTest {

    private TruckPricingStrategy strategy;
    private Rate rate;

    @BeforeEach
    void setUp() {
        strategy = new TruckPricingStrategy();
        rate = new Rate(
                TransportationType.TRUCK,
                new BigDecimal("12.00"),   // baseRatePerUnit
                new BigDecimal("1.20"),    // volumeMultiplier
                new BigDecimal("1.80"),    // sizeMultiplier
                LocalDateTime.now()
        );
    }

    @Test
    void shouldReturnTruckType() {
        assertEquals(TransportationType.TRUCK, strategy.getType());
    }

    @Test
    void shouldCalculateStandardPrice() {
        // price = 12 * (100 * 1.2) + (3000 * 1.8) = 12 * 120 + 5400 = 1440 + 5400 = 6840.00
        BigDecimal result = strategy.calculate(
                new BigDecimal("100"),
                new BigDecimal("3000"),
                rate
        );
        assertEquals(new BigDecimal("6840.00"), result);
    }

    @Test
    void shouldApplySurchargeWhenSizeExceeds5000() {
        // price = 12 * (100 * 1.2) + (6000 * 1.8) = 12 * 120 + 10800 = 1440 + 10800 = 12240
        // with 15% surcharge: 12240 * 1.15 = 14076.00
        BigDecimal result = strategy.calculate(
                new BigDecimal("100"),
                new BigDecimal("6000"),
                rate
        );
        assertEquals(new BigDecimal("14076.00"), result);
    }

    @Test
    void shouldNotApplySurchargeAtExactly5000() {
        // price = 12 * (100 * 1.2) + (5000 * 1.8) = 12 * 120 + 9000 = 1440 + 9000 = 10440.00
        BigDecimal result = strategy.calculate(
                new BigDecimal("100"),
                new BigDecimal("5000"),
                rate
        );
        assertEquals(new BigDecimal("10440.00"), result);
    }
}
