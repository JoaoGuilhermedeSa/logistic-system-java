package com.logistics.service;

import com.logistics.model.FreightQuote;
import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import com.logistics.repository.FreightQuoteRepository;
import com.logistics.strategy.PricingStrategy;
import com.logistics.strategy.PricingStrategyFactory;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class FreightCalculationService {

    private final RateService rateService;
    private final PricingStrategyFactory pricingStrategyFactory;
    private final FreightQuoteRepository freightQuoteRepository;

    public FreightCalculationService(RateService rateService,
                                     PricingStrategyFactory pricingStrategyFactory,
                                     FreightQuoteRepository freightQuoteRepository) {
        this.rateService = rateService;
        this.pricingStrategyFactory = pricingStrategyFactory;
        this.freightQuoteRepository = freightQuoteRepository;
    }

    public FreightQuote calculateQuote(TransportationType type, BigDecimal volume, BigDecimal size) {
        Rate rate = rateService.getActiveRate(type);
        PricingStrategy strategy = pricingStrategyFactory.getStrategy(type);
        BigDecimal price = strategy.calculate(volume, size, rate);

        FreightQuote quote = new FreightQuote(type, volume, size, price, rate);
        return freightQuoteRepository.save(quote);
    }

    public FreightQuote getQuote(Long id) {
        return freightQuoteRepository.findById(id)
                .orElseThrow(() -> new jakarta.persistence.EntityNotFoundException(
                        "Quote not found with id: " + id));
    }

    public List<FreightQuote> compareAllTypes(BigDecimal volume, BigDecimal size) {
        return Arrays.stream(TransportationType.values())
                .map(type -> calculateQuote(type, volume, size))
                .toList();
    }
}
