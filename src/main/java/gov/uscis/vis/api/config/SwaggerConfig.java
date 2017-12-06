package gov.uscis.vis.api.config;

import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by cedennis on 2/7/17.
 */
@Configuration
public class SwaggerConfig {

    @Bean
    @ConfigurationProperties(prefix = "swagger")
    public Swagger2Feature swagger2Feature() {
        return new Swagger2Feature();
    }


}
