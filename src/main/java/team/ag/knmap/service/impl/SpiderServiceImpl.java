package team.ag.knmap.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.ag.knmap.entity.SpiderInfo;
import team.ag.knmap.entity.Template;
import team.ag.knmap.gather.article.manger.TaskManager;
import team.ag.knmap.mapper.SpiderInfoMapper;
import team.ag.knmap.service.SpiderService;

/**
 * @Author weitangzhao
 **/
@Service
public class SpiderServiceImpl  extends ServiceImpl<SpiderInfoMapper, SpiderInfo> implements SpiderService {

    @Autowired
    TaskManager taskManager;

    @Override
    public void run(Template spiderInfo){
        taskManager.startSpider(spiderInfo);
    }
}
