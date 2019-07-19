package team.ag.knmap.vo;

import lombok.Data;

import java.util.List;

/**
 * @Author weitangzhao
 **/
@Data
public class TemplateAndClassTreeVo {
    private String fieldName;
    private List<TemplateVo> templateVoList;
}
