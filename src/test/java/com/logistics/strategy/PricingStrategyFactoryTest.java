package com.logistics.strategy;

import com.logistics.model.TransportationType;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PricingStrategyFactoryTest {

    private PricingStrategyFactory factory;

    @BeforeEach
    void setUp() {
        List<PricingStrategy> strategies = List.of(
                new BoatPricingStrategy(),
                new TruckPricingStrategy(),
                new RailPricingStrategy()
        );
        factory = new PricingStrategyFactory(strategies);
    }

    @Test
    void shouldReturnBoatStrategy() {
        PricingStrategy strategy = factory.getStrategy(TransportationType.BOAT);
        assertInstanceOf(BoatPricingStrategy.class, strategy);
    }

    @Test
    void shouldReturnTruckStrategy() {
        PricingStrategy strategy = factory.getStrategy(TransportationType.TRUCK);
        assertInstanceOf(TruckPricingStrategy.class, strategy);
    }

    @Test
    void shouldReturnRailStrategy() {
        PricingStrategy strategy = factory.getStrategy(TransportationType.RAIL);
        assertInstanceOf(RailPricingStrategy.class, strategy);
    }

    @Test
    void shouldThrowWhenStrategyNotFound() {
        PricingStrategyFactory emptyFactory = new PricingStrategyFactory(List.of());
        assertThrows(IllegalArgumentException.class,
                () -> emptyFactory.getStrategy(TransportationType.BOAT));
    }
}
