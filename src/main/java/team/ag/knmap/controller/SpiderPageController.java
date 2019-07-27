package team.ag.knmap.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import team.ag.knmap.commom.ServerResponse;
import team.ag.knmap.entity.SpiderInfo;
import team.ag.knmap.entity.Template;
import team.ag.knmap.entity.TemplateClass;
import team.ag.knmap.service.SpiderService;
import team.ag.knmap.service.TemplateClassService;
import team.ag.knmap.service.TemplateService;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.ui.Model;
import team.ag.knmap.vo.TemplateAndClassApplingVo;
import team.ag.knmap.vo.TemplateClassVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author weitangzhao
 **/
@Controller
@RequestMapping("/spiderinfo")
public class SpiderPageController {

    private static final Logger log = LogManager.getLogger(SpiderPageController.class);
    @Autowired
    SpiderService spiderService;
    @Autowired
    TemplateService templateService;

    @Autowired
    TemplateClassService templateClassServiceImpl;

    @GetMapping(value = {"/create"})
    public ModelAndView create(Model model) {
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
        return new ModelAndView("spider/create","model",model);
    }
    @GetMapping(value = {"templateCreate"})
    public String templateCreate() {
        return "spider/templateCreate";
    }

    @GetMapping(value={"/templateClass"})
    public ModelAndView templateClass(Model model) {
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
        model.addAttribute("list",templateClassList);
        return new ModelAndView("spider/templateClass","model",model);
    }

    @GetMapping(value = "/collector")
    public ModelAndView collector(Model model){

        List<Template> templateList = templateService.findApplingTemplate();
        List<TemplateAndClassApplingVo> templateAndClassApplingVos = new ArrayList<>();

        if(!CollectionUtils.isEmpty(templateList)) {
            for(Template template : templateList) {
                TemplateAndClassApplingVo templateAndClassApplingVo = new TemplateAndClassApplingVo();
                TemplateClass templateClass = templateClassServiceImpl.getById(template.getClassId());
                templateAndClassApplingVo.setFieldName(templateClass.getFieldName());
                templateAndClassApplingVo.setDiplayName(template.getDisplayName());
                templateAndClassApplingVo.setId(template.getId());
                templateAndClassApplingVo.setStartUrl(template.getStartUrl());
                templateAndClassApplingVo.setAppling(!template.isApplied());
                templateAndClassApplingVos.add(templateAndClassApplingVo);
            }
        }

        model.addAttribute("templateAndClassAppling",templateAndClassApplingVos);
        return new ModelAndView("spider/collector","model",model);
    }

    @PostMapping(value = "/", consumes = "application/json")
    public @ResponseBody void createCreawl(@RequestBody SpiderInfo spiderInfo){
        log.info(spiderInfo.toString());
        spiderService.save(spiderInfo);
    }

    /**
     * 启动采集器接口
     * @param id
     * @return
     */
    @PostMapping("/start")
    @ResponseBody
    public ServerResponse startSpider(@RequestParam Long id) {
       Template template =  templateService.getById(id);
       if(!template.isApplied()) {
           template.setApplied(true);
           templateService.updateById(template);
           TemplateClass templateClass = templateClassServiceImpl.getById(template.getClassId());
           template.setDbName(templateClass.getDbName());
           spiderService.run(template);
           return ServerResponse.createBySuccessMessage(template.getDisplayName()+"进入采集转态！");
       } else {
           return ServerResponse.createByErrorMessage(template.getDisplayName() + "已经进入采集器，请勿重复添加！");
       }
    }

    public String preView(){
        return null;
    }
}
