package com.example.rentingservice.controller;

import com.example.rentingservice.dto.request.CreateRentingTransactionRequest;
import com.example.rentingservice.dto.response.ApiResponse;
import com.example.rentingservice.dto.response.ReportStatisticResponse;
import com.example.rentingservice.dto.response.RentingTransactionResponse;
import com.example.rentingservice.service.RentingTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.format.annotation.DateTimeFormat.ISO.DATE;

@RestController
@RequestMapping("/api/v1/rentings")
@RequiredArgsConstructor
public class RentingTransactionController {

  private final RentingTransactionService rentingTransactionService;

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('CUSTOMER') and @securityExpression.isCurrentUser(#request.customerId(), authentication))")
  public ResponseEntity<RentingTransactionResponse> create(
      @RequestBody CreateRentingTransactionRequest request
  ) {
    return ResponseEntity.ok(rentingTransactionService.create(request));
  }

  @GetMapping("/{id}")
  @PreAuthorize("hasAnyAuthority('ADMIN', 'CUSTOMER')")
  public ResponseEntity<RentingTransactionResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(rentingTransactionService.getById(id));
  }

  @GetMapping("/customers/{customerId}")
  @PreAuthorize("hasAuthority('ADMIN') or (hasAuthority('CUSTOMER') and @securityExpression.isCurrentUser(#customerId, authentication))")
  public ResponseEntity<List<RentingTransactionResponse>> getHistory(
      @PathVariable Long customerId
  ) {
    return ResponseEntity.ok(rentingTransactionService.getHistory(customerId));
  }

  @GetMapping("/admin/report")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<List<ReportStatisticResponse>> getReport(
      @RequestParam @DateTimeFormat(iso = DATE) LocalDate startDate,
      @RequestParam @DateTimeFormat(iso = DATE) LocalDate endDate
  ) {
    return ResponseEntity.ok(rentingTransactionService.getReport(startDate, endDate));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
    rentingTransactionService.delete(id);

    return ResponseEntity.ok(ApiResponse.success(null));
  }
}
