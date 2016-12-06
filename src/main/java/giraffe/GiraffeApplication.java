package giraffe;

import giraffe.config.security.GiraffeSecurityConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.config.EnableHypermediaSupport;


@SpringBootApplication
@Import({GiraffeSecurityConfig.class})
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
public class GiraffeApplication {

    public static void main(String[] args) {

        SpringApplication.run(GiraffeApplication.class, args);

    }

}
