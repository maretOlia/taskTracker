package giraffe.context;

import org.neo4j.ogm.session.SessionFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.neo4j.config.Neo4jConfiguration;
import org.springframework.data.neo4j.repository.config.EnableNeo4jRepositories;
import org.springframework.data.neo4j.server.InProcessServer;
import org.springframework.data.neo4j.server.Neo4jServer;
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
        return new SessionFactory("giraffe.domain");
    }

    @Bean
    public Neo4jServer neo4jServer() {
        return new InProcessServer();
       // return new RemoteServer("http://localhost:7474", "neo4j", "giraffe");
    }

}
