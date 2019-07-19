package team.ag.knmap.service;

import com.baomidou.mybatisplus.extension.service.IService;
import team.ag.knmap.entity.Template;

import java.util.List;

public interface TemplateService extends IService<Template> {
    List<Template> findTemplateByClassId(long id);
}
