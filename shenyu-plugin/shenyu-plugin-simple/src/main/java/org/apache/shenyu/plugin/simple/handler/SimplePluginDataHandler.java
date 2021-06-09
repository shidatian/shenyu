

package org.apache.shenyu.plugin.simple.handler;

import org.apache.shenyu.common.dto.RuleData;
import org.apache.shenyu.common.dto.SelectorData;
import org.apache.shenyu.common.dto.convert.rule.impl.DivideRuleHandle;
import org.apache.shenyu.common.enums.PluginEnum;
import org.apache.shenyu.common.utils.GsonUtils;
import org.apache.shenyu.plugin.base.handler.PluginDataHandler;
import org.apache.shenyu.plugin.base.utils.CacheKeyUtils;
import org.apache.shenyu.plugin.divide.cache.UpstreamCacheManager;

import java.util.Optional;

/**
 * The type Divide plugin data handler.
 */
public class SimplePluginDataHandler implements PluginDataHandler {

    @Override
    public void handlerSelector(final SelectorData selectorData) {
        UpstreamCacheManager.getInstance().submit(selectorData);
    }

    @Override
    public void removeSelector(final SelectorData selectorData) {
        UpstreamCacheManager.getInstance().removeByKey(selectorData.getId());
    }

    @Override
    public void handlerRule(final RuleData ruleData) {
        Optional.ofNullable(ruleData.getHandle()).ifPresent(s -> {
            final DivideRuleHandle divideRuleHandle = GsonUtils.getInstance().fromJson(s, DivideRuleHandle.class);
            UpstreamCacheManager.getInstance().cachedHandle(CacheKeyUtils.INST.getKey(ruleData), divideRuleHandle);
        });
    }

    @Override
    public void removeRule(final RuleData ruleData) {
        Optional.ofNullable(ruleData.getHandle()).ifPresent(s -> UpstreamCacheManager.getInstance().removeHandle(CacheKeyUtils.INST.getKey(ruleData)));
    }

    @Override
    public String pluginNamed() {
        return PluginEnum.SIMPLE.getName();
    }
}
