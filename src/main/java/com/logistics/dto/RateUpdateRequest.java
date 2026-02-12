package com.logistics.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public record RateUpdateRequest(
        @NotNull(message = "Base rate per unit is required")
        @Positive(message = "Base rate per unit must be positive")
        BigDecimal baseRatePerUnit,

        @NotNull(message = "Volume multiplier is required")
        @Positive(message = "Volume multiplier must be positive")
        BigDecimal volumeMultiplier,

        @NotNull(message = "Size multiplier is required")
        @Positive(message = "Size multiplier must be positive")
        BigDecimal sizeMultiplier
) {
}
