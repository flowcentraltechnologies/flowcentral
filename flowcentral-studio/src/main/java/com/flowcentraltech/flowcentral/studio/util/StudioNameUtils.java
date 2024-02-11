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

package com.flowcentraltech.flowcentral.studio.util;

import com.flowcentraltech.flowcentral.studio.constants.StudioAppComponentType;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Studio name utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class StudioNameUtils {

    private static final int STUDIOAPPCOMPTYPE_CODE_LEN = 4;

    private StudioNameUtils() {

    }

    public static String getStudioAppletName(String applicationName, StudioAppComponentType type, Long instId) {
        return applicationName + "." + type.code() + instId;
    }

    public static StudioAppletNameParts getStudioAppletNameParts(String appletName) {
        String[] po = StringUtils.dotSplit(appletName);
        return new StudioAppletNameParts(po[0],
                StudioAppComponentType.fromCode(po[1].substring(0, STUDIOAPPCOMPTYPE_CODE_LEN)),
                Long.valueOf(po[1].substring(STUDIOAPPCOMPTYPE_CODE_LEN)));
    }

    public static class StudioAppletNameParts {

        private String applicationName;

        private StudioAppComponentType type;

        private Long instId;

        public StudioAppletNameParts(String applicationName, StudioAppComponentType type, Long instId) {
            this.applicationName = applicationName;
            this.type = type;
            this.instId = instId;
        }

        public String getApplicationName() {
            return applicationName;
        }

        public StudioAppComponentType getType() {
            return type;
        }

        public Long getInstId() {
            return instId;
        }

        public boolean isNullInstId() {
            return instId == null || instId.longValue() == 0L;
        }
    }
}
