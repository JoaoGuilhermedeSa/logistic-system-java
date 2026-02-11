package com.logistics.strategy;

import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class BoatPricingStrategy implements PricingStrategy {

    private static final BigDecimal BULK_VOLUME_THRESHOLD = new BigDecimal("500");
    private static final BigDecimal BULK_DISCOUNT = new BigDecimal("0.90");

    @Override
    public BigDecimal calculate(BigDecimal volume, BigDecimal size, Rate rate) {
        BigDecimal volumeComponent = volume.multiply(rate.getVolumeMultiplier());
        BigDecimal sizeComponent = size.multiply(rate.getSizeMultiplier());
        BigDecimal price = rate.getBaseRatePerUnit().multiply(volumeComponent).add(sizeComponent);

        // Bulk discount: 10% off when volume > 500 m³
        if (volume.compareTo(BULK_VOLUME_THRESHOLD) > 0) {
            price = price.multiply(BULK_DISCOUNT);
        }

        return price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public TransportationType getType() {
        return TransportationType.BOAT;
    }
}
