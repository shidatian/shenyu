package org.apache.shenyu.common.map;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleRouteMap {
    public static Map<String,String> methodRoute = new ConcurrentHashMap<>();
}
