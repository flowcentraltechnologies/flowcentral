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
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.studio.web.widgets.ReportEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.ReportEditor.Design;
import com.flowcentraltech.flowcentral.studio.web.widgets.ReportEditor.ReportColumn;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Report preview object
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ReportPreview {

    private static final int PRIVIEW_ITEMS_PER_PAGE = 10;

    private final AppletUtilities au;

    private final ReportEditor reportEditor;

    private EntityTable entityTable;

    private Design oldDesign;

    public ReportPreview(AppletUtilities au, ReportEditor reportEditor) {
        this.au = au;
        this.reportEditor = reportEditor;
    }

    public EntityTable getEntityTable() {
        return entityTable;
    }

    public void reload() throws UnifyException {
        Design design = reportEditor.getDesign();
        if (oldDesign != design) {
            final EntityDef entityDef = reportEditor.getEntityDef();
            TableDef.Builder tdb = TableDef.newBuilder(entityDef, "Preview", false, false, "studio.previewReport",
                    "Priview Report", 0L, 0L);
            tdb.sortHistory(4);
            tdb.itemsPerPage(PRIVIEW_ITEMS_PER_PAGE);
            if (design != null && design.getColumns() != null) {
                for (ReportColumn reportColumn : design.getColumns()) {
                    String renderer = InputWidgetUtils.constructRenderer(au.getWidgetTypeDef(reportColumn.getWidget()),
                            entityDef.getFieldDef(reportColumn.getFldNm()));
                    OrderType order = OrderType.fromCode(reportColumn.getOrder());
                    tdb.addColumnDef(reportColumn.getFldLabel(), reportColumn.getFldNm(), renderer, null, order,
                            reportColumn.getWidth(), false, false, false, false, false, false, false);
                }
            }

            entityTable = new EntityTable(au, tdb.build(), null);
            Restriction searchRestriction = null;
            entityTable.setSourceObjectClearSelected(searchRestriction);
            oldDesign = design;
        }
    }
}
