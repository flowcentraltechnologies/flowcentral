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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.TableColumnDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTreeTable;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTreeTable.EntityTreeItem;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTreeTable.EntityTreeLevel;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTreeTable.TableColumnInfo;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTreeTableWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.PushType;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Entity tree table widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(EntityTreeTableWidget.class)
@Component("fc-entitytreetable-writer")
public class EntityTreeTableWriter extends AbstractControlWriter {

    @Configurable
    private AppletUtilities au;

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        EntityTreeTableWidget tableWidget = (EntityTreeTableWidget) widget;
        EntityTreeTable table = tableWidget.getEntityTreeTable();
        if (table != null) {
            widget.setDisabled(true); // TODO Remove
            if (table.isMultiSelect()) {
                writeHiddenPush(writer, tableWidget.getSelectCtrl(), PushType.CHECKBOX);
            }

            final boolean classicMode = au.system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.ALL_TABLE_IN_CLASSIC_MODE);
            final String tableClass = classicMode ? "table classic" : "table";
            table.setTableClass(tableClass);
            writer.write("<div");
            writeTagAttributes(writer, tableWidget);
            writer.write(">");
            writer.write("<div><table");
            writeTagId(writer, tableWidget);
            writeTagStyleClass(writer, tableClass);
            writer.write(">");

            if (table.isMultiColumn()) {
                writeHeaderRow(writer, tableWidget, 0);
            } else {
                writer.write("<colgroup>");
                final int prefixColumns = table.isMultiSelect() ? table.getLevelCount() : table.getLevelCount() - 1;
                for (int i = 0; i < prefixColumns; i++) {
                    writer.write("<col class=\"cpre\">");
                }

                if (table.isShowLabel()) {
                    writer.write("<col class=\"clabel\">");
                }

                writer.write("<col class=\"cline\">");
                writer.write("</colgroup>");
            }

            writeBodyRows(writer, tableWidget);
            writer.write("</table></div>");
            writer.write("</div>");
        }
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        super.doWriteBehavior(writer, widget);
        EntityTreeTableWidget tableWidget = (EntityTreeTableWidget) widget;
        EntityTreeTable table = tableWidget.getEntityTreeTable();
        if (table != null) {
            // Append table rigging
            writer.beginFunction("fux.rigEntityTreeTable");
            writer.writeParam("pId", tableWidget.getId());
            writer.writeParam("pContId", tableWidget.getContainerId());
            writer.writeCommandURLParam("pCmdURL");
            if (table.isMultiSelect()) {
                writer.writeParam("pSelAllId", tableWidget.getSelectAllId());
                writer.writeParam("pSelCtrlId", tableWidget.getSelectCtrl().getId());
                writer.writeParam("pMultiSel", true);
                writer.writeParam("pMultiSelDepList", new String[] {});
                /* writer.writeParam("pLvlChain", table.getItemLevelChain()); */
            }
            writer.endFunction();
        }
    }

    private void writeHeaderRow(ResponseWriter writer, EntityTreeTableWidget tableWidget, int level)
            throws UnifyException {
        writer.write("<tr ");
        if (level > 0) {
            writer.write(" class=\"small\"");
        }
        writer.write(">");

        EntityTreeTable table = tableWidget.getEntityTreeTable();
        if (table != null) {
            writeHeaderMultiSelect(writer, tableWidget, level);

            final boolean sysHeaderUppercase = au.system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.ALL_TABLE_HEADER_TO_UPPERCASE);
            final boolean sysHeaderCenterAlign = au.system().getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.ALL_TABLE_HEADER_CENTER_ALIGNED);
            TableDef _tableDef = table.getLevel(level).getTableDef();
            for (TableColumnInfo tabelColumnInfo : table.getLevel(level).getColumnInfoList()) {
                writer.write("<th");
                if (sysHeaderCenterAlign || table.isCenterAlignHeaders()) {
                    writeTagStyle(writer, tabelColumnInfo.getStyle() + "text-align:center;");
                } else {
                    writeTagStyle(writer, tabelColumnInfo.getStyle());
                }

                writer.write("><span>");
                String caption = tabelColumnInfo.getLabel();
                if (caption != null) {
                    if (sysHeaderUppercase || _tableDef.isHeaderToUpperCase()) {
                        writer.writeWithHtmlEscape(caption.toUpperCase());
                    } else {
                        writer.writeWithHtmlEscape(caption);
                    }
                } else {
                    writer.writeHtmlFixedSpace();
                }

                writer.write("</span>");
                writer.write("</th>");
            }

            writer.write("</tr>");
        }
    }

    private void writeHeaderMultiSelect(ResponseWriter writer, EntityTreeTableWidget tableWidget, int level)
            throws UnifyException {
        EntityTreeTable table = tableWidget.getEntityTreeTable();
        if (table.isMultiSelect()) {
            writer.write("<th class=\"mselh\">");
            if (level == 0) {
                writer.write("<span");
                writeTagId(writer, "fac_" + tableWidget.getSelectAllId());
                if (tableWidget.isContainerDisabled()) {
                    writeTagStyleClass(writer, "g_cbd");
                } else {
                    writeTagStyleClass(writer, "g_cbb");
                }
                writer.write("/>");
                writer.write("<input type=\"checkbox\"");
                writeTagId(writer, tableWidget.getSelectAllId());
                writer.write("/>");
            }
            writer.write("</th>");
        }
    }

    private void writeBodyRows(ResponseWriter writer, EntityTreeTableWidget tableWidget) throws UnifyException {
        EntityTreeTable table = tableWidget.getEntityTreeTable();
        if (table != null) {
            List<EntityTreeItem> items = table.getItems();
            final int prefixColumns = table.isMultiSelect() ? table.getLevelCount() : table.getLevelCount() - 1;
            final int len = items.size();
            final boolean multiSelect = table.isMultiSelect();
            final boolean showLabel = table.isShowLabel();
            if (table.isMultiColumn()) {
                int lastDepth = 0;
                int tableClosureCount = 0;
                for (int i = 0; i < len; i++) {
                    EntityTreeItem item = items.get(i);
                    int currentDepth = item.getLevel();
                    if (currentDepth > lastDepth) {
                        // Begin inner table
                        int colspan = table.getLevel(currentDepth - 1).getColumnCount();
                        if (multiSelect) {
                            colspan++;
                        }

                        writer.write("<tr><td class=\"inner\" colspan = \"").write(colspan);
                        writer.write("\"><table"); // Add left margin class
                        writeTagStyleClass(writer, table.getTableClass());
                        writer.write(">");
                        writeHeaderRow(writer, tableWidget, currentDepth);
                        tableClosureCount++;
                        lastDepth++;
                    } else {
                        while (currentDepth < lastDepth) {
                            writeTableClosure(writer);
                            tableClosureCount--;
                            lastDepth--;
                        }
                    }

                    writer.write("<tr>");
                    writePreColumns(writer, tableWidget, item, i);
                    for (TableColumnInfo tabelColumnInfo : table.getLevel(currentDepth).getColumnInfoList()) {
                        TableColumnDef tabelColumnDef = tabelColumnInfo.getTableColumnDef();
                        Widget chWidget = tabelColumnInfo.getWidget();
                        chWidget.setEditable(tabelColumnDef.isEditable());
                        chWidget.setDisabled(tabelColumnDef.isDisabled());
                        chWidget.setValueStore(item.getInstValueStore());
                        writer.write("<td");
                        writeTagStyle(writer, chWidget.getColumnStyle());
                        writer.write(">");
                        writer.writeStructureAndContent(chWidget);
                        writer.write("</td>");
                    }

                    writer.write("</tr>");
                }

                while (tableClosureCount > 0) {
                    writeTableClosure(writer);
                    tableClosureCount--;
                }
            } else {
                for (int i = 0; i < len; i++) {
                    EntityTreeItem item = items.get(i);
                    final int level = item.getLevel();
                    EntityTreeLevel treeLevel = table.getLevel(level);
                    writer.write("<tr");
                    writeTagStyleClass(writer, "even");
                    writeTagName(writer, tableWidget.getRowId());
                    writer.write(">");

                    writePreColumns(writer, tableWidget, item, i);

                    int filler = prefixColumns - level;
                    if (multiSelect) {
                        filler--;
                    }

                    if (showLabel) {
                        writer.write("<td class=\"mlabel\" colspan=\"");
                        writer.write(filler + 1);
                        writer.write("\"><span>");
                        writer.writeWithHtmlEscape(treeLevel.getLabel());
                        writer.write("</span></td>");
                    }

                    writer.write("<td class=\"mline\" colspan=\"");
                    if (showLabel) {
                        writer.write(1);
                    } else {
                        writer.write(filler + 1);
                    }
                    writer.write("\"><span>");
                    ParameterizedStringGenerator generator = au.getStringGenerator(item.getInstValueStore(),
                            treeLevel.getLineFormat());
                    String line = generator.generate();
                    writer.writeWithHtmlEscape(line);
                    writer.write("</span></td>");
                    writer.write("</tr>");
                }
            }
        }
    }

    private void writeTableClosure(ResponseWriter writer) throws UnifyException {
        writer.write("</table></td></tr>");
    }

    private void writePreColumns(ResponseWriter writer, EntityTreeTableWidget tableWidget, EntityTreeItem item,
            int index) throws UnifyException {
        EntityTreeTable table = tableWidget.getEntityTreeTable();
        final int depth = item.getLevel();
        if (table != null && table.isMultiSelect()) {
            final Control selectCtrl = tableWidget.getSelectCtrl();
            final boolean selected = item.isSelected();
            if (table.isMultiColumn()) {
                writer.write("<td class=\"msel\">");
            } else {
                writer.write("<td class=\"msel pmsel\" colspan=\"");
                writer.write(depth + 1);
                writer.write("\">");
            }

            String namingIndexId = selectCtrl.getNamingIndexedId(index);
            EntityTreeLevel treeLevel = table.getLevel(depth);
            if (treeLevel.isMultiSelect()) {
                writer.write("<span ");
                writeTagId(writer, "fac_" + namingIndexId);
                if (tableWidget.isContainerDisabled()) {
                    if (selected) {
                        writeTagStyleClass(writer, "g_cbc");
                    } else {
                        writeTagStyleClass(writer, "g_cbd");
                    }
                } else {
                    if (selected) {
                        writeTagStyleClass(writer, "g_cba");
                    } else {
                        writeTagStyleClass(writer, "g_cbb");
                    }
                }
                writer.write("/>");
                writer.write("<input type=\"checkbox\"");
                writeTagId(writer, namingIndexId);
                writeTagName(writer, selectCtrl.getId());
                writeTagValue(writer, index);
                if (selected) {
                    writer.write(" checked=\"checked\"");
                }
                writer.write("/>");
                writer.write("</span>");
            }

            writer.write("</td>");
        } else if (depth > 0) {
            writer.write("<td class=\"mpre\" colspan=\"");
            writer.write(depth);
            writer.write("\"></td>");
        }
    }
}
