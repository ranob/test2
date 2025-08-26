package com.inditex.priceservice.infrastructure.adapter.output.persistence;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.infrastructure.adapter.output.persistence.entity.PriceEntity;
import com.inditex.priceservice.infrastructure.adapter.output.persistence.jpa.PriceJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest(properties = { "spring.sql.init.mode=never" })
@Import({PriceRepositoryAdapter.class, PricePersistenceMapper.class})
class PriceRepositoryAdapterTest {

    @Autowired
    private PriceRepositoryAdapter priceRepositoryAdapter;

    @Autowired
    private PriceJpaRepository priceJpaRepository;

    @Test
    @DisplayName("Should find and return the highest priority price")
    void givenMultiplePrices_whenFindHighestPriority_thenReturnsCorrectPrice() {
        // Arrange
        LocalDateTime applicationDate = LocalDateTime.parse("2020-06-14T16:00:00");

        // Save a lower priority price that is also applicable
        PriceEntity lowPriorityPrice = new PriceEntity();
        lowPriorityPrice.setBrandId(1L);
        lowPriorityPrice.setProductId(35455L);
        lowPriorityPrice.setPriceList(1);
        lowPriorityPrice.setPriority(0);
        lowPriorityPrice.setPriceValue(new BigDecimal("35.50"));
        lowPriorityPrice.setCurrency("EUR");
        lowPriorityPrice.setStartDate(LocalDateTime.parse("2020-06-14T00:00:00"));
        lowPriorityPrice.setEndDate(LocalDateTime.parse("2020-12-31T23:59:59"));
        priceJpaRepository.save(lowPriorityPrice);

        // Save the highest priority price
        PriceEntity highPriorityPrice = new PriceEntity();
        highPriorityPrice.setBrandId(1L);
        highPriorityPrice.setProductId(35455L);
        highPriorityPrice.setPriceList(2);
        highPriorityPrice.setPriority(1);
        highPriorityPrice.setPriceValue(new BigDecimal("25.45"));
        highPriorityPrice.setCurrency("EUR");
        highPriorityPrice.setStartDate(LocalDateTime.parse("2020-06-14T15:00:00"));
        highPriorityPrice.setEndDate(LocalDateTime.parse("2020-06-14T18:30:00"));
        priceJpaRepository.save(highPriorityPrice);

        // Act
        Optional<Price> result = priceRepositoryAdapter.findHighestPriorityApplicablePrice(35455L, 1L, applicationDate);

        // Assert
        assertTrue(result.isPresent(), "A price should have been found.");
        assertEquals(1, result.get().getPriority(), "The price with the highest priority (1) should be selected.");
        assertEquals(0, new BigDecimal("25.45").compareTo(result.get().getPriceValue()), "The price value should be 25.45.");
    }

    @Test
    @DisplayName("Should return empty optional when no price is applicable")
    void givenNoPrices_whenFindHighestPriority_thenReturnsEmpty() {
        // Act
        Optional<Price> result = priceRepositoryAdapter.findHighestPriorityApplicablePrice(99999L, 1L, LocalDateTime.now());

        // Assert
        assertFalse(result.isPresent(), "The result should be empty when no prices match.");
    }
}
