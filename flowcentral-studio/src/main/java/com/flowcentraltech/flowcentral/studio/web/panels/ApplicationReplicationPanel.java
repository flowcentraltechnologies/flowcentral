/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.application.constants.ApplicationReplicationTaskConstants;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractApplicationPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.task.TaskSetup;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;

/**
 * Application replication panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-applicationreplicationpanel")
@UplBinding("web/studio/upl/applicationreplicationpanel.upl")
public class ApplicationReplicationPanel extends AbstractApplicationPanel {

    @Action
    public void executeReplication() throws UnifyException {
        ApplicationReplication replication = getReplication();
        if (replication.getSourceApplication() == null) {
            hintUser(MODE.ERROR, "$m{applicationreplicationpanel.replication.error.source}");
            return;
        }

        if (replication.getTargetModule() == null) {
            hintUser(MODE.ERROR, "$m{applicationreplicationpanel.replication.error.module}");
            return;
        }

        if (replication.getTargetApplication() == null) {
            hintUser(MODE.ERROR, "$m{applicationreplicationpanel.replication.error.target}");
            return;
        }

        if (replication.getReplicationRuleFile() == null) {
            hintUser(MODE.ERROR, "$m{applicationreplicationpanel.replication.error}");
            return;
        }

        TaskSetup taskSetup = TaskSetup.newBuilder(ApplicationReplicationTaskConstants.APPLICATION_REPLICATION_TASK_NAME)
                .setParam(ApplicationReplicationTaskConstants.SOURCE_APPLICATION_NAME,
                        replication.getSourceApplication())
                .setParam(ApplicationReplicationTaskConstants.DESTINATION_MODULE_NAME, replication.getTargetModule())
                .setParam(ApplicationReplicationTaskConstants.DESTINATION_APPLICATION_NAME,
                        replication.getTargetApplication())
                .setParam(ApplicationReplicationTaskConstants.REPLICATION_RULES_FILE,
                        replication.getReplicationRuleFile())
                .logMessages().build();
        final String onSuccessPath = "/studio/applicationreplication/clear";
        launchTaskWithMonitorBox(taskSetup, "$m{applicationreplicationpanel.replication.title}", onSuccessPath, null);
    }

    protected ApplicationReplication getReplication() throws UnifyException {
        return getValue(ApplicationReplication.class);
    }
}
