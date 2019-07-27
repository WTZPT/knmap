package team.ag.knmap.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.juli.logging.Log;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;
import team.ag.knmap.commom.ServerResponse;
import team.ag.knmap.entity.Template;
import team.ag.knmap.entity.TemplateClass;
import team.ag.knmap.service.TemplateClassService;
import team.ag.knmap.service.TemplateService;
import team.ag.knmap.util.GetUUID;
import team.ag.knmap.util.TextParser;
import team.ag.knmap.vo.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author weitangzhao
 **/
@RestController
@RequestMapping("/template")
public class TemplateController {

    private static final Logger log = LogManager.getLogger(TemplateController.class);
    @Autowired
   TemplateClassService templateClassServiceImpl;

    @Autowired
    TemplateService templateService;
    /**
     * 分页查询
     * @param pageNum 当前页面
     * @param pageSize 页面大小
     * @return
     */
    @PostMapping("/getTemplateClassPage")
    public ServerResponse getTemplateClassPage(@RequestParam("pageNum") int pageNum, @RequestParam("pageSize")int pageSize){
                IPage<TemplateClass> templateClassIPage =  templateClassServiceImpl.page(new Page<TemplateClass>(pageNum,pageSize),null);

                return ServerResponse.createBySuccess(templateClassIPage);
    }

    /**
     * 获取所有类模板
     * @return
     */
    @GetMapping("/getTemplateClasses")
    public ServerResponse getTemplateClasses(){
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
        return ServerResponse.createBySuccess(templateClassVoList);
    }


    /**
     * 创建模板类
     * @param fieldName
     * @return
     */
    @PostMapping(value= "/createTemplateClass")
    public ServerResponse createTemplateClass(@RequestParam String fieldName){

        if(!templateClassServiceImpl.isEmptyByFieldName(fieldName)){
            return ServerResponse.createBySuccessMessage("该领域知识图谱已存在！");
        }

        TemplateClass templateClass = new TemplateClass();
        String dbName = GetUUID.getDBName();
        templateClass.setDbName(dbName);
        templateClass.setFieldName(fieldName);

        if(templateClassServiceImpl.save(templateClass)) {
            //todo..在AG数据库创建表
            return ServerResponse.createBySuccessMessage("创建成功！");
        } else {
            return ServerResponse.createByErrorMessage("创建失败！");
        }
    }

    /**
     * 更新模板类
     * @param templateClass
     * @return
     */
    @PostMapping("/updateTemplateClass")
    public ServerResponse updateTemplateClass(@RequestBody TemplateClass templateClass){

        if( templateClassServiceImpl.updateById(templateClass)) {
            return ServerResponse.createBySuccessMessage("更新成功！");
        } else {
            return ServerResponse.createByErrorMessage("更新失败！");
        }

    }

    /**
     * 创建模板
     * @param templateRequestBody)
     * @return
     */
    @PostMapping("/createTemplate")
    public ServerResponse createTemplate(@RequestBody TemplateRequestVo templateRequestBody){
        Template template = new Template();
        template.setClassId(Long.valueOf(templateRequestBody.getClassId()));
        template.setDisplayName(templateRequestBody.getDisplayName());
        template.setThread(Integer.valueOf(templateRequestBody.getThread()));
        template.setRetry(Integer.valueOf(templateRequestBody.getRetry()));
        template.setSleep(Integer.valueOf(templateRequestBody.getSleep()));
        template.setMaxPageGather(Integer.valueOf(templateRequestBody.getMaxPageGather()));
        template.setTimeout(Integer.valueOf(templateRequestBody.getTimeout()));
        template.setStartUrl(templateRequestBody.getStartUrl());
        template.setListPageUrlReg(templateRequestBody.getListPageUrlReg());
        template.setArticleUrlReg(templateRequestBody.getArticleUrlReg());
        template.setArticleUrlXpath(templateRequestBody.getArticleUrlXpath());
        template.setCharset(templateRequestBody.getCharset());
        template.setDynamicSite(Integer.valueOf(templateRequestBody.getDynamicSite()));
        String sXpath="",pXpath="",oXpath="";
        boolean flag = false;
        for(SPO spo  : templateRequestBody.getSpo()) {
            if(spo.getArticleSXpath() != null && spo.getArticlePXpath() != null && spo.getArticleOXpath()!=null) {
                if(flag) {
                    sXpath += "~";
                    pXpath += "~";
                    oXpath += "~";
                } else {
                    flag = true;
                }
                sXpath += spo.getArticleSXpath();
                pXpath += spo.getArticlePXpath();
                oXpath +=  spo.getArticleOXpath();
            }
        }

        template.setArticleSXpath(sXpath);
        template.setArticlePXpath(pXpath);
        template.setArticleOXpath(oXpath);

        if(templateService.save(template)) {
            return ServerResponse.createBySuccessMessage("创建成功！");
        } else {
            return ServerResponse.createByErrorMessage("创建失败！");
        }

    }

    /**
     * 更新模板
     * @param template
     * @return
     */
    @PostMapping("/updateTemplate")
    public ServerResponse updateTemplate(@RequestBody Template template){
        if(templateService.updateById(template)){
            return ServerResponse.createBySuccessMessage("更新成功！");
        } else {
            return ServerResponse.createByErrorMessage("更新失败！");
        }
    }

    /**
     * 获取没有应用过模板
     * @return
     */
    @GetMapping("/getApplingTemplates")
    public ServerResponse getTemplateAndClass() {

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
                templateAndClassApplingVos.add(templateAndClassApplingVo);
            }
        }
        return ServerResponse.createBySuccess(templateAndClassApplingVos);
    }
    /**
     * 获取模板类以及子模板
     * @return
     */
    @GetMapping("/getTemplatesAndClass")
    public ServerResponse getTemplatesAndClass(){
        List<TemplateClass> templateClassList = templateClassServiceImpl.list(new QueryWrapper<TemplateClass>());
        List<TemplateAndClassTreeVo> templateClassAndTemplateVos = new ArrayList<TemplateAndClassTreeVo>();

        if(!CollectionUtils.isEmpty(templateClassList)) {
            for(TemplateClass templateClass : templateClassList) {
                TemplateAndClassTreeVo templateClassAndTemplateVo = new TemplateAndClassTreeVo();
                List<TemplateVo> templateVoList = new ArrayList<>();
                templateClassAndTemplateVo.setFieldName(templateClass.getFieldName());
                List<Template> templateList = templateService.findTemplateByClassId(templateClass.getId());
                if(!CollectionUtils.isEmpty(templateList)) {
                    for(Template template : templateList) {
                        TemplateVo templateVo = new TemplateVo();
                        templateVo.setId(template.getId());
                        templateVo.setDiplayName(template.getDisplayName());
                        templateVo.setClassId(template.getClassId());
                        templateVo.setApplied(template.isApplied());
                        templateVoList.add(templateVo);
                    }
                }
                templateClassAndTemplateVo.setTemplateVoList(templateVoList);
                templateClassAndTemplateVos.add(templateClassAndTemplateVo);
            }
        }
        return ServerResponse.createBySuccess(templateClassAndTemplateVos);
    }
}
