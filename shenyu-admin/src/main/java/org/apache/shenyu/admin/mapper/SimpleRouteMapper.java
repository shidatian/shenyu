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

package org.apache.shenyu.admin.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.shenyu.admin.model.entity.SimpleRouteDO;
import org.apache.shenyu.admin.model.query.SimpleRouteQuery;

import java.util.List;

/**
 * The Role Mapper.
 */
@Mapper
public interface SimpleRouteMapper {

    /**
     * select role by id.
     *
     * @param id primary key.
     * @return {@linkplain SimpleRouteDO}
     */
    SimpleRouteDO selectById(String id);

    /**
     * select role by query.
     *
     * @param simpleRouteQuery {@linkplain SimpleRouteQuery}
     * @return  {@linkplain List}
     */
    List<SimpleRouteDO> selectByQuery(SimpleRouteQuery simpleRouteQuery);

    /**
     * Find by Role Name list.
     *
     * @param method the role name
     * @return The role
     */
    SimpleRouteDO findByMethod(String method);

    /**
     * count role by query.
     *
     * @param simpleRouteQuery {@linkplain SimpleRouteQuery}
     * @return {@linkplain Integer}
     */
    Integer countByQuery(SimpleRouteQuery simpleRouteQuery);

    /**
     * insert role.
     *
     * @param simpleRouteDO {@linkplain SimpleRouteDO}
     * @return rows int
     */
    int insert(SimpleRouteDO simpleRouteDO);

    /**
     * insert selective role.
     *
     * @param simpleRouteDO {@linkplain SimpleRouteDO}
     * @return rows int
     */
    int insertSelective(SimpleRouteDO simpleRouteDO);

    /**
     * update role.
     *
     * @param simpleRouteDO {@linkplain SimpleRouteDO}
     * @return rows int
     */
    int update(SimpleRouteDO simpleRouteDO);

    /**
     * update selective role.
     *
     * @param simpleRouteDO {@linkplain SimpleRouteDO}
     * @return rows int
     */
    int updateSelective(SimpleRouteDO simpleRouteDO);

    /**
     * delete role.
     *
     * @param ids primary keys
     * @return rows int
     */
    int delete(List<String> ids);

    /**
     * list All.
     *
     * @return {@linkplain List}
     */
    List<SimpleRouteDO> selectAll();
}
