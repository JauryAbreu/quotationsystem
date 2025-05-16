package com.quotationsystem.repository;

import com.quotationsystem.entity.QuotationHeader;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface QuotationHeaderRepository extends JpaRepository<QuotationHeader, Long> {
  List<QuotationHeader> findByStatus(String value);

  @Query(
      "SELECT q FROM QuotationHeader q LEFT JOIN FETCH q.details WHERE q.issueDate >= :dateLimit")
  List<QuotationHeader> findAllWithDetailsFromLast90Days(@Param("dateLimit") LocalDate dateLimit);
}
