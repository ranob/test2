package com.inditex.priceservice.infrastructure.adapter.input.web;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.infrastructure.adapter.input.web.dto.PriceResponse;
import org.springframework.stereotype.Component;

@Component
public class PriceWebMapper {

    public PriceResponse toResponse(Price price) {
        if (price == null) {
            return null;
        }

        return new PriceResponse(
                price.getProductId(),
                price.getBrandId(),
                price.getPriceList(),
                price.getStartDate(),
                price.getEndDate(),
                price.getPriceValue()
        );
    }
}
