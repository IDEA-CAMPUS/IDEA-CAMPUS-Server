package depth.main.ideac;

import depth.main.ideac.global.config.YamlPropertySourceFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@PropertySource(value = { "classpath:oauth2/application-oauth2.yml" }, factory = YamlPropertySourceFactory.class)
@SpringBootApplication
public class IdeacUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(IdeacUserApplication.class, args);
    }
}