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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPredefinedTableConstants;
import com.flowcentraltech.flowcentral.application.data.Snapshots;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTable;
import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;

/**
 * Snapshots panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-snapshotspanel")
@UplBinding("web/studio/upl/snapshotspanel.upl")
public class SnapshotsPanel extends AbstractFlowCentralPanel {

    @Configurable
    private AppletUtilities appletUtilities;

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        Snapshots snapshots = getValue(Snapshots.class);
        if (snapshots != null && snapshots.getSnapshotsTable() == null) {
            BeanListTable snapshotsTable = new BeanListTable(appletUtilities,
                    appletUtilities.getTableDef(ApplicationPredefinedTableConstants.SNAPSHOT_TABLE), null);
            snapshotsTable.setSourceObjectClearSelected(snapshots.getDetails());
            snapshots.setSnapshotsTable(snapshotsTable);
        }
    }

}
