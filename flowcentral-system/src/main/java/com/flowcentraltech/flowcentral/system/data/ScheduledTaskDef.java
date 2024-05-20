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

package com.flowcentraltech.flowcentral.system.data;

import com.flowcentraltech.flowcentral.common.data.ParamValuesDef;

/**
 * Schedule task runtime info.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class ScheduledTaskDef {
    
    private Long id;

    private Long tenantId;

    private String userLoginId;

    private String lock;

    private String description;

    private String taskName;

    private long startOffset;

    private long endOffset;

    private long repeatMillSecs;

    private String[] weekdays;

    private String[] days;

    private String[] months;

    private ParamValuesDef paramValuesDef;

    private long version;

    public ScheduledTaskDef(Long id, Long tenantId, String userLoginId, String lock, String description, String taskName, long startOffset, long endOffset,
            long repeatMillSecs, String[] weekdays, String[] days, String[] months, ParamValuesDef paramValuesDef,
            long version) {
        this.id = id;
        this.tenantId = tenantId;
        this.userLoginId = userLoginId;
        this.lock = lock;
        this.description = description;
        this.taskName = taskName;
        this.startOffset = startOffset;
        this.endOffset = endOffset;
        this.repeatMillSecs = repeatMillSecs;
        this.weekdays = weekdays;
        this.days = days;
        this.months = months;
        this.paramValuesDef = paramValuesDef;
        this.version = version;
    }

    public Long getId() {
        return id;
    }

    public Long getTenantId() {
        return tenantId;
    }

    public String getUserLoginId() {
        return userLoginId;
    }

    public String getLock() {
        return lock;
    }

    public String getDescription() {
        return description;
    }

    public String getTaskName() {
        return taskName;
    }

    public long getStartOffset() {
        return startOffset;
    }

    public long getEndOffset() {
        return endOffset;
    }

    public long getRepeatMillSecs() {
        return repeatMillSecs;
    }

    public String[] getWeekdays() {
        return weekdays;
    }

    public String[] getDays() {
        return days;
    }

    public String[] getMonths() {
        return months;
    }

    public ParamValuesDef getParamValuesDef() {
        return paramValuesDef;
    }

    public long getVersion() {
        return version;
    }

}
