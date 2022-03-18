package com.rswy.getopenid.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "appproperties")
public class AppProps {
    private String url;
    private String app;
    private String errKeyUrl;
    private String errBrowserUrl;
}
