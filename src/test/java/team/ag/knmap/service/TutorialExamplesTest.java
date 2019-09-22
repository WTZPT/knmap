package team.ag.knmap.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.franz.agraph.repository.AGCatalog;
import com.franz.agraph.repository.AGRepositoryConnection;
import com.franz.agraph.repository.AGServer;
import com.franz.agraph.repository.AGValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.IRI;
import org.eclipse.rdf4j.model.Literal;
import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.model.vocabulary.RDF;
import org.eclipse.rdf4j.repository.RepositoryResult;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.ag.knmap.KnmapApplicationTests;
import team.ag.knmap.commom.AGDBConnection;
import team.ag.knmap.entity.Triad;
import team.ag.knmap.gather.article.pipeline.DbPipeline;

import java.sql.Wrapper;
import java.util.List;

import static org.junit.Assert.*;
import static org.apache.commons.lang3.StringUtils.isBlank;
public class TutorialExamplesTest extends KnmapApplicationTests {
    private static final Logger LOG = LogManager.getLogger(TutorialExamplesTest.class);
    private static final String SERVER_URL ="http://47.103.11.118:10035";
    private static final String CATALOG_ID ="scratch";
    private static final String REPOSITORY_ID = "javatutorial";
    private static final String USERNAME = "test";
    private static final String PASSWORD = "xyzzy";
    @Autowired
    TutorialExamples tutorialExamples ;

    @Autowired
    TriadService triadService;

    @Test
    public void example1() throws Exception {
        tutorialExamples.example101();
    }

    @Test
    public void example00() throws  Exception{
        AGServer server = new AGServer(SERVER_URL,USERNAME,PASSWORD);
        AGCatalog catalog = server.getCatalog();

        catalog.createRepository("86a3a30aac76da66");
        LOG.info("完成 ");
       List<String> list =  catalog.listRepositories();
        for(String str : list) {
            LOG.info("name： "+str);
        }
    }

    @Autowired
    AGRepositoryConnection agRepository;

    @Autowired
    AGDBConnection agdbConnection;


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

    @Test
    public void dataProcess() throws Exception{
        QueryWrapper<Triad> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.lambda()
                .eq(Triad::isInserted,true);
        IPage<Triad> triadIPage = triadService.page(new Page<Triad>(4,2),templateQueryWrapper);
        AGValueFactory vf = agRepository.getRepository().getValueFactory();
        for(Triad triad : triadIPage.getRecords()) {
            String o =  triad.getSpoO();
            String p = triad.getSpoP();
            String s = triad.getSpoS();

            if(!isBlank(o)) {
                IRI SPO_S = vf.createIRI("http://example.org/company/"+ s);
                IRI SPO_P = vf.createIRI("http://example.org/ontology/" + p);
                IRI SPO_O ;
                if(p == "所属地域"){
                    SPO_O  = vf.createIRI("http://example.org/area/" + o);
                } else if(p == "英文名称" || p == "股票简称"){
                    SPO_O  = vf.createIRI("http://example.org/name/" + o);
                } else {
                    SPO_O  = vf.createIRI("http://example.org/people/" + o);
                }
                agRepository.add(SPO_S, SPO_P, SPO_O);
                LOG.info(s+"  "+p+"  "+o);
            }

        }
    }

    @Test
    public void dataProcess1() {
        QueryWrapper<Triad> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.lambda()
                .eq(Triad::getSpoP,"参股控股公司");

        IPage<Triad> triadIPage = triadService.page(new Page<Triad>(4,2),templateQueryWrapper);
        AGValueFactory vf = agRepository.getRepository().getValueFactory();
        for(Triad triad : triadIPage.getRecords()) {
            String o =  triad.getSpoO();
            String p = triad.getSpoP();
            String s = triad.getSpoS();
            //LOG.info(s+"\n"+o+"\n\n");
            if(!isBlank(o)&&!isBlank(s)) {
               IRI SPO_S = vf.createIRI("http://example.org/company/"+ s);
                IRI SPO_P = vf.createIRI("http://example.org/ontology/" + p);
                IRI SPO_O = vf.createIRI("http://example.org/company/" +o);
             //   Literal SPO_O = vf.createLiteral(o);
                agRepository.add(SPO_S, SPO_P, SPO_O);

                LOG.info(s+"  "+p+"  "+o);
            }
        }
    }


    @Test
    public void dataProcess2() {
        QueryWrapper<Triad> templateQueryWrapper = new QueryWrapper<>();
        AGValueFactory vf = agRepository.getRepository().getValueFactory();
        templateQueryWrapper.lambda()
                .eq(Triad::getSpoP,"十大股东");
        IPage<Triad> triadIPage = triadService.page(new Page<Triad>(4,2),templateQueryWrapper);
        for(Triad triad : triadIPage.getRecords()) {
            String o =  triad.getSpoO();
            String p = triad.getSpoP();
            String s = triad.getSpoS();
            IRI SPO_O;
            if(!isBlank(o)&&!isBlank(s)) {
                LOG.info(s+"  "+p+"  "+o);
                IRI SPO_S = vf.createIRI("http://example.org/company/"+ s);
                IRI SPO_P = vf.createIRI("http://example.org/ontology/" + p);
                if(o.length() <= 4)
                     SPO_O = vf.createIRI("http://example.org/people/"+ o);
                else
                    SPO_O = vf.createIRI("http://example.org/company/"+ o);
                agRepository.add(SPO_S, SPO_P, SPO_O);
                LOG.info(s+"  "+p+"  "+o);
            }
        }
    }

    @Test
    public void dataProcess3() {
        AGRepositoryConnection conn = agdbConnection.create("86a3a30aac76da66");
        AGValueFactory vf = conn.getValueFactory();
        QueryWrapper<Triad> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.lambda()
                .eq(Triad::getClassId,133);

        IPage<Triad> triadIPage = triadService.page(new Page<Triad>(4,2),templateQueryWrapper);
        for(Triad triad : triadIPage.getRecords()) {
            String o =  triad.getSpoO();
            String p = triad.getSpoP();
            String s = triad.getSpoS();

            if(!isBlank(o)&&!isBlank(s)) {
                IRI SPO_S = vf.createIRI("http://example.org/place/"+ s);
                IRI SPO_P = vf.createIRI("http://example.org/place/" + p);
                IRI SPO_O = vf.createIRI("http://example.org/place/" + o);
                conn.add(SPO_S, SPO_P, SPO_O);
                LOG.info(s+"  "+p+"  "+o);
            }
        }

    }

}