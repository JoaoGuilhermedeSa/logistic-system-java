package com.logistics.dto;

import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RateResponse(
        Long id,
        TransportationType transportationType,
        BigDecimal baseRatePerUnit,
        BigDecimal volumeMultiplier,
        BigDecimal sizeMultiplier,
        LocalDateTime effectiveFrom,
        LocalDateTime effectiveTo
) {

    public static RateResponse from(Rate rate) {
        return new RateResponse(
                rate.getId(),
                rate.getTransportationType(),
                rate.getBaseRatePerUnit(),
                rate.getVolumeMultiplier(),
                rate.getSizeMultiplier(),
                rate.getEffectiveFrom(),
                rate.getEffectiveTo()
        );
    }
}
