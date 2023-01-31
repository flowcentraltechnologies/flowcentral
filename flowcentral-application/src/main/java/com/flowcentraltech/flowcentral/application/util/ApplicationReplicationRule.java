/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.application.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.constants.ReplicationMatchType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Application replication rule.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ApplicationReplicationRule {

    private final ReplicationMatchType type;

    private final Map<String, String> replace;

    private final String concat;

    private ApplicationReplicationRule nameRule;

    private ApplicationReplicationRule(ReplicationMatchType type, Map<String, String> replace, String concat) {
        this.type = type;
        this.replace = replace;
        this.concat = concat;
    }

    public ReplicationMatchType getType() {
        return type;
    }

    public Map<String, String> getReplace() {
        return replace;
    }

    public String getConcat() {
        return concat;
    }

    public ApplicationReplicationRule setNameRule(ApplicationReplicationRule nameRule) {
        this.nameRule = nameRule;
        return this;
    }

    public String apply(String str) throws UnifyException {
        if (str == null) {
            return ReplicationMatchType.NULL.equals(type) ? concat : str;
        }

        switch (type) {
            case ENTITY:
                if (ApplicationNameUtils.isLongName(str)) {
                    ApplicationEntityNameParts parts = ApplicationNameUtils.getApplicationEntityNameParts(str);
                    String prefix = null;
                    for (Map.Entry<String, String> entry : replace.entrySet()) {
                        if (parts.getApplicationName().startsWith(entry.getKey())) {
                            prefix = entry.getValue() + parts.getApplicationName().substring(entry.getKey().length());
                            break;
                        }
                    }

                    final String entityName = prefix != null && nameRule != null ? nameRule.apply(parts.getEntityName())
                            : parts.getEntityName();
                    final String applicationName = prefix != null ? prefix
                            : (concat != null ? concat + parts.getApplicationName() : parts.getApplicationName());
                    return ApplicationNameUtils.getApplicationEntityLongName(applicationName, entityName);
                }
            case NAME:
                for (Map.Entry<String, String> entry : replace.entrySet()) {
                    if (str.startsWith(entry.getKey())) {
                        return entry.getValue() + str.substring(entry.getKey().length());
                    }
                }
                return concat != null ? concat + StringUtils.capitalizeFirstLetter(str) : str;
            case CLASS:
                int index = str.lastIndexOf('.');
                if (index > 0) {
                    final String _package = str.substring(0, index);
                    str = str.substring(index + 1);
                    for (Map.Entry<String, String> entry : replace.entrySet()) {
                        if (str.startsWith(entry.getKey())) {
                            return _package + "." + entry.getValue() + str.substring(entry.getKey().length());
                        }
                    }

                    return _package + "." + (concat != null ? concat + str : str);
                }
                break;
            case NULL:
                break;
            case PREFIX:
                for (Map.Entry<String, String> entry : replace.entrySet()) {
                    if (str.startsWith(entry.getKey())) {
                        return entry.getValue() + str.substring(entry.getKey().length());
                    }
                }
                return concat != null ? concat + str : str;
            case SUFFIX:
                for (Map.Entry<String, String> entry : replace.entrySet()) {
                    if (str.endsWith(entry.getKey())) {
                        return str.substring(0, str.length() - entry.getKey().length()) + entry.getValue();
                    }
                }
                return concat != null ? str + concat : str;
            case WILD: {
                for (Map.Entry<String, String> entry : replace.entrySet()) {
                    str = str.replaceAll(entry.getKey(), entry.getValue());
                }
                return str;
            }
            case WILD_PREFIX: {
                String ostr = str;
                for (Map.Entry<String, String> entry : replace.entrySet()) {
                    str = str.replaceAll(entry.getKey(), entry.getValue());
                }
                return concat != null && ostr.equals(str) ? concat + str : str;
            }
            case WILD_SUFFIX: {
                String ostr = str;
                for (Map.Entry<String, String> entry : replace.entrySet()) {
                    str = str.replaceAll(entry.getKey(), entry.getValue());
                }
                return concat != null && ostr.equals(str) ? str + concat : str;
            }
            default:
                break;
        }

        return str;
    }

    public static class Builder {

        private ReplicationMatchType type;

        private Map<String, String> replace;

        private String concat;

        public Builder(ReplicationMatchType type) {
            this.type = type;
            this.replace = new LinkedHashMap<String, String>();
        }

        public Builder replace(String target, String val) {
            replace.put(target, val);
            return this;
        }

        public Builder concat(String concat) {
            this.concat = concat;
            return this;
        }

        public ApplicationReplicationRule build() {
            return new ApplicationReplicationRule(type, Collections.unmodifiableMap(replace), concat);
        }
    }
}
