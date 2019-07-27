package team.ag.knmap.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * @Author weitangzhao
 **/
@Data
@TableName("triad")
public class Triad {
    @Id
    private Long id;
    private Long classId;
    private Long templateId;
    private String spoS;
    private String spoP;
    private String spoO;
    private boolean inserted;

    public Triad() {
    }

    public Triad(Long classId, Long templateId, String spoS, String spoP, String spoO, boolean inserted) {
        this.classId = classId;
        this.templateId = templateId;
        this.spoS = spoS;
        this.spoP = spoP;
        this.spoO = spoO;
        this.inserted = inserted;
    }
}
