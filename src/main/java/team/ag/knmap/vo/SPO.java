package team.ag.knmap.vo;

import lombok.Data;

/**
 * @Author weitangzhao
 **/


public class SPO {
    private String articleSXpath;
    private String articlePXpath;
    private String articleOXpath;

    public void setArticleSXpath(String articleSXpath){
        this.articleSXpath = articleSXpath;
    }
    public String getArticleSXpath(){
        return this.articleSXpath;
    }
    public void setArticlePXpath(String articlePXpath){
        this.articlePXpath = articlePXpath;
    }
    public String getArticlePXpath(){
        return this.articlePXpath;
    }
    public void setArticleOXpath(String articleOXpath){
        this.articleOXpath = articleOXpath;
    }
    public String getArticleOXpath(){
        return this.articleOXpath;
    }
    @Override
    public String toString() {
        return "SPOVo{" +
                "articleSXpath='" + articleSXpath + '\'' +
                ", articlePXpath='" + articlePXpath + '\'' +
                ", articleOXpath='" + articleOXpath + '\'' +
                '}';
    }
}
