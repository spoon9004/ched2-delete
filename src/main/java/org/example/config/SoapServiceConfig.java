package org.example.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "soap.service")
@Getter
@Setter
public class SoapServiceConfig {

    private String url;
    private String username;
    private String password;
    private String uuidFilePath;


}
