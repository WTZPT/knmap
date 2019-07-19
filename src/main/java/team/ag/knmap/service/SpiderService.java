package team.ag.knmap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.ag.knmap.entity.SpiderInfo;
import team.ag.knmap.entity.Template;

public interface SpiderService extends IService<SpiderInfo> {

     void run(Template spiderInfo);
}
