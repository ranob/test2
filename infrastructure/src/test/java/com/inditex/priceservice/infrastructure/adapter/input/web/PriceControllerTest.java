package com.inditex.priceservice.infrastructure.adapter.input.web;

import com.inditex.priceservice.domain.exception.PriceNotFoundException;
import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.domain.port.input.PriceUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PriceController.class)
@Import({PriceControllerTestConfig.class, PriceWebMapper.class, GlobalExceptionHandler.class})
class PriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PriceUseCase priceUseCase;

    @Test
    @DisplayName("Should return 200 OK with price data when a price is found")
    void givenPriceFound_whenGetApplicablePrice_thenReturns200Ok() throws Exception {
        // Initialize data
        LocalDateTime testDate = LocalDateTime.parse("2020-06-14T10:00:00");
        Price mockPrice = Price.builder()
                .productId(35455L)
                .brandId(1L)
                .priceList(1)
                .startDate(testDate.minusDays(1))
                .endDate(testDate.plusDays(1))
                .priceValue(new BigDecimal("35.50"))
                .build();

        when(priceUseCase.findApplicablePrice(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenReturn(mockPrice);

        // Test the endpoint
        mockMvc.perform(get("/api/prices/applicable")
                        .queryParam("applicationDate", "2020-06-14T10:00:00")
                        .queryParam("productId", "35455")
                        .queryParam("brandId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.productId").value(35455))
                .andExpect(jsonPath("$.finalPrice").value(35.50));
    }

    @Test
    @DisplayName("Should return 404 Not Found when PriceNotFoundException is thrown")
    void givenPriceNotFound_whenGetApplicablePrice_thenReturns404NotFound() throws Exception {
        // Arrange
        when(priceUseCase.findApplicablePrice(anyLong(), anyLong(), any(LocalDateTime.class)))
                .thenThrow(new PriceNotFoundException("Test exception"));

        // Act & Assert
        mockMvc.perform(get("/api/prices/applicable")
                        .queryParam("applicationDate", "2020-06-14T10:00:00")
                        .queryParam("productId", "35455")
                        .queryParam("brandId", "1"))
                .andExpect(status().isNotFound());
    }
}
