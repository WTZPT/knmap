package team.ag.knmap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;
import team.ag.knmap.entity.TemplateClass;


public interface TemplateClassService extends IService<TemplateClass> {
    boolean isEmptyByFieldName(String fieldName);

    //根据领域ID寻找dbName
    String findDbNameById(long id);
}
