package utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Денис on 30.03.2017.
 */
public class Constants {

    public final static String CRAWLER_THREADS = "ct";
    public final static String CRAWLER_MAX_SIZE = "cs";
    public final static String CRAWLER_SPARSE = "c-sparse";
    public final static String PRC_THREADS = "pt";

    private final static Map<String, String> values = new ConcurrentHashMap<>();

    public static String get(String key) {
        return values.get(key);
    }

    public static <T> void put(String key, T value) {
        values.put(key, value.toString());
    }
}
