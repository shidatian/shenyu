package org.apache.shenyu.plugin.simple;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shenyu.common.constant.Constants;
import org.apache.shenyu.common.dto.RuleData;
import org.apache.shenyu.common.dto.SelectorData;
import org.apache.shenyu.common.enums.PluginEnum;
import org.apache.shenyu.common.enums.RpcTypeEnum;
import org.apache.shenyu.common.map.SimpleRouteMap;
import org.apache.shenyu.plugin.api.ShenyuPluginChain;
import org.apache.shenyu.plugin.api.context.ShenyuContext;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.web.server.ServerWebExchange;
import org.apache.shenyu.plugin.base.AbstractShenyuPlugin;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Slf4j
public class SimplePlugin extends AbstractShenyuPlugin {

    @Override
    public int getOrder() {
        return PluginEnum.SIMPLE.getCode();
    }

    @Override
    public String named() {
        return PluginEnum.SIMPLE.getName();
    }

    @Override
    public Boolean skip(ServerWebExchange exchange) {
        final ShenyuContext shenyuContext = exchange.getAttribute(Constants.CONTEXT);
        return !Objects.equals(Objects.requireNonNull(shenyuContext).getRpcType(), RpcTypeEnum.HTTP.getName());
    }

    @Override
    protected Mono<Void> doExecute(ServerWebExchange exchange, ShenyuPluginChain chain, SelectorData selector, RuleData rule) {

        //final String ruleHandle = rule.getHandle();
        ServerHttpRequest request = exchange.getRequest();
        String contentType = request.getHeaders().getFirst(HttpHeaders.CONTENT_TYPE);
        log.info("################################contentType:" + contentType);
        if ("application/json".equals(contentType)) {
            return DataBufferUtils.join(exchange.getRequest().getBody())
                    .flatMap(dataBuffer -> {
                        byte[] bytes = new byte[dataBuffer.readableByteCount()];
                        dataBuffer.read(bytes);
                        try {
                            String bodyString = new String(bytes, "utf-8");
                            log.info("################################bodyString:"+bodyString);
                            exchange.getAttributes().put("POST_BODY", bodyString);
                            JSONObject jsonObject = JSONObject.parseObject(bodyString);
                            if(jsonObject != null){
                                String method = jsonObject.getString("method");
                                log.info("################################method:"+method);
                                String deptId = jsonObject.getString("deptId");
                                log.info("################################deptId:"+deptId);
                                if(deptId != null){ //参数含有部门id，至取路由时要添加部门id标识
                                    method += ":"+deptId;
                                }
                                log.info("################################method:"+method);
                                String realURL = SimpleRouteMap.methodRoute.get(method);//通过请求方法获取对应路由
                                if(realURL!=null){
                                    String id = jsonObject.getString("id");
                                    realURL += "?id="+id;
                                }
                                log.info("################################realURL:"+realURL);
                                exchange.getAttributes().put(Constants.HTTP_URL, realURL);
                            }else{
                                log.info("################################ body is null:");
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        return chain.execute(exchange);
                    });
        } else {
            log.info("!!!!!!!!!!!!!!!!!!!!!!!!!!!! contentType shoud be application/json ,but now it's " + contentType);
        }
        return chain.execute(exchange);
    }

    @Override
    protected Mono<Void> handleSelectorIfNull(final String pluginName, final ServerWebExchange exchange, final ShenyuPluginChain chain) {
        return doExecute(exchange, chain, null, null);
    }

    @Override
    protected Mono<Void> handleRuleIfNull(final String pluginName, final ServerWebExchange exchange, final ShenyuPluginChain chain) {
        return doExecute(exchange, chain, null, null);
    }
}
