package com.logistics.strategy;

import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class RailPricingStrategy implements PricingStrategy {

    private static final BigDecimal TIER1_THRESHOLD = new BigDecimal("1000");
    private static final BigDecimal TIER2_THRESHOLD = new BigDecimal("5000");
    private static final BigDecimal TIER1_DISCOUNT = new BigDecimal("0.95");
    private static final BigDecimal TIER2_DISCOUNT = new BigDecimal("0.85");

    @Override
    public BigDecimal calculate(BigDecimal volume, BigDecimal size, Rate rate) {
        BigDecimal volumeComponent = volume.multiply(rate.getVolumeMultiplier());
        BigDecimal sizeComponent = size.multiply(rate.getSizeMultiplier());
        BigDecimal price = rate.getBaseRatePerUnit().multiply(volumeComponent).add(sizeComponent);

        // Tiered discounts based on volume for large shipments
        if (volume.compareTo(TIER2_THRESHOLD) > 0) {
            price = price.multiply(TIER2_DISCOUNT); // 15% discount for volume > 5000 m³
        } else if (volume.compareTo(TIER1_THRESHOLD) > 0) {
            price = price.multiply(TIER1_DISCOUNT); // 5% discount for volume > 1000 m³
        }

        return price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public TransportationType getType() {
        return TransportationType.RAIL;
    }
}
