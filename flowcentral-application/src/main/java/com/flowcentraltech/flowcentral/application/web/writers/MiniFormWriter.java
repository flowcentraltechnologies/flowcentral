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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormWidget;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormWidget.FormSection;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormWidget.FormSection.RowRegulator;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormWidget.FormWidget;
import com.flowcentraltech.flowcentral.common.web.util.WidgetWriterUtils;
import com.flowcentraltech.flowcentral.common.web.util.WidgetWriterUtils.ColumnRenderInfo;
import com.flowcentraltech.flowcentral.system.business.SystemModuleService;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.EventHandler;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Mini form widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(MiniFormWidget.class)
@Component("fc-miniform-writer")
public class MiniFormWriter extends AbstractControlWriter {

    @Configurable
    private SystemModuleService systemModuleService;

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        MiniFormWidget miniFormWidget = (MiniFormWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, miniFormWidget);
        writer.write(">");
        if (miniFormWidget.isMainForm()) {
            String errMsg = (String) getRequestAttribute(AppletRequestAttributeConstants.SILENT_FORM_ERROR_MSG);
            if (!StringUtils.isBlank(errMsg)) {
                writer.write("<div class=\"mwarn\"><span style=\"display:block;text-align:center;\">");
                writer.write(errMsg);
                writer.write("</span></div>");
            }
        }

        FormContext ctx = miniFormWidget.getCtx();
        miniFormWidget.evaluateWidgetStates();
        final String quickView = resolveSessionMessage("$m{button.quickview}");
        final boolean isClassicFormSection = systemModuleService.getSysParameterValue(boolean.class,
                ApplicationModuleSysParamConstants.FORM_SECTION_CLASSIC_MODE);
        boolean isPreGap = false;
        if (miniFormWidget.isStrictRows()) {
            for (FormSection formSection : miniFormWidget.getFormSectionList()) {
                if (formSection.isVisible()) {
                    writeSectionLabel(writer, ctx, formSection, isPreGap, isClassicFormSection);
                    writer.write("<div class=\"mftable\">");
                    RowRegulator rowRegulator = formSection.getRowRegulator();
                    rowRegulator.resetRows();
                    List<ColumnRenderInfo> columnInfos = WidgetWriterUtils
                            .getColumnRenderInfoList(formSection.getColumnsType());
                    while (rowRegulator.nextRow()) {
                        writer.write("<div class=\"mfrow\">");
                        int i = 0;
                        for (FormWidget formWidget : rowRegulator.getRowWidgets()) {
                            writer.write("<div class=\"mfcol\" ").write(columnInfos.get(i).getColumnStyle()).write(">");
                            writeFieldCell(writer, ctx, formWidget, quickView);
                            writer.write("</div>");
                            i++;
                        }
                        writer.write("</div>");
                    }
                    writer.write("</div>");
                    isPreGap = true;
                }
            }
        } else {
            for (FormSection formSection : miniFormWidget.getFormSectionList()) {
                if (formSection.isVisible()) {
                    writeSectionLabel(writer, ctx, formSection, isPreGap, isClassicFormSection);
                    writer.write("<div class=\"mftable\">");
                    writer.write("<div class=\"mfrow\">");
                    final int columns = formSection.getColumns();
                    List<ColumnRenderInfo> columnInfos = WidgetWriterUtils
                            .getColumnRenderInfoList(formSection.getColumnsType());
                    for (int i = 0; i < columns; i++) {
                        writer.write("<div class=\"mfcol\" ").write(columnInfos.get(i).getColumnStyle()).write(">");
                        for (FormWidget formWidget : formSection.getFormWidgetList(i)) {
                            writeFieldCell(writer, ctx, formWidget, quickView);
                        }
                        writer.write("</div>");
                    }
                    writer.write("</div>");
                    writer.write("</div>");
                    isPreGap = true;
                }
            }
        }

        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget, EventHandler[] handlers)
            throws UnifyException {
        super.doWriteBehavior(writer, widget, handlers);
        final MiniFormWidget miniFormWidget = (MiniFormWidget) widget;
        final FormContext ctx = miniFormWidget.getCtx();
        final List<EventHandler> switchOnChangeHandlers = ctx.getFormSwitchOnChangeHandlers();
        final List<Preview> previews = ctx.isPreviewFormMode() ? Collections.emptyList() : new ArrayList<Preview>();
        for (FormSection formSection : miniFormWidget.getFormSectionList()) {
            if (formSection.isVisible()) {
                final int columns = formSection.getColumns();
                for (int col = 0; col < columns; col++) {
                    for (FormWidget formWidget : formSection.getFormWidgetList(col)) {
                        Widget chWidget = formWidget.getResolvedWidget();
                        if (chWidget.isVisible()) {
                            final String cId = chWidget.isBindEventsToFacade() ? chWidget.getFacadeId()
                                    : chWidget.getId();
                            final boolean refreshesContainer = chWidget.isRefreshesContainer();
                            if (refreshesContainer) {
                                writer.setKeepPostCommandRefs();
                            } else {
                                writer.writeBehavior(chWidget);
                            }

                            if (switchOnChangeHandlers != null
                                    && (refreshesContainer || formWidget.isSwitchOnChange())) {
                                for (EventHandler eventHandler : switchOnChangeHandlers) {
                                    writer.writeBehavior(eventHandler, cId, formWidget.getFieldName());
                                }
                            }

                            if (refreshesContainer) {
                                writer.writeBehavior(chWidget);
                                writer.clearKeepPostCommandRefs();
                            }

                            addPageAlias(miniFormWidget.getId(), chWidget);

                            if (!ctx.isPreviewFormMode() && formWidget.isWithPreviewForm()) {
                                previews.add(new Preview(chWidget.getId(), formWidget.getPreviewForm()));
                            }
                        }
                    }
                }
            }
        }

        // Form should be saved on content switch
        getRequestContextUtil().addOnSaveContentWidget(miniFormWidget.getId());

        writer.beginFunction("fux.rigMiniForm");
        writer.writeParam("pId", miniFormWidget.getId());
        writer.writeParam("pContId", miniFormWidget.getContainerId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pFocusMemId", ctx.getFocusMemoryId());
        writer.writeParam("pTabMemId", ctx.getTabMemoryId());
        writer.writeObjectParam("pPreview", DataUtils.toArray(Preview.class, previews));
        if (miniFormWidget.isMainForm()) {
            writer.writeParam("pTabWidId", miniFormWidget.getCtx().getTabWidgetIds());
        }
        writer.endFunction();

    }

    private boolean writeSectionLabel(ResponseWriter writer, FormContext ctx, FormSection formSection, boolean isPreGap,
            boolean isClassicFormSection) throws UnifyException {
        if (formSection.isWithLabel()) {
            if (isPreGap) {
                writer.write("<div class=\"mfgap\"></div>");
            }

            writer.write(isClassicFormSection ? "<div class=\"mfsectionl\"><span>" : "<div class=\"mfsection\"><span>")
                    .writeWithHtmlEscape(formSection.getLabel()).write("</span></div>");
            if (ctx.isWithSectionError(formSection.getName())) {
                writer.write("<div>");
                for (String msg : ctx.getSectionError(formSection.getName())) {
                    writer.write("<div><span class=\"errmsg\">").write(resolveSessionMessage(msg))
                            .write("</span></div>");
                }
                writer.write("</div>");
            }
            return true;
        }

        return false;
    }

    private void writeFieldCell(ResponseWriter writer, FormContext ctx, FormWidget formWidget, String quickView)
            throws UnifyException {
        if (formWidget != null) {           
            Widget chWidget = formWidget.getResolvedWidget();
            if (chWidget.isVisible()) {
                writer.write("<div class=\"mffield\">");
                writer.write("<div class=\"mffieldrow\">");

                writer.write("<div class=\"mfpre").write("\">");
                writer.write("<div class=\"mflabel\">");
                writer.write("<span>");
                if (formWidget.isRequired()) {
                    writer.write("<img class=\"mfreq\"src=\"");
                    if (StringUtils.isBlank(chWidget.getValue(String.class))) {
                        writer.writeFileImageContextURL("$t{images/red_asterix.png}");
                    } else {
                        writer.writeFileImageContextURL("$t{images/gray_asterix.png}");
                    }
                    writer.write("\"/>");
                }
                writer.writeWithHtmlEscape(formWidget.getFieldLabel());
                writer.write("</span>");
                writer.write("</div>");
                writer.write("</div>");

                writer.write("<div class=\"mfmid\">");
                writer.write("<div class=\"mfcon\">");
                writer.write("<div class=\"mfcontent\">");
                writer.writeStructureAndContent(chWidget);
                if (ctx.isWithFieldError(formWidget.getFieldName())) {
                    for (String msg : ctx.getFieldError(formWidget.getFieldName())) {
                        writer.write("<div><span class=\"errmsg\">").write(resolveSessionMessage(msg))
                                .write("</span></div>");
                    }
                }

                writer.write("</div>");
                writer.write("</div>");
                writer.write("</div>");

                if (!ctx.isPreviewFormMode() && formWidget.isWithPreviewForm()) {
                    final String bId = "view_" + chWidget.getId();
                    writer.write("<div class=\"mfview\">");
                    writeSymbolButton(writer, bId, "mfact", "info", quickView);
                    writer.write("</div>");
                }

                writer.write("<div class=\"mfpost").write("\">");
                writer.write("</div>");

                writer.write("</div>");
                writer.write("</div>");
            }         
        }
    }
    
    public class Preview {
        
        private String id;
        
        private String frm;

        public Preview(String id, String frm) {
            this.id = id;
            this.frm = frm;
        }

        public String getId() {
            return id;
        }

        public String getFrm() {
            return frm;
        }
    }
}
