package team.ag.knmap.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author weitangzhao
 **/
@Configuration
@MapperScan("team.ag.knmap.mapper")
public class MybatisPlusConfig {

}
