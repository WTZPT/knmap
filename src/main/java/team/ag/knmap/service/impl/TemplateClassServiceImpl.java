package team.ag.knmap.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import team.ag.knmap.entity.Template;
import team.ag.knmap.entity.TemplateClass;
import team.ag.knmap.mapper.TemplateClassMapper;
import team.ag.knmap.service.TemplateClassService;

/**
 * @Author weitangzhao
 **/
@Service
public class TemplateClassServiceImpl extends ServiceImpl<TemplateClassMapper, TemplateClass> implements TemplateClassService {

    @Override
    public boolean isEmptyByFieldName(String fieldName) {
        QueryWrapper<TemplateClass> templateClassQueryWrapper = new QueryWrapper<>();
        templateClassQueryWrapper.lambda()
                .eq(TemplateClass::getFieldName, fieldName);

        TemplateClass template = getOne(templateClassQueryWrapper);
        if (template != null) {
            return false;
        } else {
            return true;
        }

    }
}
