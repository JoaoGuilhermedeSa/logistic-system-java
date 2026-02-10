package com.logistics.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "freight_quotes")
public class FreightQuote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transportation_type", nullable = false)
    private TransportationType transportationType;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal volume;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal size;

    @Column(name = "calculated_price", nullable = false, precision = 19, scale = 4)
    private BigDecimal calculatedPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rate_snapshot_id", nullable = false)
    private Rate rateSnapshot;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public FreightQuote() {
    }

    public FreightQuote(TransportationType transportationType, BigDecimal volume,
                        BigDecimal size, BigDecimal calculatedPrice, Rate rateSnapshot) {
        this.transportationType = transportationType;
        this.volume = volume;
        this.size = size;
        this.calculatedPrice = calculatedPrice;
        this.rateSnapshot = rateSnapshot;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public TransportationType getTransportationType() {
        return transportationType;
    }

    public void setTransportationType(TransportationType transportationType) {
        this.transportationType = transportationType;
    }

    public BigDecimal getVolume() {
        return volume;
    }

    public void setVolume(BigDecimal volume) {
        this.volume = volume;
    }

    public BigDecimal getSize() {
        return size;
    }

    public void setSize(BigDecimal size) {
        this.size = size;
    }

    public BigDecimal getCalculatedPrice() {
        return calculatedPrice;
    }

    public void setCalculatedPrice(BigDecimal calculatedPrice) {
        this.calculatedPrice = calculatedPrice;
    }

    public Rate getRateSnapshot() {
        return rateSnapshot;
    }

    public void setRateSnapshot(Rate rateSnapshot) {
        this.rateSnapshot = rateSnapshot;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
