package com.quotationsystem.service.interfaces;

import com.quotationsystem.entity.QuotationHeader;
import java.util.List;

public interface QuotationService extends ServiceCommon<QuotationHeader, Long> {

  List<QuotationHeader> getRecent(int limit);

  QuotationHeader reissue(Long id);

  QuotationHeader addProduct(Long headerId, Long productId, double quotedPrice, int quantity);

  QuotationHeader approve(Long id, String quotationNumber);

  QuotationHeader removeProduct(Long headerId, Long detailId);

  QuotationHeader modifyProduct(Long headerId, Long detailId, Double quotedPrice, Integer quantity);

  List<QuotationHeader> getBy(String field, String value);
}
