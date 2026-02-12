package com.logistics.controller;

import com.logistics.dto.FreightCompareResponse;
import com.logistics.dto.FreightQuoteRequest;
import com.logistics.dto.FreightQuoteResponse;
import com.logistics.model.FreightQuote;
import com.logistics.service.FreightCalculationService;
import jakarta.validation.Valid;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/freight")
public class FreightController {

    private final FreightCalculationService freightCalculationService;

    public FreightController(FreightCalculationService freightCalculationService) {
        this.freightCalculationService = freightCalculationService;
    }

    @PostMapping("/quote")
    public ResponseEntity<FreightQuoteResponse> createQuote(@Valid @RequestBody FreightQuoteRequest request) {
        FreightQuote quote = freightCalculationService.calculateQuote(
                request.transportationType(), request.volume(), request.size());
        return ResponseEntity.status(HttpStatus.CREATED).body(FreightQuoteResponse.from(quote));
    }

    @GetMapping("/quote/{id}")
    public ResponseEntity<FreightQuoteResponse> getQuote(@PathVariable Long id) {
        FreightQuote quote = freightCalculationService.getQuote(id);
        return ResponseEntity.ok(FreightQuoteResponse.from(quote));
    }

    @PostMapping("/compare")
    public ResponseEntity<FreightCompareResponse> compareQuotes(@Valid @RequestBody FreightQuoteRequest request) {
        List<FreightQuote> quotes = freightCalculationService.compareAllTypes(
                request.volume(), request.size());
        List<FreightQuoteResponse> responses = quotes.stream()
                .map(FreightQuoteResponse::from)
                .toList();
        return ResponseEntity.ok(new FreightCompareResponse(request.volume(), request.size(), responses));
    }
}
