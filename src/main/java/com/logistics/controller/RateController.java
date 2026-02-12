package com.logistics.controller;

import com.logistics.dto.RateHistoryResponse;
import com.logistics.dto.RateResponse;
import com.logistics.dto.RateUpdateRequest;
import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import com.logistics.service.RateService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/rates")
public class RateController {

    private final RateService rateService;

    public RateController(RateService rateService) {
        this.rateService = rateService;
    }

    @GetMapping
    public ResponseEntity<List<RateResponse>> getAllActiveRates() {
        List<RateResponse> rates = rateService.getAllActiveRates().stream()
                .map(RateResponse::from)
                .toList();
        return ResponseEntity.ok(rates);
    }

    @GetMapping("/{type}")
    public ResponseEntity<RateResponse> getRate(@PathVariable TransportationType type) {
        Rate rate = rateService.getActiveRate(type);
        return ResponseEntity.ok(RateResponse.from(rate));
    }

    @PutMapping("/{type}")
    public ResponseEntity<RateResponse> updateRate(@PathVariable TransportationType type,
                                                   @Valid @RequestBody RateUpdateRequest request) {
        Rate updated = rateService.updateRate(type, request.baseRatePerUnit(),
                request.volumeMultiplier(), request.sizeMultiplier());
        return ResponseEntity.ok(RateResponse.from(updated));
    }

    @GetMapping("/{type}/history")
    public ResponseEntity<List<RateHistoryResponse>> getRateHistory(@PathVariable TransportationType type) {
        List<RateHistoryResponse> history = rateService.getRateHistory(type).stream()
                .map(RateHistoryResponse::from)
                .toList();
        return ResponseEntity.ok(history);
    }
}
