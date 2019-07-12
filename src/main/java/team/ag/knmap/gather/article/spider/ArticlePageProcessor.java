package team.ag.knmap.gather.article.spider;

import org.apache.commons.lang3.StringUtils;
import team.ag.knmap.entity.SpiderInfo;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;

/**
 * @author panzejia
 **/
public class ArticlePageProcessor implements PageProcessor {

    private Site site;

    private SpiderInfo info;

    private ArticlePageConsumer articlePageConsumer = new ArticlePageConsumer();
    ArticlePageProcessor(SpiderInfo info){
        this.site = Site.me().setTimeOut(100000)
                .setRetrySleepTime(info.getRetry()).setSleepTime(info.getSleep())
                .setCharset(StringUtils.isBlank(info.getCharset()) ? null : info.getCharset());
        this.info = info;
    }

    @Override
    public void process(Page page) {
        articlePageConsumer.accept(page, info);
    }

    @Override
    public Site getSite() {
        return site;
    }
}
