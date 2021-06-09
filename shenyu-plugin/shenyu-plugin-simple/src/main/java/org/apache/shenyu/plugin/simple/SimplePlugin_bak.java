package org.apache.shenyu.plugin.simple;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.shenyu.common.constant.Constants;
import org.apache.shenyu.common.dto.RuleData;
import org.apache.shenyu.common.dto.SelectorData;
import org.apache.shenyu.common.enums.PluginEnum;
import org.apache.shenyu.common.enums.RpcTypeEnum;
import org.apache.shenyu.plugin.api.ShenyuPluginChain;
import org.apache.shenyu.plugin.api.context.ShenyuContext;
import org.apache.shenyu.plugin.simple.mapping.RouteMap;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpRequestDecorator;
import org.springframework.web.server.ServerWebExchange;
import org.apache.shenyu.plugin.base.AbstractShenyuPlugin;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.UnsupportedEncodingException;
import java.util.Objects;

@Slf4j
public class SimplePlugin extends AbstractShenyuPlugin {

    @Override
    public int getOrder() {
        return PluginEnum.AROUND2LOG.getCode();
    }

    @Override
    public String named() {
        return PluginEnum.AROUND2LOG.getName();
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
            //Flux<DataBuffer> body = request.getBody();
            //log.info("################################body:" + body);
            //CompletableFuture<String> future = new CompletableFuture<>();//通过调用get方法来使主线程 等待 异步线程执行完成
            //AtomicReference<String> bodyRef = new AtomicReference<>();
            //Map<String,String> dataMap = new HashMap<String,String>();
            /*body.subscribe((DataBuffer buffer) -> {  //订阅执行获取的body
                byte[] bytes = new byte[buffer.readableByteCount()];
                buffer.read(bytes);
                try {
                    String bodyString = new String(bytes, "utf-8");
                    //bodyRef.set(bodyString);
                    log.info("################################bodyString:"+bodyString);
                    JSONObject jsonObject = JSONObject.parseObject(bodyString);
                    if(jsonObject != null){
                        *//*dataMap.put("id", jsonObject.getString("id"));
                        dataMap.put("method", jsonObject.getString("method"));
                        dataMap.put("content", jsonObject.getString("content"));*//*
                        String id = jsonObject.getString("id");
                        exchange.getAttributes().put(Constants.APP_PARAM, id);
                        String method = jsonObject.getString("method");
                        log.info("################################method:"+method);
                        String realURL = RouteMap.methodRoute.get(method);
                        log.info("################################realURL:"+realURL);
                        exchange.getAttributes().put(Constants.HTTP_URL, realURL);
                    }else{
                        log.info("################################ body is null:");
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }finally {
                    //future.complete("################################ body.subscribe ok");
                }
            });*/
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
                                String realURL = RouteMap.methodRoute.get(method);//通过请求方法获取对应路由
                                String id = jsonObject.getString("id");
                                realURL += "?id="+id;
                                log.info("################################realURL:"+realURL);
                                exchange.getAttributes().put(Constants.HTTP_URL, realURL);
                            }else{
                                log.info("################################ body is null:");
                            }
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        DataBufferUtils.release(dataBuffer);
                        Flux<DataBuffer> cachedFlux = Flux.defer(() -> {
                            DataBuffer buffer = exchange.getResponse().bufferFactory()
                                    .wrap(bytes);
                            return Mono.just(buffer);
                        });

                        ServerHttpRequest mutatedRequest = new ServerHttpRequestDecorator(
                                exchange.getRequest()) {
                            @Override
                            public Flux<DataBuffer> getBody() {
                                return cachedFlux;
                            }
                        };
                        return chain.execute(exchange.mutate().request(mutatedRequest)
                                .build());
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
