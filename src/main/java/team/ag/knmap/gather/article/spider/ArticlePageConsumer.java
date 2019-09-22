package team.ag.knmap.gather.article.spider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import team.ag.knmap.entity.SpiderInfo;
import team.ag.knmap.entity.Template;
import team.ag.knmap.gather.commons.PageConsumer;
import team.ag.knmap.util.TextParser;
import us.codecraft.webmagic.Page;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * @Author
 **/
public class ArticlePageConsumer implements PageConsumer {

    private static final Logger LOG = LogManager.getLogger(ArticlePageConsumer.class);

    private static ArticleSpiderParser parser = new ArticleSpiderParser();

    @Override
    public void accept(Page page, Template info) {
        page.putField("dbName",info.getDbName());
        try{
            //一级列表页
            if(page.getUrl().regex(info.getListPageUrlReg()).match()){
                resolveListPage(page,info.getListPageUrlReg(),info.getArticleUrlXpath(),info.getArticleUrlReg());
                LOG.info("Skip FirstLevelURL: "+page.getUrl());
            }else if(!isBlank(info.getSlListPageUrlReg()) && page.getUrl().regex(info.getSlListPageUrlReg()).match()) {
                //二级列表页
                resolveListPage(page,info.getSlListPageUrlReg(),info.getSlArticleUrlXpath(),info.getArticleUrlReg());
                LOG.info("Skip SecondLevelURL: "+page.getUrl());
            } else if(!isBlank(info.getTlListPageUrlReg())&& page.getUrl().regex(info.getTlListPageUrlReg()).match()) {
                //三级列表页
                resolveListPage(page,info.getTlListPageUrlReg(),info.getTlListPageUrlReg(),info.getArticleUrlReg());
                LOG.info("Skip ThirtLevelURL: "+page.getUrl());
            }

            //抽取详情页面
            if(page.getUrl().regex(info.getArticleUrlReg()).match()){
                //获取三元组
                resolverSPO(page,info);
                LOG.info("Crawl- URL: "+page.getUrl());
            }else {
                page.setSkip(true);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 处理列表页面
     * @param page
     * @param listPageUrlReg
     * @param articleUrlReg
     * @param articleUrlXpath
     */
    private static void resolveListPage(Page page,String listPageUrlReg,String articleUrlXpath,String articleUrlReg){
        //使用正则表达式来寻找下一页
        List<String> nextPageUrls = parser.getListNextPageUrls(
                page,listPageUrlReg,
                ArticleSpiderConstant.MATCH_TYPE_REG
        );
        if(nextPageUrls!=null){
            page.addTargetRequests(nextPageUrls);
        }
        List<String> pageUrls = null;
        // zsz：如果是列表页，提取列表中的文章url
        if (!isBlank(articleUrlXpath)){
            pageUrls = parser.getListPageUrls(
                    page,articleUrlXpath,
                    ArticleSpiderConstant.MATCH_TYPE_XPATH
            );
        } else {
            // zsz：如果没有设置xpath就使用正则表达式来获取
            //根据文章网址的正则表达式来获取
            pageUrls = parser.getListPageUrls(
                    page,articleUrlReg,
                    ArticleSpiderConstant.MATCH_TYPE_REG
            );
        }
        if( pageUrls!=null ){
            page.addTargetRequests(pageUrls);
        }
    }

    /**
     * 处理页面，抽取三元组
     * 根据 xpathList中的xpath依次获取页面上三元组，用“~”做为切割标识
     * @param page
     * @param info
     *
     */
    private static void resolverSPO(Page page,Template info) {

        String[] sxpathList = TextParser.splitMatchWithWaterLine(info.getArticleSXpath());
        String[] pxpathList = TextParser.splitMatchWithWaterLine(info.getArticlePXpath());
        String[] oxpathList = TextParser.splitMatchWithWaterLine(info.getArticleOXpath());
        String sobject = "";
        String pobject = "";
        String oobject = "";
        String objects,objectp,objecto;
        boolean flag = false;
        for(int i = 0; i < sxpathList.length; i++) {
            if(!isBlank(sxpathList[i] ) && !isBlank(pxpathList[i] ) && !isBlank(pxpathList[i])  ) {
                objects =  parser.getContent(page,sxpathList[i],ArticleSpiderConstant.MATCH_TYPE_XPATH);
                objectp =  parser.getContent(page,pxpathList[i],ArticleSpiderConstant.MATCH_TYPE_XPATH);
                objecto =  parser.getContent(page,oxpathList[i],ArticleSpiderConstant.MATCH_TYPE_XPATH);
                   if(!isBlank(objects) && !isBlank(objecto) && !isBlank(objectp)) {
                       if(flag) {
                           sobject+="~";
                           pobject+="~";
                           oobject+="~";
                       } else {
                           flag = true;
                       }
                       sobject += objects;
                       pobject += objectp;
                       oobject += objecto;
                   }
            }
        }
        page.putField(ArticleSpiderConstant.SPO_S,sobject);
        page.putField(ArticleSpiderConstant.SPO_P,pobject);
        page.putField(ArticleSpiderConstant.SPO_O,oobject);
    }



}
