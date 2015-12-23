package giraffe.config.neo4j;

import giraffe.domain.GiraffeEntity;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.event.BeforeSaveEvent;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.Neo4jServer;
import org.springframework.data.neo4j.server.RemoteServer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import java.util.UUID;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@EnableTransactionManagement
@EnableAutoConfiguration
@ComponentScan(basePackages = {"giraffe"})
@Configuration
@EnableNeo4jRepositories("giraffe.repository")
public class GiraffeNeo4jConfiguration extends Neo4jConfiguration {

    @Override
    public Neo4jServer neo4jServer() {
        //return new InProcessServer(); //TODO change this to external one
        return new RemoteServer("http://localhost:7474", "neo4j", "giraffe");
    }

    @Override
    public SessionFactory getSessionFactory() {
        return new SessionFactory("giraffe.domain");
    }

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
