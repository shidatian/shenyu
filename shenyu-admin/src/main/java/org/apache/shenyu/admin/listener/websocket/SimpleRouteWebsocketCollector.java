/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.shenyu.admin.listener.websocket;


import net.minidev.json.JSONObject;
import org.apache.shenyu.admin.service.SyncDataService;
import org.apache.shenyu.admin.service.init.SimpleRouteLoader;
import org.apache.shenyu.admin.spring.SpringBeanUtils;
import org.apache.shenyu.admin.utils.ThreadLocalUtil;
import org.apache.shenyu.common.dto.WebsocketData;
import org.apache.shenyu.common.enums.ConfigGroupEnum;
import org.apache.shenyu.common.enums.DataEventTypeEnum;
import org.apache.shenyu.common.utils.GsonUtils;

import javax.websocket.OnMessage;
import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;

/**
 * The type Websocket data changed listener.
 *
 * @since 2.0.0
 */

public class SimpleRouteWebsocketCollector extends WebsocketCollector{

    /**
     * On message.
     *
     * @param message the message
     * @param session the session
     */
    @Override
    @OnMessage
    public void onMessage(final String message, final Session session) {
        if (message.equals(DataEventTypeEnum.MYSELF.name())) {
            try {
                ThreadLocalUtil.put(SESSION_KEY, session);
                SpringBeanUtils.getInstance().getBean(SyncDataService.class).syncAll(DataEventTypeEnum.MYSELF);
                SimpleRouteLoader.methodRoute.forEach((key, value) -> {
                    List list = new ArrayList<>();
                    JSONObject json = new JSONObject();
                    json.put(key,value);
                    list.add(json);
                    WebsocketData websocketData = new WebsocketData();
                    websocketData.setGroupType(ConfigGroupEnum.SIMPLE_ROUTE.name());
                    websocketData.setEventType("INIT");
                    websocketData.setData(list);
                    SimpleRouteWebsocketCollector.sendMessageBySession(session, GsonUtils.getInstance().toJson(websocketData));
                });
            } finally {
                ThreadLocalUtil.clear();
            }
        }
    }
}
