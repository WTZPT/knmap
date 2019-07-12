package team.ag.knmap.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author weitangzhao
 **/
@Data
@TableName("spider_info")
public class SpiderInfo implements Serializable {

    @TableId
    private long id ;

    private String name;

    private String displayName;

    /**
     * 数据库名
     */
    private String dbName;

    /**
     * 抓取线程数
     */
    private Integer thread;

    /**
     * 失败的网页重试次数
     */
    private Integer retry;

    /**
     * 抓取每个网页睡眠时间
     */
    private Integer sleep;

    /**
     * 最大抓取网页数量,0代表不限制
     */
    private Integer maxPageGather;

    /**
     * HTTP链接超时时间
     */
    private Integer timeout;


    /**
     * 起始页面
     */
    private String startUrl;

    /**
     * 下一页链接的Xpath
     */
    private String listPageUrlReg;

    /**
     * 正文链接的正则表达式，用于发现正文地址
     */
    private String articleUrlReg;

    /**
     * 列表的XPATH
     */
    private String articleUrlXpath;

    /**
     * 编码
     */
    private String charset;

    /**
     * 是否是动态网站，如果是则使用seleniumDownloader
     */
    private Integer dynamicSite;

    private String articleSXpath;

    private String articleSReg;

    private String articleOXpath;

    private String articleOReg;

    private String articlePXpath;

    private String articlePReg;

    @Override
    public String toString() {
        return "SpiderInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", displayName='" + displayName + '\'' +
                ", dbName='" + dbName + '\'' +
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
                ", articleSXpath='" + articleSXpath + '\'' +
                ", articleSRep='" + articleSReg + '\'' +
                ", articleOXpath='" + articleOXpath + '\'' +
                ", articleORep='" + articleOReg + '\'' +
                ", articlePXpath='" + articlePXpath + '\'' +
                ", articlePRep='" + articlePReg + '\'' +
                '}';
    }
}
