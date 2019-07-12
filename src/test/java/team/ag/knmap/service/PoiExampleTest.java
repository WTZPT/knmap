package team.ag.knmap.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import team.ag.knmap.KnmapApplicationTests;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PoiExampleTest extends KnmapApplicationTests {

    @Autowired
    private PoiExample poiExample;
    @Test
    public void exchange() {
        List<String> result = PoiExample.converPPTtoImage("D:\\课程资料\\Java开发SSH\\课件资源\\第二章 Struts的基本用法_New.ppt", "D:\\demo\\e2\\", "jpg", 8);
        for(String s:result){
            System.out.println(s);
        }
        /**
        try {
            poiExample.exchange("C:\\Users\\12771\\Desktop\\消息中间件.pptx");
        } catch (IOException e) {
            e.printStackTrace();
        }**/
    }

    @Test
    public void exchange1() {
        Date date = new Date();
        List<String> res = new ArrayList<>();
        long t1 = date.getTime();
        PoiExample.doPPT2007toImage(new File("D:\\课程资料\\Java开发SSH\\课件资源\\第二章 Struts的基本用法_New.ppt"),new File("D:\\demo\\e2\\"),res);
        long t2 = date.getTime();
        System.out.println(t2-t1);
    }
}