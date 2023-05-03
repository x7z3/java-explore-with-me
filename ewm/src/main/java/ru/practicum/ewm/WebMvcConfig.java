package ru.practicum.ewm;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@Component
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    private final StatsReportingInterceptor statsReportingInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(statsReportingInterceptor);
    }
}
