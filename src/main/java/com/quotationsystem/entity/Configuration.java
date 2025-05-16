package com.quotationsystem.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "configuration")
@Builder(toBuilder = true)
public class Configuration {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "rnc")
  private String rnc;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "address")
  private String address;

  @Column(name = "phone")
  private String phone;

  @Column(name = "draft_prefix")
  private String draftPrefix;

  @Column(name = "draft_sequence")
  private Integer draftSequence;

  @Column(name = "prefix")
  private String prefix;

  @Column(name = "sequence")
  private Integer sequence;

  @Column(name = "report_template")
  private String reportTemplate;
}
