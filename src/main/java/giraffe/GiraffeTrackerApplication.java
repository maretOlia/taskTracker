package giraffe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.config.EnableHypermediaSupport;

@SpringBootApplication
@Import(SharedConfigurationReference.class)
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class GiraffeTrackerApplication {

    public static void main(String[] args) {

        SpringApplication.run(GiraffeTrackerApplication.class, args);

    }

}
