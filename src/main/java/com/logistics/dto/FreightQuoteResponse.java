package com.logistics.dto;

import com.logistics.model.FreightQuote;
import com.logistics.model.TransportationType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FreightQuoteResponse(
        Long id,
        TransportationType transportationType,
        BigDecimal volume,
        BigDecimal size,
        BigDecimal calculatedPrice,
        RateApplied rateApplied,
        LocalDateTime createdAt
) {

    public record RateApplied(
            BigDecimal baseRatePerUnit,
            BigDecimal volumeMultiplier,
            BigDecimal sizeMultiplier
    ) {
    }

    public static FreightQuoteResponse from(FreightQuote quote) {
        return new FreightQuoteResponse(
                quote.getId(),
                quote.getTransportationType(),
                quote.getVolume(),
                quote.getSize(),
                quote.getCalculatedPrice(),
                new RateApplied(
                        quote.getRateSnapshot().getBaseRatePerUnit(),
                        quote.getRateSnapshot().getVolumeMultiplier(),
                        quote.getRateSnapshot().getSizeMultiplier()
                ),
                quote.getCreatedAt()
        );
    }
}
