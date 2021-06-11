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

package org.apache.shenyu.admin.service;

import org.apache.shenyu.admin.model.dto.SimpleRouteDTO;
import org.apache.shenyu.admin.model.page.CommonPager;
import org.apache.shenyu.admin.model.query.SimpleRouteQuery;
import org.apache.shenyu.admin.model.vo.SimpleRouteVO;

import java.util.List;

/**
 * this is role service.
 */
public interface SimpleRouteService {

    /**
     * create or update rule.
     *
     * @param simpleRouteDTO {@linkplain SimpleRouteDTO}
     * @return rows int
     */
    int createOrUpdate(SimpleRouteDTO simpleRouteDTO);

    /**
     * delete roles.
     *
     * @param ids primary key
     * @return rows int
     */
    int delete(List<String> ids);

    /**
     * find role by id.
     *
     * @param id primary key
     * @return {@linkplain SimpleRouteDTO}
     */
    SimpleRouteVO findById(String id);

    /**
     * find role by roleName.
     *
     * @param method role name
     * @return {@linkplain SimpleRouteDTO}
     */
    SimpleRouteVO findByQuery(String method);

    /**
     * find page of role by query.
     *
     * @param simpleRouteQuery {@linkplain SimpleRouteQuery}
     * @return {@linkplain CommonPager}
     */
    CommonPager<SimpleRouteVO> listByPage(SimpleRouteQuery simpleRouteQuery);

    /**
     * select all roles not super.
     *
     * @return {@linkplain List}
     */
    List<SimpleRouteVO> selectAll();
}
