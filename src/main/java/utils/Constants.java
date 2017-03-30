package utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Денис on 30.03.2017.
 */
public class Constants {

    public final static String SCANNER_THREAD = "ct";
    public final static String SCANNER_SIZE = "cs";
    public final static String SCANNER_SPARSE = "c-sparse";
    public final static String PAGERANK_THREAD = "pt";

    private final static Map<String, String> values = new ConcurrentHashMap<>();

    public static String get(String key) {
        return values.get(key);
    }

    public static <T> void put(String key, T value) {
        values.put(key, value.toString());
    }
}
