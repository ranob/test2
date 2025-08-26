package com.inditex.priceservice.infrastructure.adapter.output.persistence;

import com.inditex.priceservice.domain.model.Price;
import com.inditex.priceservice.infrastructure.adapter.output.persistence.entity.PriceEntity;
import org.springframework.stereotype.Component;

@Component
public class PricePersistenceMapper {

    public Price toDomain(PriceEntity entity) {
        if (entity == null) {
            return null;
        }

        return Price.builder()
                .productId(entity.getProductId())
                .brandId(entity.getBrandId())
                .priceList(entity.getPriceList())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .priceValue(entity.getPriceValue())
                .priority(entity.getPriority())
                .currency(entity.getCurrency())
                .build();
    }

    public PriceEntity toEntity(Price domain) {
        if (domain == null) {
            return null;
        }

        PriceEntity entity = new PriceEntity();
        entity.setProductId(domain.getProductId());
        entity.setBrandId(domain.getBrandId());
        entity.setPriceList(domain.getPriceList());
        entity.setStartDate(domain.getStartDate());
        entity.setEndDate(domain.getEndDate());
        entity.setPriceValue(domain.getPriceValue());
        entity.setPriority(domain.getPriority());
        entity.setCurrency(domain.getCurrency());
        return entity;
    }
}
