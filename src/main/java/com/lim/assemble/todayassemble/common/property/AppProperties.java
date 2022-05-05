package com.lim.assemble.todayassemble.common.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties("app")
public class AppProperties {

    private String host;
    private String fronthost;
}
