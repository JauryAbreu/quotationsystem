package com.quotationsystem.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quotation_header")
@Builder(toBuilder = true)
public class QuotationHeader {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "quotation_number")
  private String quotationNumber;

  @Column(name = "sequential_number")
  private String sequentialNumber;

  @ManyToOne
  @JoinColumn(name = "client_id")
  private Client client;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  private Seller seller;

  @ManyToOne
  @JoinColumn(name = "user_id")
  private User user;

  @ManyToOne
  @JoinColumn(name = "department_id")
  private Department department;

  @ManyToOne
  @JoinColumn(name = "delivery_time_id")
  private DeliveryTime deliveryTime;

  @Column(name = "issue_date")
  private LocalDate issueDate;

  @Column(name = "subtotal_amount")
  private double subtotalAmount;

  @Column(name = "tax_amount")
  private double taxAmount;

  @Column(name = "total_amount")
  private double totalAmount;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private Status status;

  @OneToMany(mappedBy = "quotationHeader", cascade = CascadeType.ALL, orphanRemoval = true)
  @JsonManagedReference
  private List<QuotationDetail> details = new ArrayList<>();
}
