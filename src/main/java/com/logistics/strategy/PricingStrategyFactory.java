package com.logistics.strategy;

import com.logistics.model.TransportationType;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;

@Component
public class PricingStrategyFactory {

    private final Map<TransportationType, PricingStrategy> strategies;

    public PricingStrategyFactory(List<PricingStrategy> strategyList) {
        this.strategies = strategyList.stream()
                .collect(Collectors.toMap(PricingStrategy::getType, Function.identity()));
    }

    public PricingStrategy getStrategy(TransportationType type) {
        PricingStrategy strategy = strategies.get(type);
        if (strategy == null) {
            throw new IllegalArgumentException("No pricing strategy found for type: " + type);
        }
        return strategy;
    }
}
