package team.ag.knmap.entity;

import org.springframework.data.annotation.Id;

import java.io.Serializable;

/**
 * @Author weitangzhao
 **/
public class Catalog implements Serializable {
    @Id
    private long id;

    private String dbName;
}
