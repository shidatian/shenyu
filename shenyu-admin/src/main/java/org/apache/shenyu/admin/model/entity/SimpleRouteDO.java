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

package org.apache.shenyu.admin.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.apache.shenyu.admin.model.dto.SimpleRouteDTO;
import org.apache.shenyu.common.utils.UUIDUtils;
import reactor.util.StringUtils;

import java.sql.Timestamp;
import java.util.Optional;

/**
 * The Role Data Entity.
 */
@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public final class SimpleRouteDO extends BaseDO {

    private static final long serialVersionUID = -7319631396664845158L;

    /**
     * method.
     */
    private String method;

    /**
     * deptId.
     */
    private String deptId;

    /**
     * url.
     */
    private String url;

    /**
     * build RoleDO.
     *
     * @param simpleRouteDTO {@linkplain SimpleRouteDTO}
     * @return {@linkplain SimpleRouteDO}
     */
    public static SimpleRouteDO buildSimpleRouteDO(final SimpleRouteDTO simpleRouteDTO) {
        return Optional.ofNullable(simpleRouteDTO).map(item -> {
            Timestamp currentTime = new Timestamp(System.currentTimeMillis());
            SimpleRouteDO simpleRouteDO = SimpleRouteDO.builder()
                    .method(item.getMethod())
                    .deptId(item.getDeptId())
                    .url(item.getUrl())
                    .dateUpdated(currentTime)
                    .build();
            if (StringUtils.isEmpty(item.getId())) {
                simpleRouteDO.setId(UUIDUtils.getInstance().generateShortUuid());
                simpleRouteDO.setDateCreated(currentTime);
            } else {
                simpleRouteDO.setId(item.getId());
            }
            return simpleRouteDO;
        }).orElse(null);
    }
}
