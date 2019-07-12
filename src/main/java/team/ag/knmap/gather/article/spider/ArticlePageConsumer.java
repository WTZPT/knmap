package team.ag.knmap.gather.article.spider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.ag.knmap.entity.SpiderInfo;
import team.ag.knmap.gather.commons.PageConsumer;
import team.ag.knmap.util.TextParser;
import us.codecraft.webmagic.Page;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @Author weitangzhao
 **/
public class ArticlePageConsumer implements PageConsumer {

    private static final Logger LOG = LogManager.getLogger(ArticlePageConsumer.class);

    private static ArticleSpiderParser parser = new ArticleSpiderParser();

    @Override
    public void accept(Page page, SpiderInfo info) {
        page.putField("dbName",info.getDbName());
        try{
            //列表页
            if(page.getUrl().regex(info.getListPageUrlReg()).match()){
                resolveListPage(page,info);
                page.setSkip(true);
                LOG.info("Skip URL: "+page.getUrl());
            } else {
                //获取三元组
                resolverSPO(page,info);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理列表页面
     * @param page
     * @param info
     */
    private static void resolveListPage(Page page,SpiderInfo info){
        //使用正则表达式来寻找下一页
        List<String> nextPageUrls = parser.getListNextPageUrls(
                page,info.getListPageUrlReg(),
                ArticleSpiderConstant.MATCH_TYPE_REG
        );
        if(nextPageUrls!=null){
            page.addTargetRequests(nextPageUrls);
        }
        List<String> pageUrls = null;
        // zsz：如果是列表页，提取列表中的文章url
        if (!isBlank(info.getArticleUrlXpath())){
            pageUrls = parser.getListPageUrls(
                    page,info.getArticleUrlXpath(),
                    ArticleSpiderConstant.MATCH_TYPE_XPATH
            );
        } else {
            // zsz：如果没有设置xpath就使用正则表达式来获取
            //根据文章网址的正则表达式来获取
            pageUrls = parser.getListPageUrls(
                    page,info.getArticleUrlReg(),
                    ArticleSpiderConstant.MATCH_TYPE_REG
            );
        }
        if( pageUrls!=null ){
            page.addTargetRequests(pageUrls);
        }
    }

    /**
     * 处理页面，抽取三元组
     * @param page
     * @param info
     *
     */
    private static void resolverSPO(Page page,SpiderInfo info) {

        String[] sxpathList = TextParser.splitMatchWithWaterLine(info.getArticleSXpath());
        String[] pxpathList = TextParser.splitMatchWithWaterLine(info.getArticlePXpath());
        String[] oxpathList = TextParser.splitMatchWithWaterLine(info.getArticleOXpath());
        String sobject = "";
        String pobject = "";
        String oobject = "";
        for(int i = 0; i < sxpathList.length; i++) {
            if(!isBlank(sxpathList[i] ) && !isBlank(pxpathList[i] ) && !isBlank(pxpathList[i])  ) {
                   sobject += parser.getContent(page,sxpathList[i],ArticleSpiderConstant.MATCH_TYPE_XPATH);
                   pobject += parser.getContent(page,pxpathList[i],ArticleSpiderConstant.MATCH_TYPE_XPATH);
                   oobject += parser.getContent(page,oxpathList[i],ArticleSpiderConstant.MATCH_TYPE_XPATH);
                   if(i == sxpathList.length - 1) {break;}
                   sobject+="~";
                   pobject+="~";
                   oobject+="~";
            }
        }
        page.putField(ArticleSpiderConstant.SPO_S,sobject);
        page.putField(ArticleSpiderConstant.SPO_P,pobject);
        page.putField(ArticleSpiderConstant.SPO_O,oobject);
    }

    private static void obtainSPO(Page page,String xpath,String SPO,SpiderInfo info){
        String Object = "";
        if(!isBlank(info.getArticleSXpath())) {
            Object = parser.getContent(page,
                    info.getArticleSXpath(),
                    ArticleSpiderConstant.MATCH_TYPE_XPATH);
        }else if(!isBlank(info.getArticleSReg())) {
            Object = parser.getContent(page,
                    info.getArticleSReg(),
                    ArticleSpiderConstant.MATCH_TYPE_REG);
        }

        page.putField(SPO,Object);
    }

}
