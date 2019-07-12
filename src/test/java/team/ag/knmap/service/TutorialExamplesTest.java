package team.ag.knmap.service;

import com.franz.agraph.repository.AGCatalog;
import com.franz.agraph.repository.AGRepositoryConnection;
import com.franz.agraph.repository.AGServer;
import com.franz.agraph.repository.AGValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.ag.knmap.KnmapApplicationTests;
import team.ag.knmap.gather.article.pipeline.DbPipeline;

import java.util.List;

import static org.junit.Assert.*;

public class TutorialExamplesTest extends KnmapApplicationTests {
    private static final Logger LOG = LogManager.getLogger(TutorialExamplesTest.class);
    private static final String SERVER_URL ="http://47.103.11.118:10035";
    private static final String CATALOG_ID ="scratch";
    private static final String REPOSITORY_ID = "javatutorial";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "xyzzy";
    @Autowired
    TutorialExamples tutorialExamples ;

    @Test
    public void example1() throws Exception {
        tutorialExamples.example101();
    }

    @Test
    public void example00() throws  Exception{
        AGServer server = new AGServer(SERVER_URL,USERNAME,PASSWORD);
        AGCatalog catalog = server.getCatalog();
        catalog.deleteRepository("test1");
        catalog.createRepository("test1");
       List<String> list =  catalog.listRepositories();
        for(String str : list) {
            LOG.info("name： "+str);
        }
    }

    @Autowired
    AGRepositoryConnection agRepository;

    @Test
    public void example11() throws Exception {
        AGValueFactory vf = agRepository.getRepository().getValueFactory();

        String[] s = {"深圳中国农大科技股份有限公司","深圳中国农大科技股份有限公司","深圳中国农大科技股份有限公司","深圳中国农大科技股份有限公司","深圳中国农大科技股份有限公司","深圳中国农大科技股份有限公司","深圳中国农大科技股份有限公司"};
        String[] p = {"董事长","财务总监","总经理","董事会秘书","前十大股东","前十大股东","前十大股东"};
        String[] o = {"黄翔","徐文苏","黄翔","徐文苏","深圳中农大科技投资有限公司","中科汇通(深圳)股权投资基金有限公司","国华人寿保险股份有限公司-万能三号"};

        for(int i = 0; i < 7;i++) {
            LOG.warn(s[i] + " " + p[i] + " " +o[i]);
            IRI SPO_S = vf.createIRI("http://example.org/s/" + s[i]);
            IRI SPO_P = vf.createIRI("http://example.org/ontology/" + p[i]);
            IRI SPO_O = vf.createIRI("http://example.org/o/" + o[i]);

            agRepository.add(SPO_S, SPO_P, SPO_O);
        }

     }


    @Test
    public void example222() throws Exception {
        //agRepository.remove(null, null, null);
        RepositoryResult<Statement> result = agRepository.getStatements(null, null, null, false);
        while (result.hasNext()) {
            Statement st = result.next();
            LOG.info(st);
        }
    }
}