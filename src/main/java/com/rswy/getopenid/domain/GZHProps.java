package com.rswy.getopenid.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "gzhproperties")
@Data
public class GZHProps {

    private String servUrl;
    private String reUrl;
    private String appId;
    private String appKey;


}
