package utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by Денис on 30.03.2017.
 */
public class Constants {

    public static final String SCANNER_THREAD = "scT";
    public static final String SCANNER_SIZE = "scS";
    public static final String SCANNER_SPARSE = "s-sparse";
    public static final String PAGERANK_THREAD = "prT";
    public static final String ANCHOR = "#";

    public static final int DEFAULT_SIZE = 100;

    private final static Map<String, String> values = new ConcurrentHashMap<>();

    public static String get(String key) {
        return values.get(key);
    }

    public static <T> void put(String key, T value) {
        values.put(key, value.toString());
    }
}
