package org.apache.shenyu.admin.service.init;

import lombok.extern.slf4j.Slf4j;
import org.apache.shenyu.admin.model.dto.SimpleRouteDTO;
import org.apache.shenyu.admin.model.vo.SimpleRouteVO;
import org.apache.shenyu.admin.service.SimpleRouteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class SimpleRouteLoader {

    public static Map<String,String> methodRoute = new ConcurrentHashMap<>();
    //public static List<SimpleRouteVO> simpleRouteVOS = new ArrayList<>();
    //public static Vector simpleRouteVOSvictor = new Vector(); //同步数组，线程安全
    //public static JsonArray methodRoutes = new JsonArray();

    @Autowired
    public SimpleRouteLoader(SimpleRouteService simpleRouteService){
        List<SimpleRouteVO> simpleRouteVOS = simpleRouteService.selectAll();
        simpleRouteVOS.forEach(simpleRouteVO -> {
            final String method = simpleRouteVO.getMethod();
            String deptId = simpleRouteVO.getDeptId();
            String key = Optional.ofNullable(deptId).map(s -> method +":"+ s).orElse(method);
            String value = simpleRouteVO.getUrl();
            methodRoute.put(key,value);
            log.info("######################## init key("+key+"),value("+value+")");
        });
    }

    public static void addMethodRoute(SimpleRouteDTO simpleRouteDTO){
        String method = simpleRouteDTO.getMethod();
        String deptId = simpleRouteDTO.getDeptId();
        String value = simpleRouteDTO.getUrl();
        String key = Optional.ofNullable(deptId).map(s -> method +":"+ s).orElse(method);
        methodRoute.put(key,value);
        log.info("######################## add key("+key+"),value("+value+")");
    }

    public static void addMethodRoute(String key,String value){
        methodRoute.put(key,value);
        log.info("######################## add key("+key+"),value("+value+")");
    }

}
