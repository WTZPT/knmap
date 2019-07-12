package team.ag.knmap.gather.article.spider;

import team.ag.knmap.util.TextParser;
import us.codecraft.webmagic.Page;


import java.util.List;


/**
 * Created by panzejia on 2019/2/20
 * Project : gatherplatform
 */
public class ArticleSpiderParser {

    public List<String> getListNextPageUrls (Page page, String match, String matchType){
        switch (matchType){
            case ArticleSpiderConstant.MATCH_TYPE_REG:
                return page.getHtml().regex(match).all();
             default:
        }
        return null;
    }

    public List<String> getListPageUrls (Page page, String match, String matchType){
        switch (matchType){
            case ArticleSpiderConstant.MATCH_TYPE_XPATH:
                System.out.println(page.getHtml().xpath(match).links().all());
                return page.getHtml().xpath(match).links().all();
            case ArticleSpiderConstant.MATCH_TYPE_REG:
                return page.getHtml().regex(match).all();
            default:
        }
        return null;
    }

    public String getContent(Page page, String match, String matchType){
        String content = null;
        switch(matchType) {
            case ArticleSpiderConstant.MATCH_TYPE_XPATH:
                for(String m: TextParser.splitMatch(match)){
                    content = page.getHtml().xpath(m).get();
                    if(content != null){ return content;}
                }
                break;
            case ArticleSpiderConstant.MATCH_TYPE_REG:
                for(String m:TextParser.splitMatch(match)){
                    content = page.getHtml().regex(m).get();
                    if(content != null){return content;}
                }
                break;
                default:
        }
        return null;
    }




}
