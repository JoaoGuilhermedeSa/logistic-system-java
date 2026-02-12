package com.logistics.dto;

import com.logistics.model.RateHistory;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RateHistoryResponse(
        Long id,
        BigDecimal previousValue,
        BigDecimal newValue,
        LocalDateTime changedAt
) {

    public static RateHistoryResponse from(RateHistory history) {
        return new RateHistoryResponse(
                history.getId(),
                history.getPreviousValue(),
                history.getNewValue(),
                history.getChangedAt()
        );
    }
}
