package team.ag.knmap.commom;

import com.franz.agraph.repository.AGCatalog;
import com.franz.agraph.repository.AGRepository;
import com.franz.agraph.repository.AGRepositoryConnection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.util.HashMap;

/**
 * @Author weitangzhao
 **/

@Component
public class AGDBConnection {

    private static HashMap<String, AGRepositoryConnection> connections = new HashMap<String,AGRepositoryConnection>();

    @Autowired
    AGCatalog agCatalog;

    public AGRepositoryConnection create(String dbName){
            if(connections.containsKey(dbName))
            {
                return connections.get(dbName);
            }

            try{
                AGRepository myRepository = agCatalog.openRepository(dbName);
                AGRepositoryConnection conn = myRepository.getConnection();
                connections.put(dbName,conn);
                return conn;
            }catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }

    public void close(String dbname){
        if(connections.containsKey(dbname)){
                connections.get(dbname).close();
        }
    }

}
