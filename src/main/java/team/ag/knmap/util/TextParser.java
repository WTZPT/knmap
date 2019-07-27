package team.ag.knmap.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author weitangzhao
 **/
public class TextParser {
    public static  String[] splitMatch(String match){
        String[] list = match.split(";");
        return list;
    }

    public static String[] splitMatchWithWaterLine(String match) {
        String[] list = match.split("~");
        return list;
    }
    public static String getNewContent(String content) {
        String regEx_html="<[^>]+>"; //定义HTML标签的正则表达式
        String regEx_txt = "[^a-zA-Z_\u4e00-\u9fa5]"; //定义文本中的特殊符号的正则表达式

        //清除HTML标签
        Pattern p_html=Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
        Matcher m_html=p_html.matcher(content);
        content=m_html.replaceAll("");

        //清除文本中特殊符号
        Pattern p_txt=Pattern.compile(regEx_txt,Pattern.CASE_INSENSITIVE);
        Matcher m_txt=p_txt.matcher(content);
        content=m_txt.replaceAll("");

        return content.trim();
    }
}
