package com.inditex.priceservice.infrastructure.config;

import com.inditex.priceservice.application.usecase.PriceUseCaseImpl;
import com.inditex.priceservice.domain.port.input.PriceUseCase;
import com.inditex.priceservice.domain.port.output.PriceRepositoryPort;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public PriceUseCase priceUseCase(PriceRepositoryPort priceRepositoryPort) {
        return new PriceUseCaseImpl(priceRepositoryPort);
    }
}
