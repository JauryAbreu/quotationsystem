package com.quotationsystem.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quotation_detail")
@Builder(toBuilder = true)
public class QuotationDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne
  @JoinColumn(name = "quotation_header_id")
  @JsonBackReference
  private QuotationHeader quotationHeader;

  @ManyToOne
  @JoinColumn(name = "product_id")
  private Product product;

  @Column(name = "quoted_price", nullable = false)
  private double quotedPrice;

  @Column(name = "quantity")
  private int quantity;

  @Column(name = "tax_amount")
  private double taxAmount;
}
