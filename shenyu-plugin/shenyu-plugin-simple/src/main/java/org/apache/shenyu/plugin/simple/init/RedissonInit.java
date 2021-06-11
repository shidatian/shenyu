package org.apache.shenyu.plugin.simple.init;

import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;

@Component
@Slf4j
public class RedissonInit {

    @Bean
    public RedissonClient getRedissonClient() throws IOException {
        URL url = this.getClass().getClassLoader().getResource("redisson.yml");
        log.info("######### RedissonClient bean init ##########" + url.getFile());
        Config config = Config.fromYAML(new File(url.getFile()));
        return Redisson.create(config);
    }

}
