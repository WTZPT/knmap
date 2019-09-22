package team.ag.knmap.gather.article.pipeline;

import com.franz.agraph.repository.AGRepositoryConnection;
import com.franz.agraph.repository.AGValueFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.model.IRI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.ag.knmap.entity.Triad;
import team.ag.knmap.gather.article.spider.ArticleSpiderConstant;
import team.ag.knmap.service.SpiderService;
import team.ag.knmap.service.TriadService;
import team.ag.knmap.util.TextParser;
import us.codecraft.webmagic.ResultItems;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.Pipeline;
import static org.apache.commons.lang3.StringUtils.isBlank;
/**
 * @Author weitangzhao
 **/
@Component
public class DbPipeline implements Pipeline {

    private static final Logger LOG = LogManager.getLogger(DbPipeline.class);

    @Autowired
    SpiderService spiderService;



    @Autowired
    TriadService triadService;

    private String dbName;
    private Long classId;
    private Long templateId;

    public DbPipeline setDatabase(String dbName,Long classId,Long templateId) {
        this.dbName = dbName;
        this.classId = classId;
        this.templateId = templateId;
        return this;
    }

    @Override
    public void process(ResultItems resultItems, Task task) {

        try {
            String s = resultItems.get(ArticleSpiderConstant.SPO_S).toString();
            String p = resultItems.get(ArticleSpiderConstant.SPO_P).toString();
            String o = resultItems.get(ArticleSpiderConstant.SPO_O).toString();
            if(!isBlank(s) && !isBlank(p) && !isBlank(o)) {
                String[] sList = TextParser.splitMatchWithWaterLine(s);
                String[] pList = TextParser.splitMatchWithWaterLine(p);
                String[] oList = TextParser.splitMatchWithWaterLine(o);
                for(int i = 0; i < sList.length; i++){
                    LOG.info(TextParser.getNewContent(sList[i])+"  "+TextParser.getNewContent(pList[i])+"  "+TextParser.getNewContent(oList[i]));
                    Triad triad = new Triad(classId,templateId,TextParser.getNewContent(sList[i]),
                            TextParser.getNewContent(pList[i]),TextParser.getNewContent(oList[i]),false);
                    triadService.save(triad);
                }
            }
        }catch (Exception e) {
            LOG.error(e.getMessage());
        }
    }
}
