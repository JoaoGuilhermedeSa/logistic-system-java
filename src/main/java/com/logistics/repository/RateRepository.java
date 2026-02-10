package com.logistics.repository;

import com.logistics.model.Rate;
import com.logistics.model.TransportationType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RateRepository extends JpaRepository<Rate, Long> {

    @Query("SELECT r FROM Rate r WHERE r.transportationType = :type "
            + "AND r.effectiveFrom <= :now "
            + "AND (r.effectiveTo IS NULL OR r.effectiveTo > :now)")
    Optional<Rate> findActiveRate(@Param("type") TransportationType type,
                                  @Param("now") LocalDateTime now);

    List<Rate> findByTransportationTypeOrderByEffectiveFromDesc(TransportationType type);

    @Query("SELECT r FROM Rate r WHERE r.effectiveFrom <= :now "
            + "AND (r.effectiveTo IS NULL OR r.effectiveTo > :now)")
    List<Rate> findAllActiveRates(@Param("now") LocalDateTime now);
}
