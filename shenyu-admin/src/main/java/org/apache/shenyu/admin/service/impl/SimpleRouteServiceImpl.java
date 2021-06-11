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

package org.apache.shenyu.admin.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shenyu.admin.mapper.SimpleRouteMapper;
import org.apache.shenyu.admin.model.dto.SimpleRouteDTO;
import org.apache.shenyu.admin.model.entity.SimpleRouteDO;
import org.apache.shenyu.admin.model.page.CommonPager;
import org.apache.shenyu.admin.model.page.PageResultUtils;
import org.apache.shenyu.admin.model.query.SimpleRouteQuery;
import org.apache.shenyu.admin.model.vo.SimpleRouteVO;
import org.apache.shenyu.admin.service.SimpleRouteService;
import org.apache.shenyu.admin.service.init.SimpleRouteLoader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * SimpleRouteServiceImpl.
 */
@Service("simpleRouteService")
@Slf4j
public class SimpleRouteServiceImpl implements SimpleRouteService {

    private final SimpleRouteMapper simpleRouteMapper;

    @Autowired(required = false)
    public SimpleRouteServiceImpl(final SimpleRouteMapper simpleRouteMapper) {
        this.simpleRouteMapper = simpleRouteMapper;
    }

    /**
     * create or update role info.
     *
     * @param simpleRouteDTO {@linkplain SimpleRouteDTO}
     * @return rows
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int createOrUpdate(final SimpleRouteDTO simpleRouteDTO) {
        SimpleRouteDO simpleRouteDO = SimpleRouteDO.buildSimpleRouteDO(simpleRouteDTO);
        if (StringUtils.isEmpty(simpleRouteDTO.getId())) {
            return simpleRouteMapper.insertSelective(simpleRouteDO);
        } else {
            return simpleRouteMapper.updateSelective(simpleRouteDO);
        }
    }

    /**
     * delete role info.
     *
     * @param ids primary key
     * @return rows
     */
    @Override
    public int delete(final List<String> ids) {
        return simpleRouteMapper.delete(ids);
    }

    /**
     * find role info by id.
     *
     * @param id primary key
     * @return {@linkplain SimpleRouteVO}
     */
    @Override
    public SimpleRouteVO findById(final String id) {
        SimpleRouteDO simpleRouteDO = simpleRouteMapper.selectById(id);

        /*final String tempKey = simpleRouteDO.getMethod();
        String key = Optional.ofNullable(simpleRouteDO.getDeptId()).map(item -> tempKey+":"+item).orElse(tempKey);
        log.info("#######################key is "+key);
        String value = SimpleRouteLoader.methodRoute.get(key);
        if(value == null){
            SimpleRouteLoader.methodRoute.put(key,simpleRouteDO.getUrl());
            log.info("####################### methodRoute add "+key);
        }else{
            log.info("####################### methodRoute has "+value);
        }*/

        return SimpleRouteVO.buildSimpleRouteVO(simpleRouteDO);
    }

    /**
     * find role by query.
     *
     * @param method role name
     * @return {@linkplain SimpleRouteVO}
     */
    @Override
    public SimpleRouteVO findByQuery(final String method) {
        return SimpleRouteVO.buildSimpleRouteVO(simpleRouteMapper.findByMethod(method));
    }

    /**
     * find page of role by query.
     *
     * @param simpleRouteQuery {@linkplain SimpleRouteQuery}
     * @return {@linkplain CommonPager}
     */
    @Override
    public CommonPager<SimpleRouteVO> listByPage(final SimpleRouteQuery simpleRouteQuery) {
        return PageResultUtils.result(simpleRouteQuery.getPageParameter(),
            () -> simpleRouteMapper.countByQuery(simpleRouteQuery),
            () -> simpleRouteMapper.selectByQuery(simpleRouteQuery).stream().map(SimpleRouteVO::buildSimpleRouteVO).collect(Collectors.toList()));
    }

    /**
     * select all roles.
     *
     * @return {@linkplain List}
     */
    @Override
    public List<SimpleRouteVO> selectAll() {
        return simpleRouteMapper.selectAll().stream().map(SimpleRouteVO::buildSimpleRouteVO).collect(Collectors.toList());
    }

}
