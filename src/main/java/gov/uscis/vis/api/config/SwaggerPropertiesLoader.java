package gov.uscis.vis.api.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.event.ApplicationEnvironmentPreparedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.PropertiesPropertySource;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Created by cedennis on 2/7/17.
 */
public class SwaggerPropertiesLoader implements ApplicationListener<ApplicationEnvironmentPreparedEvent> {

    @Override
    public void onApplicationEvent(ApplicationEnvironmentPreparedEvent event) {
        ConfigurableEnvironment env = event.getEnvironment();
        env.getPropertySources().addLast(new PropertiesPropertySource("swagger", createSwaggerProperties(env)));
    }

    private Properties createSwaggerProperties(ConfigurableEnvironment env) {
        Properties properties = new Properties();
        properties.setProperty("swagger.title", "Jira Metrics API");
        properties.setProperty("swagger.description", "Jira Metrics API Documentation");
        properties.setProperty("swagger.version", env.getProperty("info.build.version"));
        properties.setProperty("swagger.base-path", "/jira-metrics-api");
        properties.setProperty("swagger.pretty-print", "true");
        properties.setProperty("swagger.scan", "true");
        properties.setProperty("swagger.contact", "");
        properties.setProperty("swagger.license", "");
        properties.setProperty("swagger.licenseUrl", "");

        // Packages that should be added as resources visible in swagger
        List<String> packagesToAdd = new ArrayList<>();
        packagesToAdd.add("gov.uscis.vis.api");
//        packagesToAdd.add("gov.uscis.visapi.common.batch.resource");
        // Add scan packages
        properties.setProperty("swagger.resourcePackage", StringUtils.join(packagesToAdd, ","));

        return properties;
    }

}
