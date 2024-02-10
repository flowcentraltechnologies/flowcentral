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
package com.flowcentraltech.flowcentral.common.util;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.FactoryMap;

/**
 * Workspace naming utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class WorkspaceNamingUtils {

    private static FactoryMap<Long, String> workspaceCodes = new FactoryMap<Long, String>()
        {
            @Override
            protected String create(Long workspaceId, Object... arg1) throws Exception {
                return String.format("wksp_%04x", workspaceId);
            }

        };

    private WorkspaceNamingUtils() {

    }

    public static String getWorkspaceCodeFromId(Long workspaceId) throws UnifyException {
        return workspaceCodes.get(workspaceId);
    }
}
