package other;

import lombok.Data;

/**
 * Created by zhuanggangqing on 2018/4/2.
 */
public class Map {
    private static String map = "aaaaaaaaaaaaaaaaaaaa*aaaaaaaaaaaaaaaaaaaa*aaaaaaaaaaaaaaaaaaaa*____________________*bbbbbbbbbbbbbbbbbb__*bbbbbbbbbbbbbbbbbbbb*bbbbbbbbbbbbbbbbbbbb*cccccccccccccccccccc*cccccccccccccccccccc*cccccccccccccccccccc*____________________*dddddddddddddddddd__*dddddddddddddddddddd*dddddddddddddddddddd*dddddddddddddddddddd";

    public static String getMap() {
        return map;
    }

    public static void setMap(String map) {
        Map.map = map;
    }
}
