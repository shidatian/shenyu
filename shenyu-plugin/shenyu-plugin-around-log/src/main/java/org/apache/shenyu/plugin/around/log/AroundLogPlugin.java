package org.apache.shenyu.plugin.around.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.shenyu.common.enums.PluginEnum;
import org.apache.shenyu.plugin.api.ShenyuPlugin;
import org.apache.shenyu.plugin.api.ShenyuPluginChain;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
public class AroundLogPlugin implements ShenyuPlugin {
    @Override
    public Mono<Void> execute(ServerWebExchange exchange, ShenyuPluginChain chain) {
        exchange.getAttributes().put("START_TIME", System.currentTimeMillis());

        log.info("--------------------------AroundPlugin start executing...---------------------");

        return chain.execute(exchange)
                .then(Mono.defer(() -> {
                    Long startTime = exchange.getAttribute("START_TIME");
                    log.info("------------------org.apache.shenyu.plugin.around.log.AroundLogPlugin end. Total execution time: {} ms", System.currentTimeMillis() - startTime);
                    return Mono.empty();
                }));
    }

    @Override
    public int getOrder() {
        return PluginEnum.AROUNDLOG.getCode();
    }

    @Override
    public String named() {
        return PluginEnum.AROUNDLOG.getName();
    }

    @Override
    public Boolean skip(ServerWebExchange exchange) {
        return ShenyuPlugin.super.skip(exchange);
    }
}
