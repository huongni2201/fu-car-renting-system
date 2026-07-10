package com.example.carservice.controller;

import com.example.carservice.dto.request.CarInformationRequest;
import com.example.carservice.dto.response.ApiResponse;
import com.example.carservice.dto.response.CarInformationResponse;
import com.example.carservice.dto.response.PageResponse;
import com.example.carservice.service.CarInformationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/cars")
@RequiredArgsConstructor
public class CarInformationController {

  private final CarInformationService carInformationService;

  @GetMapping
  public ResponseEntity<PageResponse<CarInformationResponse>> getCars(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size
  ) {
    return ResponseEntity.ok(carInformationService.getCars(page, size));
  }

  @GetMapping("/{id}")
  public ResponseEntity<CarInformationResponse> getById(@PathVariable Long id) {
    return ResponseEntity.ok(carInformationService.getById(id));
  }

  @PostMapping
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<CarInformationResponse> create(
      @RequestBody CarInformationRequest request
  ) {
    return ResponseEntity.ok(carInformationService.create(request));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<CarInformationResponse> update(
      @PathVariable Long id,
      @RequestBody CarInformationRequest request
  ) {
    return ResponseEntity.ok(carInformationService.update(id, request));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAuthority('ADMIN')")
  public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
    carInformationService.delete(id);

    return ResponseEntity.ok(ApiResponse.success(null));
  }
}
