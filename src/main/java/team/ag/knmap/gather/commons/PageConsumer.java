package team.ag.knmap.gather.commons;

import team.ag.knmap.entity.SpiderInfo;
import team.ag.knmap.entity.Template;
import us.codecraft.webmagic.Page;

/**
 * PageConsumer
 *
 * @author Gao Shen
 * @version 16/7/8
 */
@FunctionalInterface
public interface PageConsumer {
    void accept(Page page, Template info);
}
