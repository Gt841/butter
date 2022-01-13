package com.rswy.getopenid.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wxmpproperties")
public class WXMPProps {
    private String appId;
    private String appKey;
}
