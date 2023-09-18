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

package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationPredefinedTableConstants;
import com.flowcentraltech.flowcentral.application.data.Attachments;
import com.flowcentraltech.flowcentral.application.web.widgets.BeanListTable;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralPanel;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplBinding;

/**
 * Attachments panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-attachmentspanel")
@UplBinding("web/application/upl/attachmentspanel.upl")
public class AttachmentsPanel extends AbstractFlowCentralPanel implements FormPanel {

    private static final String ATTACHMENTS_TABLE_PAGE_ATTRIBUTE = "page.attachments.viewer.table";
    
    @Configurable
    private AppletUtilities appletUtilities;
    
    
    public final void setAppletUtilities(AppletUtilities appletUtilities) {
        this.appletUtilities = appletUtilities;
    }

    @Override
    public void switchState() throws UnifyException {
        super.switchState();
        Attachments attachments = getValue(Attachments.class); 
        if (attachments != null) {
            BeanListTable attachmentsTable = getAttachmentsTable();
            attachmentsTable.setSourceObjectClearSelected(attachments.getAttachments());
        }
     }

    @Override
    public List<FormValidationErrors> validate(EvaluationMode evaluationMode) throws UnifyException {
        return Collections.emptyList();
    }
    
    private BeanListTable getAttachmentsTable() throws UnifyException {
        BeanListTable attachmentsTable = getPageAttribute(BeanListTable.class, ATTACHMENTS_TABLE_PAGE_ATTRIBUTE);
        if (attachmentsTable == null) {
            attachmentsTable = new BeanListTable(appletUtilities,
                    appletUtilities.getTableDef(ApplicationPredefinedTableConstants.ATTACHMENT_TABLE), null);
            setPageAttribute(ATTACHMENTS_TABLE_PAGE_ATTRIBUTE, attachmentsTable);
        }
        
        return attachmentsTable;
    }

}
