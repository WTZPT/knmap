package team.ag.knmap.vo;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * @Author weitangzhao
 **/
@Data
public class TemplateVo {
    private long id;
    private long classId;
    private String diplayName;
    private boolean applied;
}
