package com.fredfonseca.bookstoremanager.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.data.web.config.PageableHandlerMethodArgumentResolverCustomizer;

@Configuration
public class WebConfiguration implements PageableHandlerMethodArgumentResolverCustomizer {

    @Override
    public void customize(PageableHandlerMethodArgumentResolver pageableResolver) {
        pageableResolver.setFallbackPageable(Pageable.unpaged());
    }

}