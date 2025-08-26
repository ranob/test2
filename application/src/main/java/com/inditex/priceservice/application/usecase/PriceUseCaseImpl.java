package com.inditex.priceservice.application.usecase;

import com.inditex.priceservice.domain.exception.PriceNotFoundException;
import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.port.input.PriceUseCase;
import com.inditex.priceservice.domain.port.output.PriceRepositoryPort;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

@RequiredArgsConstructor
public class PriceUseCaseImpl implements PriceUseCase {

    private static final Logger log = LoggerFactory.getLogger(PriceUseCaseImpl.class);

    private final PriceRepositoryPort priceRepositoryPort;

    @Override
    public Price findApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        log.debug("Searching for applicable price for productId: {}, brandId: {}, applicationDate: {}",
                productId, brandId, applicationDate);
        try {
            Price price = priceRepositoryPort.findHighestPriorityApplicablePrice(productId, brandId, applicationDate)
                    .orElseThrow(() -> new PriceNotFoundException(String.format("No price found for product %d, brand %d on %s", productId, brandId, applicationDate)));
            log.info("Found applicable price: {}", price);
            return price;
        } catch (PriceNotFoundException e) {
            log.warn("Price not found for productId: {}, brandId: {}, applicationDate: {}",
                    productId, brandId, applicationDate);
            throw e;
        }
    }
}

