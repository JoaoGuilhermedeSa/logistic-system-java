package com.logistics.service;

import com.logistics.model.FreightQuote;
import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import com.logistics.repository.FreightQuoteRepository;
import com.logistics.strategy.BoatPricingStrategy;
import com.logistics.strategy.PricingStrategyFactory;
import com.logistics.strategy.RailPricingStrategy;
import com.logistics.strategy.TruckPricingStrategy;
import jakarta.persistence.EntityNotFoundException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FreightCalculationServiceTest {

    @Mock
    private RateService rateService;

    @Mock
    private FreightQuoteRepository freightQuoteRepository;

    private FreightCalculationService freightCalculationService;

    private Rate boatRate;
    private Rate truckRate;
    private Rate railRate;

    @BeforeEach
    void setUp() {
        PricingStrategyFactory factory = new PricingStrategyFactory(List.of(
                new BoatPricingStrategy(),
                new TruckPricingStrategy(),
                new RailPricingStrategy()
        ));
        freightCalculationService = new FreightCalculationService(rateService, factory, freightQuoteRepository);

        boatRate = new Rate(TransportationType.BOAT,
                new BigDecimal("10.00"), new BigDecimal("1.50"), new BigDecimal("2.00"),
                LocalDateTime.now());
        boatRate.setId(1L);

        truckRate = new Rate(TransportationType.TRUCK,
                new BigDecimal("12.00"), new BigDecimal("1.20"), new BigDecimal("1.80"),
                LocalDateTime.now());
        truckRate.setId(2L);

        railRate = new Rate(TransportationType.RAIL,
                new BigDecimal("8.00"), new BigDecimal("1.10"), new BigDecimal("1.50"),
                LocalDateTime.now());
        railRate.setId(3L);
    }

    @Test
    void shouldCalculateQuoteForBoat() {
        when(rateService.getActiveRate(TransportationType.BOAT)).thenReturn(boatRate);
        when(freightQuoteRepository.save(any(FreightQuote.class)))
                .thenAnswer(invocation -> {
                    FreightQuote q = invocation.getArgument(0);
                    q.setId(1L);
                    return q;
                });

        FreightQuote quote = freightCalculationService.calculateQuote(
                TransportationType.BOAT, new BigDecimal("100"), new BigDecimal("2000"));

        assertNotNull(quote);
        assertEquals(TransportationType.BOAT, quote.getTransportationType());
        // price = 10 * (100 * 1.5) + (2000 * 2.0) = 5500.00
        assertEquals(new BigDecimal("5500.00"), quote.getCalculatedPrice());
    }

    @Test
    void shouldGetExistingQuote() {
        FreightQuote existing = new FreightQuote(
                TransportationType.BOAT, new BigDecimal("100"), new BigDecimal("2000"),
                new BigDecimal("5500.00"), boatRate);
        existing.setId(1L);

        when(freightQuoteRepository.findById(1L)).thenReturn(Optional.of(existing));

        FreightQuote result = freightCalculationService.getQuote(1L);

        assertEquals(1L, result.getId());
        assertEquals(new BigDecimal("5500.00"), result.getCalculatedPrice());
    }

    @Test
    void shouldThrowWhenQuoteNotFound() {
        when(freightQuoteRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> freightCalculationService.getQuote(99L));
    }

    @Test
    void shouldCompareAllTransportTypes() {
        when(rateService.getActiveRate(TransportationType.BOAT)).thenReturn(boatRate);
        when(rateService.getActiveRate(TransportationType.TRUCK)).thenReturn(truckRate);
        when(rateService.getActiveRate(TransportationType.RAIL)).thenReturn(railRate);
        when(freightQuoteRepository.save(any(FreightQuote.class)))
                .thenAnswer(invocation -> {
                    FreightQuote q = invocation.getArgument(0);
                    q.setId(1L);
                    return q;
                });

        List<FreightQuote> quotes = freightCalculationService.compareAllTypes(
                new BigDecimal("200"), new BigDecimal("3000"));

        assertEquals(3, quotes.size());
    }
}
