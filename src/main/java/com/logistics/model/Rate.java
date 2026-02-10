package com.logistics.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "rates")
public class Rate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "transportation_type", nullable = false)
    private TransportationType transportationType;

    @Column(name = "base_rate_per_unit", nullable = false, precision = 19, scale = 4)
    private BigDecimal baseRatePerUnit;

    @Column(name = "volume_multiplier", nullable = false, precision = 19, scale = 4)
    private BigDecimal volumeMultiplier;

    @Column(name = "size_multiplier", nullable = false, precision = 19, scale = 4)
    private BigDecimal sizeMultiplier;

    @Column(name = "effective_from", nullable = false)
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    public Rate() {
    }

    public Rate(TransportationType transportationType, BigDecimal baseRatePerUnit,
                BigDecimal volumeMultiplier, BigDecimal sizeMultiplier,
                LocalDateTime effectiveFrom) {
        this.transportationType = transportationType;
        this.baseRatePerUnit = baseRatePerUnit;
        this.volumeMultiplier = volumeMultiplier;
        this.sizeMultiplier = sizeMultiplier;
        this.effectiveFrom = effectiveFrom;
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

    public BigDecimal getBaseRatePerUnit() {
        return baseRatePerUnit;
    }

    public void setBaseRatePerUnit(BigDecimal baseRatePerUnit) {
        this.baseRatePerUnit = baseRatePerUnit;
    }

    public BigDecimal getVolumeMultiplier() {
        return volumeMultiplier;
    }

    public void setVolumeMultiplier(BigDecimal volumeMultiplier) {
        this.volumeMultiplier = volumeMultiplier;
    }

    public BigDecimal getSizeMultiplier() {
        return sizeMultiplier;
    }

    public void setSizeMultiplier(BigDecimal sizeMultiplier) {
        this.sizeMultiplier = sizeMultiplier;
    }

    public LocalDateTime getEffectiveFrom() {
        return effectiveFrom;
    }

    public void setEffectiveFrom(LocalDateTime effectiveFrom) {
        this.effectiveFrom = effectiveFrom;
    }

    public LocalDateTime getEffectiveTo() {
        return effectiveTo;
    }

    public void setEffectiveTo(LocalDateTime effectiveTo) {
        this.effectiveTo = effectiveTo;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
