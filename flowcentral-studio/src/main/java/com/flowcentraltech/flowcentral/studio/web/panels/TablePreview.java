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

package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.studio.web.widgets.TableEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.TableEditor.Design;
import com.flowcentraltech.flowcentral.studio.web.widgets.TableEditor.TableColumn;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.OrderType;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Table preview object
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TablePreview {

    private static final int PRIVIEW_ITEMS_PER_PAGE = 10;

    private final AppletUtilities au;

    private final TableEditor tableEditor;

    private EntityTable entityTable;

    private Design oldDesign;

    public TablePreview(AppletUtilities au, TableEditor tableEditor) {
        this.au = au;
        this.tableEditor = tableEditor;
    }

    public EntityTable getEntityTable() {
        return entityTable;
    }

    public void reload() throws UnifyException {
        Design design = tableEditor.getDesign();
        if (oldDesign != design) {
            final EntityDef entityDef = tableEditor.getEntityDef();
            TableDef.Builder tdb = TableDef.newBuilder(entityDef, "Preview", false, false, "studio.previewTable",
                    "Priview Table", 0L, 0L);
            tdb.sortHistory(4);
            tdb.itemsPerPage(PRIVIEW_ITEMS_PER_PAGE);
            if (design != null && design.getColumns() != null) {
                for (TableColumn tableColumn : design.getColumns()) {
                    String renderer = InputWidgetUtils.constructRenderer(au.getWidgetTypeDef(tableColumn.getWidget()),
                            entityDef.getFieldDef(tableColumn.getFldNm()));
                    OrderType order = OrderType.fromCode(tableColumn.getOrder());
                    tdb.addColumnDef(tableColumn.getLabel(), tableColumn.getFldNm(), renderer, tableColumn.getLink(),
                            order, tableColumn.getWidth(), tableColumn.isSwitchOnChange(), tableColumn.isHidden(),
                            tableColumn.isDisabled(), tableColumn.isEditable(), tableColumn.isSort(),
                            tableColumn.isSummary());
                }
            }

            entityTable = new EntityTable(au, tdb.build(), null);
            Restriction searchRestriction = null;
            entityTable.setSourceObject(searchRestriction);
            oldDesign = design;
        }
    }
}
