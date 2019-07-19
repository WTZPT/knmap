package team.ag.knmap.gather.article.manger;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.assertj.core.util.Preconditions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.ag.knmap.entity.SpiderInfo;
import team.ag.knmap.entity.Template;
import team.ag.knmap.gather.article.spider.ArticleSpider;
import us.codecraft.webmagic.Spider;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author weitangzhao
 **/
@Component
public class TaskManager {
    @Autowired
    ArticleSpider articleSpider;
    private static final Logger LOG = LogManager.getLogger(TaskManager.class);
    private Map<String, Spider> spiderMap = new HashMap<>();

    private TaskManager() {}

    private static class InnerTaskManager {
        private static TaskManager taskManager = new TaskManager();
    }
    public static TaskManager getInstance() {
        return InnerTaskManager.taskManager;
    }

    /**
     * 启动一个爬虫
     * @param info
     * @return
     */
    public String startSpider(Template info){
        // 当断点之后爬虫的uuid会重新随机生成，无法实现去重和断点重爬，所以将uuid与info.name和info.id绑定
        final String uuid = info.getId() + "_" + info.getDisplayName() + "_" + info.getClassId();
        // 判断该任务是否已经存在
        boolean running = spiderMap.containsKey(uuid);

        Preconditions.checkArgument(!running, "已经提交了这个任务,模板编号%s,请勿重复提交",uuid);
        articleSpider.start(uuid,info);
        return uuid;
    }

    /**
     * 停止爬虫
     *
     * @param uuid
     */
    public void stop(String uuid) {
        Preconditions.checkArgument(spiderMap.containsKey(uuid), "找不到UUID为%s的爬虫,请检查参数", uuid);
        spiderMap.get(uuid).close();
        spiderMap.get(uuid).stop();
    }

    /**
     * 删除爬虫
     *
     * @param uuid
     */
    public void delete(String uuid) {
        Preconditions.checkArgument(spiderMap.containsKey(uuid) , "找不到UUID为%s的爬虫,请检查参数", uuid);
        spiderMap.remove(uuid);
    }
}
