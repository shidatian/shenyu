import org.apache.shenyu.plugin.api.ShenyuPlugin;
import org.apache.shenyu.plugin.around.log.AroundLogPlugin;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(AroundLogPlugin.class)
public class AroundLogPluginConfiguration {

    /**
     * Waf plugin soul plugin.
     *
     * @return the soul plugin
     */
    @Bean
    public ShenyuPlugin aroundLogPlugin() {
        return new AroundLogPlugin();
    }
}