package com.quotationsystem.service.implementation;

import com.quotationsystem.entity.*;
import com.quotationsystem.repository.*;
import com.quotationsystem.service.interfaces.QuotationService;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class QuotationServiceImpl implements QuotationService {

  @Autowired private QuotationHeaderRepository quotationHeaderRepository;

  @Autowired private QuotationDetailRepository quotationDetailRepository;

  @Autowired private ProductRepository productRepository;
  @Autowired private ConfigurationServiceImpl configurationService;

  @Override
  @Transactional
  public QuotationHeader create(QuotationHeader quotationHeader) {
    if (quotationHeader.getStatus() == null) {
      quotationHeader.setStatus(Status.DRAFT);
    }
    if (quotationHeader.getQuotationNumber() == null
        && quotationHeader.getStatus() == Status.DRAFT) {
      quotationHeader.setQuotationNumber(configurationService.getNextDraftSequence());
      quotationHeader.setQuotationNumber(configurationService.getNextDraftSequence());
    }

    if (Objects.equals(quotationHeader.getSequentialNumber(), quotationHeader.getQuotationNumber())
        && quotationHeader.getStatus() == Status.APPROVED)
      quotationHeader.setSequentialNumber(configurationService.getNextFinalSequence());

    var header = quotationHeaderRepository.save(quotationHeader);
    double totalTaxAmount = 0;
    double subtotalAmount = 0;
    double totalAmount = 0;
    if (quotationHeader.getDetails() != null) {
      for (QuotationDetail detail : quotationHeader.getDetails()) {
        var item = productRepository.findById(detail.getProduct().getId()).orElseThrow();
        detail.setProduct(item);
        var taxRate = item.getTax().getRate();
        double taxAmount =
            ((detail.getQuotedPrice() * taxRate) - detail.getQuotedPrice()) * detail.getQuantity();
        double subtotal = detail.getQuotedPrice() * detail.getQuantity();
        detail.setTaxAmount(taxAmount);
        totalTaxAmount += taxAmount;
        subtotalAmount += subtotal;
        totalAmount += subtotal + taxAmount;
        detail.setQuotationHeader(header);
        quotationDetailRepository.save(detail);
      }
    }
    quotationHeader.setTotalAmount(totalAmount);
    quotationHeader.setTaxAmount(totalTaxAmount);
    quotationHeader.setSubtotalAmount(subtotalAmount);
    return quotationHeaderRepository.save(quotationHeader);
  }

  @Override
  public Optional<QuotationHeader> get(Long id) {
    return quotationHeaderRepository.findById(id);
  }

  @Override
  public List<QuotationHeader> getAll() {
    LocalDate dateLimit = LocalDate.now().minusDays(90);
    return quotationHeaderRepository.findAllWithDetailsFromLast90Days(dateLimit);
  }

  @Override
  public List<QuotationHeader> getBy(String field, Object value) {
    return List.of();
  }

  @Override
  public List<QuotationHeader> getRecent(int limit) {
    return quotationHeaderRepository
        .findAll(PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "issueDate")))
        .getContent();
  }

  @Override
  public Optional<QuotationHeader> update(Long id, QuotationHeader quotationHeader) {
    return quotationHeaderRepository
        .findById(id)
        .map(
            existing -> {
              if (existing.getStatus() == Status.APPROVED) {
                throw new RuntimeException("Cannot update an APPROVED quotation");
              }
              existing.setSequentialNumber(quotationHeader.getSequentialNumber());
              existing.setClient(quotationHeader.getClient());
              existing.setSeller(quotationHeader.getSeller());
              existing.setUser(quotationHeader.getUser());
              existing.setDepartment(quotationHeader.getDepartment());
              existing.setDeliveryTime(quotationHeader.getDeliveryTime());
              existing.setIssueDate(quotationHeader.getIssueDate());
              double totalAmount = 0;
              for (QuotationDetail detail : quotationHeader.getDetails()) {
                double subtotal = detail.getQuotedPrice() * detail.getQuantity();
                double taxAmount =
                    subtotal
                        * (detail.getProduct().getTax() != null
                            ? detail.getProduct().getTax().getRate()
                            : 0);
                detail.setTaxAmount(taxAmount);
                totalAmount += subtotal + taxAmount;
                detail.setQuotationHeader(existing);
                quotationDetailRepository.save(detail);
              }
              existing.setTotalAmount(totalAmount);
              return quotationHeaderRepository.save(existing);
            });
  }

  @Override
  public boolean delete(Long id) {
    return quotationHeaderRepository
        .findById(id)
        .map(
            quotationHeader -> {
              if (quotationHeader.getStatus() == Status.APPROVED) {
                throw new RuntimeException("Cannot delete an APPROVED quotation");
              }
              quotationHeaderRepository.delete(quotationHeader);
              return true;
            })
        .orElse(false);
  }

  @Override
  public QuotationHeader reissue(Long id) {
    return quotationHeaderRepository
        .findById(id)
        .map(
            existing -> {
              if (existing.getStatus() != Status.APPROVED) {
                throw new RuntimeException("Only APPROVED quotations can be reissued");
              }
              return QuotationHeader.builder()
                  .quotationNumber(existing.getQuotationNumber() + "-RE")
                  .client(existing.getClient())
                  .seller(existing.getSeller())
                  .user(existing.getUser())
                  .department(existing.getDepartment())
                  .deliveryTime(existing.getDeliveryTime())
                  .issueDate(existing.getIssueDate())
                  .totalAmount(existing.getTotalAmount())
                  .status(Status.APPROVED)
                  .build();
            })
        .map(quotationHeaderRepository::save)
        .orElseThrow(() -> new RuntimeException("Quotation not found"));
  }

  @Override
  public QuotationHeader addProduct(
      Long headerId, Long productId, double quotedPrice, int quantity) {
    QuotationHeader header =
        quotationHeaderRepository
            .findById(headerId)
            .orElseThrow(() -> new RuntimeException("Quotation not found"));
    if (header.getStatus() != Status.DRAFT) {
      throw new RuntimeException("Can only add products to DRAFT quotations");
    }
    Product product =
        productRepository
            .findById(productId)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    double subtotal = quotedPrice * quantity;
    double taxAmount = ((quotedPrice * product.getTax().getRate()) - quotedPrice) * quantity;

    QuotationDetail detail =
        QuotationDetail.builder()
            .quotationHeader(header)
            .product(product)
            .quotedPrice(quotedPrice)
            .quantity(quantity)
            .taxAmount(taxAmount)
            .build();

    quotationDetailRepository.save(detail);

    header.setTotalAmount(header.getTotalAmount() + subtotal + taxAmount);
    return quotationHeaderRepository.save(header);
  }

  @Override
  public QuotationHeader approve(Long id, String quotationNumber) {
    return quotationHeaderRepository
        .findById(id)
        .map(
            existing -> {
              if (existing.getStatus() != Status.DRAFT) {
                throw new RuntimeException("Only DRAFT quotations can be approved");
              }
              existing.setStatus(Status.APPROVED);
              existing.setQuotationNumber(quotationNumber);
              existing.setSequentialNumber(null);
              return quotationHeaderRepository.save(existing);
            })
        .orElseThrow(() -> new RuntimeException("Quotation not found"));
  }

  @Override
  public QuotationHeader removeProduct(Long headerId, Long detailId) {
    QuotationHeader header =
        quotationHeaderRepository
            .findById(headerId)
            .orElseThrow(() -> new RuntimeException("Quotation not found"));
    if (header.getStatus() != Status.DRAFT) {
      throw new RuntimeException("Can only remove products from DRAFT quotations");
    }
    QuotationDetail detail =
        quotationDetailRepository
            .findById(detailId)
            .orElseThrow(() -> new RuntimeException("Quotation detail not found"));
    if (!detail.getQuotationHeader().getId().equals(headerId)) {
      throw new RuntimeException("Quotation detail does not belong to this quotation");
    }

    double subtotal = detail.getQuotedPrice() * detail.getQuantity();
    double taxAmount = detail.getTaxAmount();
    header.setTotalAmount(header.getTotalAmount() - subtotal - taxAmount);

    quotationDetailRepository.delete(detail);
    return quotationHeaderRepository.save(header);
  }

  @Override
  public QuotationHeader modifyProduct(
      Long headerId, Long detailId, Double quotedPrice, Integer quantity) {
    QuotationHeader header =
        quotationHeaderRepository
            .findById(headerId)
            .orElseThrow(() -> new RuntimeException("Quotation not found"));
    if (header.getStatus() != Status.DRAFT) {
      throw new RuntimeException("Can only modify products in DRAFT quotations");
    }
    QuotationDetail detail =
        quotationDetailRepository
            .findById(detailId)
            .orElseThrow(() -> new RuntimeException("Quotation detail not found"));
    if (!detail.getQuotationHeader().getId().equals(headerId)) {
      throw new RuntimeException("Quotation detail does not belong to this quotation");
    }

    double oldSubtotal = detail.getQuotedPrice() * detail.getQuantity();
    double oldTaxAmount = detail.getTaxAmount();

    if (quotedPrice != null) {
      detail.setQuotedPrice(quotedPrice);
    }
    if (quantity != null) {
      detail.setQuantity(quantity);
    }

    double newSubtotal = detail.getQuotedPrice() * detail.getQuantity();
    double newTaxAmount =
        newSubtotal
            * (detail.getProduct().getTax() != null ? detail.getProduct().getTax().getRate() : 0);
    detail.setTaxAmount(newTaxAmount);

    header.setTotalAmount(
        header.getTotalAmount() - oldSubtotal - oldTaxAmount + newSubtotal + newTaxAmount);

    quotationDetailRepository.save(detail);
    return quotationHeaderRepository.save(header);
  }

  @Override
  public List<QuotationHeader> getBy(String field, String value) {
    throw new RuntimeException("Invalid field or value: " + field + "=" + value);
  }
}
