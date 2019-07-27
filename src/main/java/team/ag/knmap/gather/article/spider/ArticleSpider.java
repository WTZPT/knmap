package team.ag.knmap.gather.article.spider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.JedisPool;
import team.ag.knmap.entity.SpiderInfo;
import team.ag.knmap.entity.Template;
import team.ag.knmap.gather.article.pipeline.DbPipeline;
import team.ag.knmap.gather.article.scheduler.RedisScheduler;
import team.ag.knmap.gather.article.selenium.MySeleniumDownloader;
import us.codecraft.webmagic.Request;
import us.codecraft.webmagic.Spider;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.proxy.Proxy;

import java.applet.AppletContext;
import java.io.Serializable;

/**
 * @Author weitangzhao
 **/
@Component
public class ArticleSpider implements Serializable {
    private static final Logger LOG = LogManager.getLogger(ArticleSpider.class);

    @Autowired
    private DbPipeline dbPipeline;

    @Value("${webdriver.chrome.driver}")
    private String chromeDriverPath;

    @Value("${selenuim_config}")
    private String selenuim_config;

    public String start(String uuid, Template info) {
        Spider spider = makeSpider(uuid,info);
        spider.addPipeline(dbPipeline.setDatabase(info.getDbName(),info.getClassId(),info.getId()));
        if(info.getDynamicSite().equals(ArticleSpiderConstant.DYNAMICCRAWLING)) {
            LOG.info("启用Selenium下载器。");
            spider.setDownloader(new MySeleniumDownloader(chromeDriverPath));
        } else {
            HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
            spider.setDownloader(httpClientDownloader);
        }
        //将第一页的URL加入到scheduler中
        RedisScheduler redisScheduler = new RedisScheduler(new JedisPool("127.0.0.1", 6379));
        String preUrl = redisScheduler.getPreUrl(spider);
        if (preUrl == null) {
            redisScheduler.delSetKey(info.getStartUrl());
            redisScheduler.push(new Request(info.getStartUrl()), spider);
        } else {
            redisScheduler.push(new Request(preUrl), spider);
        }
        spider.setScheduler(redisScheduler);

        spider.start();
        spider.run();

        return uuid;
    }

    /**
     * 生成爬虫
     *
     * @param info 抓取模板
     */
    private Spider makeSpider(String uuid, Template info) {
        Spider spider = new Spider(new ArticlePageProcessor(info)).thread(info.getThread())
                            .setUUID(uuid);
        return spider;                                                                                          
    }
}
