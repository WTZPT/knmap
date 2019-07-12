package team.ag.knmap.gather.article.pipeline;


import com.franz.agraph.repository.AGRepositoryConnection;
import com.franz.agraph.repository.AGValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.ag.knmap.gather.article.spider.ArticleSpiderConstant;
import team.ag.knmap.service.SpiderService;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;

/**
 * @Author weitangzhao
 **/
@Component
public class DbPipeline implements Pipeline {

    private static final Logger LOG = LogManager.getLogger(DbPipeline.class);

    @Autowired
    SpiderService spiderService;

    @Autowired
    AGRepositoryConnection agRepository;

    private String dbName;
    public DbPipeline setDatabase(String dbName) {
        this.dbName = dbName;
        return this;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {
        AGValueFactory vf = agRepository.getRepository().getValueFactory();
        try {

            String s = resultItems.get(ArticleSpiderConstant.SPO_S).toString();
            String p = resultItems.get(ArticleSpiderConstant.SPO_P).toString();
            String o = resultItems.get(ArticleSpiderConstant.SPO_O).toString();

            LOG.warn(s + p + o);

            IRI SPO_S = vf.createIRI("http://example.org/s/" + s);
            IRI SPO_P = vf.createIRI("http://example.org/ontology/" + p);
            IRI SPO_O = vf.createIRI("http://example.org/o/" + o);
            //agRepository.add(SPO_S, SPO_P, SPO_O);


        }catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
