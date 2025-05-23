package com.Pharmacy.Pharmacy.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Configuration class to serve the React frontend from the Spring Boot application.
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Serve static resources from the frontend build directory
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/");
    }

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        // Forward requests to the index.html for client-side routing
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("/{x:[\\w\\-]+}").setViewName("forward:/index.html");
        registry.addViewController("/{x:^(?!api$).*$}/**").setViewName("forward:/index.html");
    }
}