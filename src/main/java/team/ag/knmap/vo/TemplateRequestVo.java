package team.ag.knmap.vo;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @Author weitangzhao
 **/

@Data
public class TemplateRequestVo {
    private String classId;

    private String displayName;

    private String startUrl;

    private String listPageUrlReg;

    private String preViewArticleZoneUrl;

    private String articleUrlReg;

    private String articleUrlXpath;

    private String preViewArticleUrl;

    //第二层级URL正则 seconde level
    private String slListPageUrlReg;

    //第三层级URL正则
    private String tlListPageUrlReg;

    //第二层级列表的XPATH
    private String slArticleUrlXpath;

    //第三层级列表的XPATH
    private String tlArticleUrlXpath;

    private List<SPO> spo;

    private String thread;

    private String retry;

    private String sleep;

    private String maxPageGather;

    private String timeout;

    private String gatherFirstPage;

    private String dynamicSite;

    private String charset;


    @Override
    public String toString() {
        return "TemplateRequestVo{" +
                " classId=" + classId +
                ", displayName='" + displayName + '\'' +
                ", thread=" + thread +
                ", retry=" + retry +
                ", sleep=" + sleep +
                ", maxPageGather=" + maxPageGather +
                ", timeout=" + timeout +
                ", startUrl='" + startUrl + '\'' +
                ", listPageUrlReg='" + listPageUrlReg + '\'' +
                ", articleUrlReg='" + articleUrlReg + '\'' +
                ", articleUrlXpath='" + articleUrlXpath + '\'' +
                ", charset='" + charset + '\'' +
                ", dynamicSite=" + dynamicSite +
                ", spo=" + spo+
                '}';
    }
}
