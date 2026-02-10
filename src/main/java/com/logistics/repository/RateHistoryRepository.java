package com.logistics.repository;

import com.logistics.model.RateHistory;
import com.logistics.model.TransportationType;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RateHistoryRepository extends JpaRepository<RateHistory, Long> {

    @Query("SELECT rh FROM RateHistory rh WHERE rh.rate.transportationType = :type "
            + "ORDER BY rh.changedAt DESC")
    List<RateHistory> findByTransportationType(@Param("type") TransportationType type);
}
