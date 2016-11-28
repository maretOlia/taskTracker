package giraffe.context;

import org.neo4j.ogm.session.Session;
import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Guschcyna Olga
 * @version 1.0.0
 */
@Configuration
@EnableNeo4jRepositories("giraffe.repository")
@EnableTransactionManagement
@ComponentScan("giraffe")
@EnableAutoConfiguration
public class PersistenceContext extends Neo4jConfiguration {

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

    @Override
    @Bean
    public Session getSession() throws Exception {
        return super.getSession();
    }

}
