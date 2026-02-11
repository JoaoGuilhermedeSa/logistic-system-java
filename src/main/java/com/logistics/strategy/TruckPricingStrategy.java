package com.logistics.strategy;

import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import java.math.BigDecimal;
import java.math.RoundingMode;
import org.springframework.stereotype.Component;

@Component
public class TruckPricingStrategy implements PricingStrategy {

    private static final BigDecimal OVERSIZE_THRESHOLD = new BigDecimal("5000");
    private static final BigDecimal OVERSIZE_SURCHARGE = new BigDecimal("1.15");

    @Override
    public BigDecimal calculate(BigDecimal volume, BigDecimal size, Rate rate) {
        BigDecimal volumeComponent = volume.multiply(rate.getVolumeMultiplier());
        BigDecimal sizeComponent = size.multiply(rate.getSizeMultiplier());
        BigDecimal price = rate.getBaseRatePerUnit().multiply(volumeComponent).add(sizeComponent);

        // Surcharge: 15% extra for oversized cargo (size > 5000 kg)
        if (size.compareTo(OVERSIZE_THRESHOLD) > 0) {
            price = price.multiply(OVERSIZE_SURCHARGE);
        }

        return price.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public TransportationType getType() {
        return TransportationType.TRUCK;
    }
}
