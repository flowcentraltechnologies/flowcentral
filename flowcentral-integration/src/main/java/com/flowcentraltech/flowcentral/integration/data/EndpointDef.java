/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.integration.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;
import com.flowcentraltech.flowcentral.common.data.VersionedEntityDef;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;

/**
 * End-point definition
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EndpointDef implements VersionedEntityDef {

    private String name;

    private String endpointName;

    private Long id;

    private long version;

    private ParamValuesDef endpointParamsDef;

    private Map<String, EndpointPathDef> paths;

    private List<EndpointPathDef> sources;

    private List<EndpointPathDef> destinations;

    public EndpointDef(String name, String endpointName, Long id, long version, ParamValuesDef endpointParamsDef,
            Map<String, EndpointPathDef> paths) {
        this.name = name;
        this.endpointName = endpointName;
        this.id = id;
        this.version = version;
        this.endpointParamsDef = endpointParamsDef;
        this.paths = paths;
    }

    public String getName() {
        return name;
    }

    public String getEndpointName() {
        return endpointName;
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public long getVersion() {
        return version;
    }

    public ParamValuesDef getEndpointParamsDef() {
        return endpointParamsDef;
    }

    public <T> T getParam(Class<T> type, String name) throws UnifyException {
        return DataUtils.convert(type, endpointParamsDef.getParamValue(name));
    }
    
    public EndpointPathDef getPathDef(String name) {
        return paths.get(name);
    }

    public boolean isPathDef(String name) {
        return paths.containsKey(name);
    }

    public List<EndpointPathDef> getSources() throws UnifyException {
        if (sources == null) {
            synchronized (this) {
                if (sources == null) {
                    sources = new ArrayList<EndpointPathDef>();
                    for (EndpointPathDef pathDef : paths.values()) {
                        if (pathDef.getDirection().isIncoming()) {
                            sources.add(pathDef);
                        }
                    }

                    DataUtils.sortAscending(sources, Listable.class, "listDescription");
                    sources = Collections.unmodifiableList(sources);
                }
            }
        }

        return sources;
    }

    public List<EndpointPathDef> getDestinations() throws UnifyException {
        if (destinations == null) {
            synchronized (this) {
                if (destinations == null) {
                    destinations = new ArrayList<EndpointPathDef>();
                    for (EndpointPathDef pathDef : paths.values()) {
                        if (pathDef.getDirection().isOutgoing()) {
                            destinations.add(pathDef);
                        }
                    }

                    DataUtils.sortAscending(destinations, Listable.class, "listDescription");
                    destinations = Collections.unmodifiableList(destinations);
                }
            }
        }

        return destinations;
    }
}
