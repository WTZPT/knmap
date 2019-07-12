package team.ag.knmap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.ag.knmap.entity.SpiderInfo;

public interface SpiderService extends IService<SpiderInfo> {

     void run(SpiderInfo spiderInfo);
}
