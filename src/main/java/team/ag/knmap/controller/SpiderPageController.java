package team.ag.knmap.controller;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import team.ag.knmap.entity.SpiderInfo;
import team.ag.knmap.service.SpiderService;



/**
 * @Author weitangzhao
 **/
@Controller
public class SpiderPageController {
    private static final Logger log = LogManager.getLogger(SpiderPageController.class);
    @Autowired
    SpiderService spiderService;
    @GetMapping(value = {"create"})
    public String create() {

        return "spider/create";

    }

    @PostMapping(value = "/spiderinfo", consumes = "application/json")
    public @ResponseBody void createCreawl(@RequestBody SpiderInfo spiderInfo){

        log.info(spiderInfo.toString());
        spiderService.save(spiderInfo);
        spiderService.run(spiderInfo);

    }
}
