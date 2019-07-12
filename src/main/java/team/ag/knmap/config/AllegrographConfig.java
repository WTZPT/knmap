package team.ag.knmap.config;

import com.franz.agraph.repository.AGCatalog;
import com.franz.agraph.repository.AGRepository;
import com.franz.agraph.repository.AGRepositoryConnection;
import com.franz.agraph.repository.AGServer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author weitangzhao
 **/
@Configuration
public class AllegrographConfig {
    private static final String SERVER_URL ="http://47.103.11.118:10035";
    private static final String CATALOG_ID ="scratch";
    private static final String REPOSITORY_ID = "test1";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "xyzzy";

    @Bean
    public AGRepositoryConnection AGRepositoryConnection(){
        AGServer server = new AGServer(SERVER_URL,USERNAME,PASSWORD);
        AGCatalog catalog = server.getCatalog();
        AGRepository myRepository = catalog.openRepository(REPOSITORY_ID);

        AGRepositoryConnection conn = myRepository.getConnection();
        return conn;
    }

    @Bean
    public AGCatalog AGCatalog(){
        AGServer server = new AGServer(SERVER_URL,USERNAME,PASSWORD);
        AGCatalog catalog = server.getCatalog();
        return catalog ;
    }
}
