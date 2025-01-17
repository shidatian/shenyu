package org.apache.shenyu.springboot.plugin.simple;

import org.apache.shenyu.plugin.api.ShenyuPlugin;
import org.apache.shenyu.plugin.simple.SimplePlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(SimplePlugin.class)
public class SimplePluginConfiguration {

    /**
     * Around2Log plugin soul plugin.
     *
     * @return the soul plugin
     */
    @Bean
    public ShenyuPlugin simplePlugin() {
        return new SimplePlugin();
    }
}
