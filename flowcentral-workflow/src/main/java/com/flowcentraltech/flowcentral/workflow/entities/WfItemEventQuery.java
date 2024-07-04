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

package com.flowcentraltech.flowcentral.workflow.entities;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.entities.BaseEntityQuery;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.IsNotNull;
import com.tcdng.unify.core.criterion.Less;

/**
 * Workflow item event query.
 * 
 * @author FlowCentral Technologies Limited
 * @version 1.0
 */
public class WfItemEventQuery extends BaseEntityQuery<WfItemEvent> {

    public WfItemEventQuery() {
        super(WfItemEvent.class);
    }

    public WfItemEventQuery wfItemHistId(Long wfItemHistId) {
        return (WfItemEventQuery) addEquals("wfItemHistId", wfItemHistId);
    }

    public WfItemEventQuery reminderDue(Date now) {
        return (WfItemEventQuery) addRestriction(
                new And().add(new IsNotNull("reminderDt")).add(new Less("reminderDt", now)));
    }

    public WfItemEventQuery expirationDue(Date now) {
        return (WfItemEventQuery) addRestriction(
                new And().add(new IsNotNull("expectedDt")).add(new Less("expectedDt", now)));
    }

    public WfItemEventQuery criticalDue(Date now) {
        return (WfItemEventQuery) addRestriction(
                new And().add(new IsNotNull("criticalDt")).add(new Less("criticalDt", now)));
    }

    public WfItemEventQuery commentsOnly() {
        return (WfItemEventQuery) addIsNotNull("comment");
    }

}
