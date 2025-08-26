package com.inditex.priceservice.application.usecase;

import com.inditex.priceservice.domain.exception.PriceNotFoundException;
import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.port.output.PriceRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

/**
 * Unit tests for the PriceUseCaseImpl class.
 */
@ExtendWith(MockitoExtension.class)
class PriceUseCaseImplTest {

    @Mock
    private PriceRepositoryPort priceRepositoryPort;

    @InjectMocks
    private PriceUseCaseImpl priceUseCase;

    @Test
    @DisplayName("Should return price when repository finds one")
    void givenPriceFound_whenFindApplicablePrice_thenReturnsPrice() {
        // Arrange
        Price mockPrice = Price.builder().priority(1).priceValue(new BigDecimal("25.45")).build();
        when(priceRepositoryPort.findHighestPriorityApplicablePrice(eq(35455L), eq(1L), any(LocalDateTime.class)))
                .thenReturn(Optional.of(mockPrice));

        // Act
        Price result = priceUseCase.findApplicablePrice(35455L, 1L, LocalDateTime.now());

        // Assert
        assertNotNull(result, "An applicable price should have been found.");
        assertEquals(1, result.getPriority(), "The priority should match the mock.");
        assertEquals(new BigDecimal("25.45"), result.getPriceValue(), "The price value should match the mock.");
    }

    @Test
    @DisplayName("Should throw PriceNotFoundException when repository returns empty")
    void givenNoPrice_whenFindApplicablePrice_thenThrowsNotFoundException() {
        // Arrange
        when(priceRepositoryPort.findHighestPriorityApplicablePrice(any(Long.class), any(Long.class), any(LocalDateTime.class)))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(PriceNotFoundException.class,
                () -> priceUseCase.findApplicablePrice(99999L, 1L, LocalDateTime.now()),
                "Should throw PriceNotFoundException when the repository returns an empty optional.");
    }
}
