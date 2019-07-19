package team.ag.knmap.util;

import java.util.Random;
import java.util.UUID;

/**
 * @Author weitangzhao
 **/
public class GetUUID {
    public static String getDBName(){
        UUID uuid = UUID.randomUUID();
        String[] uuids = uuid.toString().split("-");
        int ida = new Random().nextInt(5);
        int idb = new Random().nextInt(5);
        String dbName = uuids[ida] + uuids[idb];
        return dbName;
    }

}
