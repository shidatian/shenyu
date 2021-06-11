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

package org.apache.shenyu.admin.controller;


import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import org.apache.shenyu.admin.listener.websocket.WebsocketCollector;
import org.apache.shenyu.admin.model.dto.SimpleRouteDTO;
import org.apache.shenyu.admin.model.page.CommonPager;
import org.apache.shenyu.admin.model.page.PageParameter;
import org.apache.shenyu.admin.model.query.SimpleRouteQuery;
import org.apache.shenyu.admin.model.result.ShenyuAdminResult;
import org.apache.shenyu.admin.model.vo.SimpleRouteVO;
import org.apache.shenyu.admin.service.SimpleRouteService;
import org.apache.shenyu.admin.service.init.SimpleRouteLoader;
import org.apache.shenyu.admin.utils.ShenyuResultMessage;
import org.apache.shenyu.common.dto.WebsocketData;
import org.apache.shenyu.common.enums.ConfigGroupEnum;
import org.apache.shenyu.common.enums.DataEventTypeEnum;
import org.apache.shenyu.common.utils.GsonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.websocket.Session;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * this is simpleroute controller.
 */
@Slf4j
@RestController
@RequestMapping("/simpleroute")
public class SimpleRouteController {

    private final SimpleRouteService simpleRouteService;

    @Autowired(required = false)
    public SimpleRouteController(final SimpleRouteService simpleRouteService) {
        this.simpleRouteService = simpleRouteService;
    }

    /**
     * query simpleRoutes.
     *
     * @param method      method.
     * @param currentPage current page.
     * @param pageSize    page size.
     * @return {@linkplain ShenyuAdminResult}
     */
    @GetMapping("")
    public ShenyuAdminResult SimpleRoutes(final String method,final String deptId,final String url, final Integer currentPage, final Integer pageSize) {
        CommonPager<SimpleRouteVO> commonPager = simpleRouteService.listByPage(new SimpleRouteQuery( method,deptId,url, new PageParameter(currentPage, pageSize)));
        return ShenyuAdminResult.success(ShenyuResultMessage.QUERY_SUCCESS, commonPager);

    }

    /**
     * detail simpleRoute.
     *
     * @param id simpleRoute id.
     * @return {@linkplain ShenyuAdminResult}
     */
    @GetMapping("/{id}")
    public ShenyuAdminResult detailSimpleRoute(@PathVariable("id") final String id) {
        SimpleRouteVO simpleRouteVO = simpleRouteService.findById(id);
        return ShenyuAdminResult.success(ShenyuResultMessage.DETAIL_SUCCESS, simpleRouteVO);
    }

    /**
     * create simpleRoute.
     *
     * @param simpleRouteDTO rule.
     * @return {@linkplain ShenyuAdminResult}
     */
    @PostMapping("/save")
    public ShenyuAdminResult createSimpleRoute(@RequestBody final SimpleRouteDTO simpleRouteDTO) {
        Integer createCount = simpleRouteService.createOrUpdate(simpleRouteDTO);//路由信息保存至数据库

        //admin控制台的SimpleRouteLoader.methodRoute添加路由信息
        String method = simpleRouteDTO.getMethod();
        String deptId = simpleRouteDTO.getDeptId();
        String value = simpleRouteDTO.getUrl();
        String key = Optional.ofNullable(deptId).map(s -> method +":"+ s).orElse(method);
        SimpleRouteLoader.addMethodRoute(key,value);

        //bootstrap网关的SimpleRouteMap.methodRoute添加路由信息
        List list = new ArrayList<>();
        JSONObject json = new JSONObject();
        json.put(key,value);
        list.add(json);
        WebsocketData websocketData = new WebsocketData();
        websocketData.setGroupType(ConfigGroupEnum.SIMPLE_ROUTE.name());
        websocketData.setEventType("ADD");
        websocketData.setData(list);
        WebsocketCollector.send(GsonUtils.getInstance().toJson(websocketData), DataEventTypeEnum.CREATE);

        return ShenyuAdminResult.success(ShenyuResultMessage.CREATE_SUCCESS, createCount);
    }

    /**
     * update simpleRoute.
     *
     * @param simpleRouteDTO rule.
     * @return {@linkplain ShenyuAdminResult}
     */
    @PostMapping("/update")
    public ShenyuAdminResult updateSimpleRoute(@RequestBody final SimpleRouteDTO simpleRouteDTO) {
        Integer updateCount = simpleRouteService.createOrUpdate(simpleRouteDTO);
        return ShenyuAdminResult.success(ShenyuResultMessage.UPDATE_SUCCESS, updateCount);
    }

    /**
     * update simpleRoute.
     *
     * @param id      primary key.
     * @param simpleRouteDTO simpleRoute.
     * @return {@linkplain ShenyuAdminResult}
     */
    @PutMapping("/{id}")
    public ShenyuAdminResult updateSimpleRoute(@PathVariable("id") final String id, @RequestBody final SimpleRouteDTO simpleRouteDTO) {
        Objects.requireNonNull(simpleRouteDTO);
        simpleRouteDTO.setId(id);
        Integer updateCount = simpleRouteService.createOrUpdate(simpleRouteDTO);
        return ShenyuAdminResult.success(ShenyuResultMessage.UPDATE_SUCCESS, updateCount);
    }

    /**
     * delete simpleRoutes.
     *
     * @param ids primary key.
     * @return {@linkplain ShenyuAdminResult}
     */
    @DeleteMapping("/batch")
    public ShenyuAdminResult deleteSimpleRoutes(@RequestBody final List<String> ids) {
        Integer deleteCount = simpleRouteService.delete(ids);
        return ShenyuAdminResult.success(ShenyuResultMessage.DELETE_SUCCESS, deleteCount);
    }
}
