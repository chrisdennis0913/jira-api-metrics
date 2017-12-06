package gov.uscis.vis.api;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import gov.uscis.vis.api.config.Profiles;
import gov.uscis.vis.api.config.SwaggerPropertiesLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;

/**
 * Created by cedennis on 3/10/17.
 */
@Profile(Profiles.APPLICATION)
@SpringBootApplication
public class Application {

    private static Logger logger = LoggerFactory.getLogger(Application.class);

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.addListeners(new SwaggerPropertiesLoader());

        springApplication.run(args);

        logger.info("Jira Metrics API is up and running");
    }

    @Bean
    public JacksonJsonProvider jsonProvider() {
        JacksonJsonProvider jsonProvider = new JacksonJsonProvider();

        jsonProvider.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        return jsonProvider;
    }
}
