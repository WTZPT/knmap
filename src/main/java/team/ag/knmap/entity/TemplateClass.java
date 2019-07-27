package team.ag.knmap.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Author weitangzhao
 **/
@Data
@TableName("template_class")
public class TemplateClass {
    @Id
    private long id;
    private String fieldName;
    private String dbName;
}
