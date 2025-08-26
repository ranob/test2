package com.inditex.priceservice.domain.port.output;

import com.inditex.priceservice.domain.model.Price;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * Output Port for the Price domain.
 * Defines the contract for data persistence operations needed by the domain.
 * This interface is implemented by a persistence adapter in the infrastructure layer.
 */
public interface PriceRepositoryPort {
    /**
     * Finds the single, highest-priority applicable price for the given criteria.
     * The database is responsible for filtering and sorting by priority.
     *
     * @param productId The ID of the product.
     * @param brandId The ID of the brand.
     * @param applicationDate The date that must be within the price's start and end dates.
     * @return An Optional containing the matching Price domain object, or empty if not found.
     */
    Optional<Price> findHighestPriorityApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate);
}
