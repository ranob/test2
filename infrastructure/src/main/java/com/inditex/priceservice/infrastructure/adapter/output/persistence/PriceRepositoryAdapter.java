package com.inditex.priceservice.infrastructure.adapter.output.persistence;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.port.output.PriceRepositoryPort;
import com.inditex.priceservice.infrastructure.adapter.output.persistence.entity.PriceEntity;
import com.inditex.priceservice.infrastructure.adapter.output.persistence.jpa.PriceJpaRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PriceRepositoryAdapter implements PriceRepositoryPort {

    private static final Logger log = LoggerFactory.getLogger(PriceRepositoryAdapter.class);

    private final PriceJpaRepository priceJpaRepository;
    private final PricePersistenceMapper priceMapper;

    @Override
    public Optional<Price> findHighestPriorityApplicablePrice(Long productId, Long brandId, LocalDateTime applicationDate) {
        log.debug("Querying database for applicable price with productId: {}, brandId: {}, applicationDate: {}",
                productId, brandId, applicationDate);

        List<PriceEntity> entities = priceJpaRepository.findApplicablePricesOrderedByPriority(
                productId, brandId, applicationDate, PageRequest.of(0, 1));

        if (entities.isEmpty()) {
            log.info("No applicable price found in database for productId: {}, brandId: {}", productId, brandId);
            return Optional.empty();
        } else {
            PriceEntity highestPriorityEntity = entities.get(0);
            log.info("Found highest priority applicable price in database: {}", highestPriorityEntity);
            return Optional.of(priceMapper.toDomain(highestPriorityEntity));
        }
    }
}

