/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.flowcentraltech.flowcentral.system.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseAuditEntity;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.annotation.UniqueConstraint;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.security.SecurityComponents;

/**
 * Data source connection entity.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table(name = "FC_DATASOURCECONNECTION",
        uniqueConstraints = {
                @UniqueConstraint({ "name" }),
                @UniqueConstraint({ "description" }) })
public class DataSourceConnection extends BaseAuditEntity {

    @Column(name = "CONNECTION_NM", length = 64)
    private String name;

    @Column(name = "CONNECTION_DESC", length = 128)
    private String description;

    @Column(name = "CONNECTION_DIALECT", length = 32)
    private String dialect;

    @Column(name = "HOST", length = 96)
    private String host;

    @Column(name = "PORT", length = 8)
    private String port;

    @Column(name = "TARGET_DATABASE", length = 96, nullable = true)
    private String database;

    @Column(name = "TARGET_SERVICE", length = 96, nullable = true)
    private String service;

    @Column(name = "TARGET_SCHEMA", length = 96, nullable = true)
    private String schema;

    @Column(name = "USER_NAME", length = 64, nullable = true)
    private String userName;

    @Column(name = "PASSWORD", length = 2048, transformer = SecurityComponents.TWOWAY_STRING_CRYPTOGRAPH, nullable = true)
    private String password;

    @Override
    public String getDescription() {
        return this.description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDialect() {
        return dialect;
    }

    public void setDialect(String dialect) {
        this.dialect = dialect;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

}
