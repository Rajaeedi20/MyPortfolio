package com.rajarao.portfolio.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "portfolio.profile")
public record ProfileProperties(
        String name, String title, String tagline, String company, String location,
        String email, String phone, String linkedin, String github, String summary) {
}
