package team.ag.knmap.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import jdk.nashorn.internal.ir.annotations.Ignore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author weitangzhao
 **/
@Data
@TableName("template")
public class Template {
    @Id
    private long id;

    private long classId;

    private String displayName;

    @TableField(exist = false)
    private String dbName;

    private int thread;

    private int retry;

    private int sleep;

    private int maxPageGather;

    private int timeout;

    private String startUrl;

    private String listPageUrlReg;

    //第二层级URL正则 seconde level
    private String slListPageUrlReg;

    //第三层级URL正则
    private String tlListPageUrlReg;

    //第二层级列表的XPATH
    private String SlArticleUrlXpath;

    //第三层级列表的XPATH
    private String TlArticleUrlXpath;

    private String articleUrlReg;

    private String articleUrlXpath;

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

    private String charset;

    private boolean applied;
}
