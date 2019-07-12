package team.ag.knmap.util;

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
}
