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

package com.flowcentraltech.flowcentral.system.data;

/**
 * Data source connection definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class DataSourceConnectionDef {

    private String name;

    private String description;

    private String dialect;

    private String host;

    private String port;

    private String database;

    private String service;

    private String schema;

    private String userName;

    private String password;

    private Long id;

    private long versionNo;

    public DataSourceConnectionDef(String name, String description, String dialect, String host, String port,
            String database, String service, String schema, String userName, String password, Long id, long versionNo) {
        this.name = name;
        this.description = description;
        this.dialect = dialect;
        this.host = host;
        this.port = port;
        this.database = database;
        this.service = service;
        this.schema = schema;
        this.userName = userName;
        this.password = password;
        this.id = id;
        this.versionNo = versionNo;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDialect() {
        return dialect;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getDatabase() {
        return database;
    }

    public String getService() {
        return service;
    }

    public String getSchema() {
        return schema;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public Long getId() {
        return id;
    }

    public long getVersionNo() {
        return versionNo;
    }

}
