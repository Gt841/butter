package com.rswy.getopenid.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "zfbproperties")
public class ZFBProps {
    private String appId;
    private String priKey;
    private String pubKey;
    private String signType;
    private String servUrl;
    private String reUrl;
}
