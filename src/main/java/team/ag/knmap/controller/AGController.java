package team.ag.knmap.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import team.ag.knmap.commom.ServerResponse;
import team.ag.knmap.entity.TemplateClass;
import team.ag.knmap.service.AllegrographService;
import team.ag.knmap.service.TemplateClassService;
import team.ag.knmap.vo.TemplateClassVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author weitangzhao
 **/
@Controller
@RequestMapping("/search")
public class AGController {
    @Autowired
    TemplateClassService templateClassService;

    @Autowired
    AllegrographService allegrographService;

    @Autowired
    TemplateClassService templateClassServiceImpl;

    @GetMapping("/page")
    public ModelAndView search(Model model) {

        List<TemplateClass> templateClassList = templateClassServiceImpl.list(new QueryWrapper<TemplateClass>());
        List<TemplateClassVo> templateClassVoList = new ArrayList<TemplateClassVo>();
        if(!CollectionUtils.isEmpty(templateClassList)){
            for(TemplateClass templateClass : templateClassList) {
                TemplateClassVo templateClassVo = new TemplateClassVo();
                templateClassVo.setId(templateClass.getId());
                templateClassVo.setFieldName(templateClass.getFieldName());
                templateClassVoList.add(templateClassVo);
            }
        }
        model.addAttribute("templateClassList",templateClassVoList);
        return new ModelAndView("search/page","model",model);
    }

    @GetMapping("/find")
    public @ResponseBody ServerResponse searchByKey(@RequestParam long fieldId, @RequestParam String key) {
           String dbName =  templateClassService.findDbNameById(fieldId);
            System.out.println(dbName + " " + key);
           return allegrographService.searchGraphInfoByKey(dbName,key);

    }
}
