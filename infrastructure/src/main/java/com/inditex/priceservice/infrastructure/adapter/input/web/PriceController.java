package com.inditex.priceservice.infrastructure.adapter.input.web;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.port.input.PriceUseCase;
import com.inditex.priceservice.infrastructure.adapter.input.web.dto.PriceResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/prices")
@RequiredArgsConstructor
@Slf4j
public class PriceController {

    private final PriceUseCase priceUseCase;
    private final PriceWebMapper priceMapper;

    @GetMapping("/applicable")
    public ResponseEntity<PriceResponse> getApplicablePrice(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime applicationDate,
            @RequestParam Long productId,
            @RequestParam Long brandId) {

        log.info("Incoming request for brandId: {}, productId: {}, applicationDate: {}", brandId, productId, applicationDate);

        // The use case now returns a Price or throws an exception
        Price price = priceUseCase.findApplicablePrice(productId, brandId, applicationDate);

        PriceResponse response = priceMapper.toResponse(price);
        log.info("Found applicable price: {}", response);

        return ResponseEntity.ok(response);
    }
}

