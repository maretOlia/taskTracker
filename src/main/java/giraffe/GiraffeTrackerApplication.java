package giraffe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

/**
 * @author Olga Gushchyna
 * @version 0.0.1
 */
@SpringBootApplication
@Import(SharedConfigurationReference.class)
public class GiraffeTrackerApplication {

    public static void main(String[] args) {

        SpringApplication.run(GiraffeTrackerApplication.class, args);

    }

}
