package com.quotationsystem.repository;

import com.quotationsystem.entity.QuotationDetail;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuotationDetailRepository extends JpaRepository<QuotationDetail, Long> {}
