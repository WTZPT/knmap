package team.ag.knmap.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import team.ag.knmap.entity.Template;
import team.ag.knmap.mapper.TemplateMapper;
import team.ag.knmap.service.TemplateService;

import java.util.List;

/**
 * @Author weitangzhao
 **/
@Service
public class TemplateServiceImpl  extends ServiceImpl<TemplateMapper, Template> implements TemplateService {



    //根据classId查询子模板
    @Override
    public List<Template> findTemplateByClassId(long id) {
        QueryWrapper<Template> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.lambda()
                .eq(Template::getClassId ,id);

        return list(templateQueryWrapper);
    }

    //获取没用使用过的模板
    @Override
    public List<Template> findApplingTemplate() {
        QueryWrapper<Template> templateQueryWrapper = new QueryWrapper<>();
        templateQueryWrapper.lambda()
                .eq(Template::isApplied,false);
        return list(templateQueryWrapper);
    }

}
