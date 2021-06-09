package org.apache.shenyu.plugin.simple.mapping;

import java.util.HashMap;
import java.util.Map;

public class RouteMap {
    public static Map<String,String> methodRoute = new HashMap<String,String>();
    static {
        methodRoute.put("findUserById","http://127.0.0.1/findUserById");
        methodRoute.put("getUsers","http://127.0.0.2/getUsers");
        methodRoute.put("getProducts","http://127.0.0.3/getProducts");
        methodRoute.put("getProviders","http://127.0.0.4/getProviders");
        methodRoute.put("findById","http://127.0.0.1:8189/order/findById");

    }
}
