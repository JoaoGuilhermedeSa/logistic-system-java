package com.logistics.service;

import com.logistics.exception.RateNotFoundException;
import com.logistics.model.Rate;
import com.logistics.model.RateHistory;
import com.logistics.model.TransportationType;
import com.logistics.repository.RateHistoryRepository;
import com.logistics.repository.RateRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RateServiceTest {

    @Mock
    private RateRepository rateRepository;

    @Mock
    private RateHistoryRepository rateHistoryRepository;

    private RateService rateService;

    private Rate boatRate;

    @BeforeEach
    void setUp() {
        rateService = new RateService(rateRepository, rateHistoryRepository);
        boatRate = new Rate(
                TransportationType.BOAT,
                new BigDecimal("10.00"),
                new BigDecimal("1.50"),
                new BigDecimal("2.00"),
                LocalDateTime.now().minusDays(1)
        );
        boatRate.setId(1L);
    }

    @Test
    void shouldReturnAllActiveRates() {
        when(rateRepository.findAllActiveRates(any(LocalDateTime.class)))
                .thenReturn(List.of(boatRate));

        List<Rate> rates = rateService.getAllActiveRates();

        assertEquals(1, rates.size());
        assertEquals(TransportationType.BOAT, rates.get(0).getTransportationType());
    }

    @Test
    void shouldReturnActiveRateForType() {
        when(rateRepository.findActiveRate(eq(TransportationType.BOAT), any(LocalDateTime.class)))
                .thenReturn(Optional.of(boatRate));

        Rate rate = rateService.getActiveRate(TransportationType.BOAT);

        assertEquals(TransportationType.BOAT, rate.getTransportationType());
        assertEquals(new BigDecimal("10.00"), rate.getBaseRatePerUnit());
    }

    @Test
    void shouldThrowWhenNoActiveRateFound() {
        when(rateRepository.findActiveRate(eq(TransportationType.RAIL), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertThrows(RateNotFoundException.class,
                () -> rateService.getActiveRate(TransportationType.RAIL));
    }

    @Test
    void shouldUpdateRateAndCreateHistory() {
        when(rateRepository.findActiveRate(eq(TransportationType.BOAT), any(LocalDateTime.class)))
                .thenReturn(Optional.of(boatRate));
        when(rateRepository.save(any(Rate.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(rateHistoryRepository.save(any(RateHistory.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Rate updated = rateService.updateRate(
                TransportationType.BOAT,
                new BigDecimal("15.00"),
                new BigDecimal("1.80"),
                new BigDecimal("2.50")
        );

        // Verify old rate was expired
        assertNotNull(boatRate.getEffectiveTo());

        // Verify new rate has correct values
        assertEquals(new BigDecimal("15.00"), updated.getBaseRatePerUnit());
        assertEquals(new BigDecimal("1.80"), updated.getVolumeMultiplier());
        assertEquals(new BigDecimal("2.50"), updated.getSizeMultiplier());

        // Verify history was created
        ArgumentCaptor<RateHistory> historyCaptor = ArgumentCaptor.forClass(RateHistory.class);
        verify(rateHistoryRepository).save(historyCaptor.capture());
        RateHistory history = historyCaptor.getValue();
        assertEquals(new BigDecimal("10.00"), history.getPreviousValue());
        assertEquals(new BigDecimal("15.00"), history.getNewValue());
    }

    @Test
    void shouldThrowWhenUpdatingNonExistentRate() {
        when(rateRepository.findActiveRate(eq(TransportationType.RAIL), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        assertThrows(RateNotFoundException.class,
                () -> rateService.updateRate(TransportationType.RAIL,
                        new BigDecimal("5.00"), new BigDecimal("1.00"), new BigDecimal("1.00")));
    }

    @Test
    void shouldReturnRateHistory() {
        RateHistory historyEntry = new RateHistory(boatRate, new BigDecimal("8.00"), new BigDecimal("10.00"));
        when(rateHistoryRepository.findByTransportationType(TransportationType.BOAT))
                .thenReturn(List.of(historyEntry));

        List<RateHistory> history = rateService.getRateHistory(TransportationType.BOAT);

        assertEquals(1, history.size());
        assertEquals(new BigDecimal("8.00"), history.get(0).getPreviousValue());
        assertEquals(new BigDecimal("10.00"), history.get(0).getNewValue());
    }
}
