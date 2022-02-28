package com.rswy.getopenid.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "appproperties")
public class AppProps {
    private String url;
    private String app;
}
