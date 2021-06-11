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

package org.apache.shenyu.plugin.sync.data.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.apache.shenyu.common.map.SimpleRouteMap;
import org.apache.shenyu.common.utils.GsonUtils;

import java.util.List;
import java.util.Map;


/**
 * The type SimpleRouteDataHandler.
 */
@Slf4j
public class SimpleRouteDataHandler implements DataHandler{

    protected void doRefresh(String json) {
        List<Map<String, Object>> arrayList = GsonUtils.getInstance().toListMap(json);
        arrayList.forEach(map -> map.forEach((key, value)-> SimpleRouteMap.methodRoute.put(key,value.toString())));
        SimpleRouteMap.methodRoute.forEach((key,value)-> log.info(key+"--------"+value));
    }

    protected void doUpdate() {

    }

    protected void doDelete() {

    }

    @Override
    public void handle(final String json, final String eventType) {
        log.info("############### eventType "+eventType);
        log.info("############### json "+json);
        if("INIT".equals(eventType)){
            doRefresh(json);
        }else if("ADD".equals(eventType)){
            doRefresh(json);
        }else if("UPDATE".equals(eventType)){
            doRefresh(json);
        }
    }

}
