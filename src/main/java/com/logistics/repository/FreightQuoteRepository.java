package com.logistics.repository;

import com.logistics.model.FreightQuote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FreightQuoteRepository extends JpaRepository<FreightQuote, Long> {
}
