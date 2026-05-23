/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.audit.web.writers;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.audit.business.AuditModuleService;
import com.flowcentraltech.flowcentral.audit.web.widgets.EntityAuditSnapshotViewWidget;
import com.flowcentraltech.flowcentral.common.data.FormattedAudit;
import com.flowcentraltech.flowcentral.common.data.FormattedEntityAudit;
import com.flowcentraltech.flowcentral.common.data.FormattedFieldAudit;
import com.flowcentraltech.flowcentral.configuration.constants.AuditEventType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.constant.LocaleType;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractWidgetWriter;

/**
 * Entity audit snapshot view writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(EntityAuditSnapshotViewWidget.class)
@Component("entityauditsnapshotview-writer")
public class EntityAuditSnapshotViewWriter extends AbstractWidgetWriter {

    @Configurable
    private ApplicationModuleService applicationModuleService;

    @Configurable
    private AuditModuleService auditModuleService;

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        EntityAuditSnapshotViewWidget entityAuditSnapshotViewWidget = (EntityAuditSnapshotViewWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, entityAuditSnapshotViewWidget);
        writer.write(">");
        final Long entityAuditKeysId = entityAuditSnapshotViewWidget.getEntityAuditKeysId();
        final FormattedAudit formattedAudit = auditModuleService
                .getSnapshotFormattedAuditByEntityAuditKeys(entityAuditKeysId);
        final FormattedEntityAudit[] snapshots = formattedAudit.getSnapshots();
        final String fieldHeader = resolveSessionMessage("$m{entityauditsnapshotview.header.field}");
        final String oldValueHeader = resolveSessionMessage("$m{entityauditsnapshotview.header.oldvalue}");
        final String newValueHeader = resolveSessionMessage("$m{entityauditsnapshotview.header.newvalue}");
        for (int i = 0; i < snapshots.length; i++) {
            FormattedEntityAudit snapshot = snapshots[i];
            final EntityDef entityDef = applicationModuleService.getEntityDef(snapshot.getEntity());
            writer.write("<div style=\"display:table;width:100%;\">");
            writer.write("<div style=\"display:table-row;\">");
            for (int j = 0; j < i; j++) {
                // Child depth padding
                writer.write("<div class=\"eadepth\" style=\"display:table-cell;\">");
                writer.write("</div>");
            }

            // Snapshot
            writer.write("<div class=\"snapshot\" style=\"display:table-cell;\">");
            final AuditEventType eventType = AuditEventType.fromName(snapshot.getEventType());
            final boolean showHighlight = !eventType.isView();
            final String highlight = showHighlight
                    ? (eventType.isCreate() ? "eagreen" : (eventType.isUpdate() ? "eaorange" : "eared"))
                    : "eawhite";
            final String eventDesc = getListItemByKey(LocaleType.SESSION, "auditeventtypelist", eventType.code())
                    .getListDescription();
            writeHeader(writer, "$m{entityauditsnapshotview.entity}", entityDef.getLabel(), null);
            writeHeader(writer, "$m{entityauditsnapshotview.event}", eventDesc, highlight);
            writer.write("<div class=\"eabdy\">");

            // Details
            writer.write("<table class=\"eadetails\">");
            writer.write("<tr>");
            writer.write("<th style=\"width:20%;\">").writeWithHtmlEscape(fieldHeader).write("</th>");
            writer.write("<th style=\"width:40%;\">").writeWithHtmlEscape(oldValueHeader).write("</th>");
            writer.write("<th style=\"width:40%;\">").writeWithHtmlEscape(newValueHeader).write("</th>");
            writer.write("</tr>");
            for (FormattedFieldAudit fieldSnapshot : snapshot.getFields()) {
                if (entityDef.isField(fieldSnapshot.getFieldName())) {
                    final boolean highlightChange = showHighlight && fieldSnapshot.changed();
                    final boolean delete = eventType.isDelete();
                    writer.write("<tr>");
                    writer.write("<td>")
                            .writeWithHtmlEscape(resolveSessionMessage(
                                    entityDef.getFieldDef(fieldSnapshot.getFieldName()).getFieldLabel()))
                            .write("</td>");
                    writer.write("<td");
                    if (highlightChange && delete) {
                        writer.write(" class=\"").write(highlight).write("\"");
                    }
                    writer.write(">");
                    writeValue(writer, fieldSnapshot.getOldValue());
                    writer.write("</td>");
                    writer.write("<td");
                    if (highlightChange && !delete) {
                        writer.write(" class=\"").write(highlight).write("\"");
                    }
                    writer.write(">");
                    writeValue(writer, fieldSnapshot.getNewValue());
                    writer.write("</td>");
                    writer.write("</tr>");
                }
            }
            writer.write("</table>");

            writer.write("</div>");
            writer.write("</div>");
            // Snapshot end

            writer.write("</div>");
            writer.write("</div>");
        }

        writer.write("</div>");

    }

    private void writeHeader(ResponseWriter writer, String labelKey, String headerText, String textClass)
            throws UnifyException {
        writer.write("<div class=\"eaheader\">");
        writer.write("<span class=\"eaheaderlbl\">");
        writer.writeWithHtmlEscape(resolveSessionMessage(labelKey));
        writer.write(":");
        writer.write("</span>");
        if (textClass != null) {
            writer.write("<span class=\"eaheadertxtc");
            writer.write(" ").write(textClass);
        } else {
            writer.write("<span class=\"eaheadertxt");
        }

        writer.write("\">");
        writer.writeWithHtmlEscape(headerText);
        writer.write("</span>");
        writer.write("</div>");
    }

    private void writeValue(ResponseWriter writer, String[] val) throws UnifyException {
        if (val != null) {
            for (int i = 0; i < val.length; i++) {
                if (i > 0) {
                    writer.write("<br>");
                }

                writer.writeWithHtmlEscape(val[i]);
            }
        }
    }

}
