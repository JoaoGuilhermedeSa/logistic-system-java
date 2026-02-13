package com.logistics.strategy;

import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BoatPricingStrategyTest {

    private BoatPricingStrategy strategy;
    private Rate rate;

    @BeforeEach
    void setUp() {
        strategy = new BoatPricingStrategy();
        rate = new Rate(
                TransportationType.BOAT,
                new BigDecimal("10.00"),   // baseRatePerUnit
                new BigDecimal("1.50"),    // volumeMultiplier
                new BigDecimal("2.00"),    // sizeMultiplier
                LocalDateTime.now()
        );
    }

    @Test
    void shouldReturnBoatType() {
        assertEquals(TransportationType.BOAT, strategy.getType());
    }

    @Test
    void shouldCalculateStandardPrice() {
        // price = 10 * (100 * 1.5) + (2000 * 2.0) = 10 * 150 + 4000 = 1500 + 4000 = 5500.00
        BigDecimal result = strategy.calculate(
                new BigDecimal("100"),
                new BigDecimal("2000"),
                rate
        );
        assertEquals(new BigDecimal("5500.00"), result);
    }

    @Test
    void shouldApplyBulkDiscountWhenVolumeExceeds500() {
        // price = 10 * (600 * 1.5) + (2000 * 2.0) = 10 * 900 + 4000 = 9000 + 4000 = 13000
        // with 10% discount: 13000 * 0.90 = 11700.00
        BigDecimal result = strategy.calculate(
                new BigDecimal("600"),
                new BigDecimal("2000"),
                rate
        );
        assertEquals(new BigDecimal("11700.00"), result);
    }

    @Test
    void shouldNotApplyDiscountAtExactly500() {
        // price = 10 * (500 * 1.5) + (2000 * 2.0) = 10 * 750 + 4000 = 7500 + 4000 = 11500.00
        BigDecimal result = strategy.calculate(
                new BigDecimal("500"),
                new BigDecimal("2000"),
                rate
        );
        assertEquals(new BigDecimal("11500.00"), result);
    }
}
