package com.testows.config

import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer

@Configuration
class MvcConfig: WebMvcConfigurer {
    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler(SecurityConstants.STATIC_DIR)
                .addResourceLocations("classpath:/static/")
    }

    override fun addCorsMappings(registry: CorsRegistry) {
        registry.addMapping("/**")
    }
}