package com.logistics.dto;

import java.math.BigDecimal;
import java.util.List;

public record FreightCompareResponse(
        BigDecimal volume,
        BigDecimal size,
        List<FreightQuoteResponse> quotes
) {
}
