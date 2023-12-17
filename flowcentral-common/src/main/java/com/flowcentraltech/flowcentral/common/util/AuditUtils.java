/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

/**
 * Audit utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class AuditUtils {

    private AuditUtils() {

    }

    public static ChildAuditKey getChildAuditKey(String childFieldName, Long entityId) {
        return new ChildAuditKey(childFieldName + ":" + entityId, childFieldName, entityId);
    }

    public static ChildAuditKey reconstructChildAuditKey(String auditKey) {
        String[] parts = auditKey.split(":");
        return new ChildAuditKey(auditKey, parts[0], Long.valueOf(parts[1]));
    }

    public static class ChildAuditKey {

        private String key;

        private String childFieldName;

        private Long entityId;

        private ChildAuditKey(String key, String childFieldName, Long entityId) {
            this.key = key;
            this.childFieldName = childFieldName;
            this.entityId = entityId;
        }
        
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            
            if (!(o instanceof ChildAuditKey)) {
                return false;
            }
            
            return key.equals(((ChildAuditKey) o).key);
        }
        
        public String getKey() {
            return key;
        }

        public String getFieldName() {
            return childFieldName;
        }

        public Long getEntityId() {
            return entityId;
        }
    }
}
