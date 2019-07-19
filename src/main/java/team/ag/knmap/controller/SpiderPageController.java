package team.ag.knmap.controller;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.ag.knmap.commom.ServerResponse;
import team.ag.knmap.entity.SpiderInfo;
import team.ag.knmap.entity.Template;
import team.ag.knmap.service.SpiderService;
import team.ag.knmap.service.TemplateService;


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
    @GetMapping(value = {"create"})
    public String create() {
        return "spider/create";

    }
    @GetMapping(value = {"templateCreate"})
    public String templateCreate() {
        return "spider/templateCreate";
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
    public ServerResponse startSpider(@RequestBody Long id) {
       Template template =  templateService.getById(id);
       if(!template.isApplied()) {
           Template updateTemplate = new Template();
           updateTemplate.setApplied(true);
           templateService.updateById(updateTemplate);
           //spiderService.run(template);
           return ServerResponse.createBySuccessMessage(template.getDisplayName()+"进入采集转态！");
       } else {
           return ServerResponse.createByErrorMessage(template.getDisplayName() + "已经进入采集器，请勿重复添加！");
       }
    }
}
