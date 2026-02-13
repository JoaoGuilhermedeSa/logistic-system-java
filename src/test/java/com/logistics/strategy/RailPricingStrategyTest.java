package com.logistics.strategy;

import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RailPricingStrategyTest {

    private RailPricingStrategy strategy;
    private Rate rate;

    @BeforeEach
    void setUp() {
        strategy = new RailPricingStrategy();
        rate = new Rate(
                TransportationType.RAIL,
                new BigDecimal("8.00"),    // baseRatePerUnit
                new BigDecimal("1.30"),    // volumeMultiplier
                new BigDecimal("2.50"),    // sizeMultiplier
                LocalDateTime.now()
        );
    }

    @Test
    void shouldReturnRailType() {
        assertEquals(TransportationType.RAIL, strategy.getType());
    }

    @Test
    void shouldCalculateStandardPriceWithNoDiscount() {
        // price = 8 * (500 * 1.3) + (3000 * 2.5) = 8 * 650 + 7500 = 5200 + 7500 = 12700.00
        BigDecimal result = strategy.calculate(
                new BigDecimal("500"),
                new BigDecimal("3000"),
                rate
        );
        assertEquals(new BigDecimal("12700.00"), result);
    }

    @Test
    void shouldApplyTier1DiscountWhenVolumeExceeds1000() {
        // price = 8 * (2000 * 1.3) + (3000 * 2.5) = 8 * 2600 + 7500 = 20800 + 7500 = 28300
        // with 5% discount: 28300 * 0.95 = 26885.00
        BigDecimal result = strategy.calculate(
                new BigDecimal("2000"),
                new BigDecimal("3000"),
                rate
        );
        assertEquals(new BigDecimal("26885.00"), result);
    }

    @Test
    void shouldApplyTier2DiscountWhenVolumeExceeds5000() {
        // price = 8 * (6000 * 1.3) + (3000 * 2.5) = 8 * 7800 + 7500 = 62400 + 7500 = 69900
        // with 15% discount: 69900 * 0.85 = 59415.00
        BigDecimal result = strategy.calculate(
                new BigDecimal("6000"),
                new BigDecimal("3000"),
                rate
        );
        assertEquals(new BigDecimal("59415.00"), result);
    }

    @Test
    void shouldNotApplyDiscountAtExactly1000() {
        // price = 8 * (1000 * 1.3) + (3000 * 2.5) = 8 * 1300 + 7500 = 10400 + 7500 = 17900.00
        BigDecimal result = strategy.calculate(
                new BigDecimal("1000"),
                new BigDecimal("3000"),
                rate
        );
        assertEquals(new BigDecimal("17900.00"), result);
    }
}
