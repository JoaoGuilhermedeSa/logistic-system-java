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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RateService {

    private final RateRepository rateRepository;
    private final RateHistoryRepository rateHistoryRepository;

    public RateService(RateRepository rateRepository, RateHistoryRepository rateHistoryRepository) {
        this.rateRepository = rateRepository;
        this.rateHistoryRepository = rateHistoryRepository;
    }

    public List<Rate> getAllActiveRates() {
        return rateRepository.findAllActiveRates(LocalDateTime.now());
    }

    public Rate getActiveRate(TransportationType type) {
        return rateRepository.findActiveRate(type, LocalDateTime.now())
                .orElseThrow(() -> new RateNotFoundException(
                        "No active rate found for transportation type: " + type));
    }

    @Transactional
    public Rate updateRate(TransportationType type, BigDecimal newBaseRate,
                           BigDecimal newVolumeMultiplier, BigDecimal newSizeMultiplier) {
        LocalDateTime now = LocalDateTime.now();

        Rate currentRate = rateRepository.findActiveRate(type, now)
                .orElseThrow(() -> new RateNotFoundException(
                        "No active rate found for transportation type: " + type));

        // Expire the current rate
        currentRate.setEffectiveTo(now);
        rateRepository.save(currentRate);

        // Log history
        rateHistoryRepository.save(new RateHistory(currentRate, currentRate.getBaseRatePerUnit(), newBaseRate));

        // Create new rate
        Rate newRate = new Rate(type, newBaseRate, newVolumeMultiplier, newSizeMultiplier, now);
        return rateRepository.save(newRate);
    }

    public List<RateHistory> getRateHistory(TransportationType type) {
        return rateHistoryRepository.findByTransportationType(type);
    }
}
