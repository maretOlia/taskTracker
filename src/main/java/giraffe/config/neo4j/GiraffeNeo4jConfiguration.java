package giraffe.config.neo4j;

import giraffe.domain.GiraffeEntity;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.event.BeforeSaveEvent;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.UUID;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Configuration
@EnableTransactionManagement
@ComponentScan(basePackages = {"giraffe"})
@EnableNeo4jRepositories("giraffe.repository")
public class GiraffeNeo4jConfiguration extends Neo4jConfiguration {

    @Override
    public SessionFactory getSessionFactory() {
        return new SessionFactory(getConfiguration(), "giraffe.domain");
    }

    @Bean
    public org.neo4j.ogm.config.Configuration getConfiguration() {
        org.neo4j.ogm.config.Configuration config = new org.neo4j.ogm.config.Configuration();
        config
                .driverConfiguration()
                .setDriverClassName("org.neo4j.ogm.drivers.http.driver.HttpDriver")
                .setURI("http://neo4j:giraffe@localhost:7474");
        return config;
    }


    /**
     * Before saving new {@link GiraffeEntity} do DB check {@link GiraffeEntity#uuid} and {@link GiraffeEntity#timeCreated}
     * and populate this fields they are not specified
     */
    @Bean
    ApplicationListener<BeforeSaveEvent> beforeSaveEventApplicationListener() {
        return new ApplicationListener<BeforeSaveEvent>() {
            @Override
            public void onApplicationEvent(BeforeSaveEvent event) {
                GiraffeEntity entity = (GiraffeEntity) event.getEntity();
                if (entity.getUuid() == null)
                    entity.setUuid(UUID.randomUUID().toString());
                if (entity.getTimeCreated() == null) {
                    entity.setTimeCreated(System.currentTimeMillis() / 100);
                }
            }
        };
    }

}
