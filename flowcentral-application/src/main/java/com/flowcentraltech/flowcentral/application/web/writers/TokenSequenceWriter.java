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
package com.flowcentraltech.flowcentral.application.web.writers;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.TokenSequenceEntry;
import com.flowcentraltech.flowcentral.application.web.widgets.TokenSequenceWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Token sequence widget writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(TokenSequenceWidget.class)
@Component("fc-tokensequence-writer")
public class TokenSequenceWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        TokenSequenceWidget tokenSequenceWidget = (TokenSequenceWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, tokenSequenceWidget);
        writer.write(">");

        writer.write("<table style=\"width:100%;height:100%;table-layout:fixed;\">");
        writer.write("<tr style=\"height:100%;\">");

        // Preview
        writer.write("<td class=\"previewfrm\">");
        writer.write("<span class=\"caption\">");
        writer.write(resolveSessionMessage("$m{tokensequence.template}"));
        writer.write("</span>");
        writer.write("<div class=\"previewbox\" style=\"display:block;width:100%;overflow-y:auto;\">");
        writer.write("<span class=\"previewbdy\">");
        String preview = tokenSequenceWidget.getPreview();
        if (!StringUtils.isBlank(preview)) {
            writer.writeWithHtmlEscape(preview);
        }
        writer.write("</span>");
        writer.write("</div>");
        writer.write("</td>");

        // Editor
        writer.write("<td class=\"editorfrm\">");
        List<ValueStore> valueStoreList = tokenSequenceWidget.getValueList();
        if (valueStoreList != null) {
            Control tokenSelectCtrl = tokenSequenceWidget.getTokenSelectCtrl();
            Control textCtrl = tokenSequenceWidget.getTextCtrl();
            Control fieldSelectCtrl = tokenSequenceWidget.getFieldSelectCtrl();
            Control paramCtrl = tokenSequenceWidget.getParamCtrl();
            Control generatorSelectCtrl = tokenSequenceWidget.getGeneratorSelectCtrl();
            Control moveUpCtrl = tokenSequenceWidget.getMoveUpCtrl();
            Control moveDownCtrl = tokenSequenceWidget.getMoveDownCtrl();
            Control deleteCtrl = tokenSequenceWidget.getDeleteCtrl();
            final int len = valueStoreList.size();
            final int last = len - 1;

            final String typeLabel = resolveSessionMessage("$m{tokensequence.type}");
            final String textLabel = resolveSessionMessage("$m{tokensequence.text}");
            final String fieldLabel = resolveSessionMessage("$m{tokensequence.field}");
            final String usesLabel = resolveSessionMessage("$m{tokensequence.usesformatter}");
            final String generatorLabel = resolveSessionMessage("$m{tokensequence.usesgenerator}");
            writer.write("<span class=\"caption\">");
            writer.write(resolveSessionMessage("$m{tokensequence.editor}"));
            writer.write("</span>");
            writer.write("<div class=\"editorbox\" style=\"display:block;width:100%;overflow-y:auto;\">");
            writer.write("<table class=\"editor\" style=\"display: block;width:100%;table-layout:fixed;\">");
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                TokenSequenceEntry fso = (TokenSequenceEntry) lineValueStore.getValueObject();
                writer.write("<tr class=\"line\">");
                writeValuesItem(writer, lineValueStore, tokenSelectCtrl, typeLabel);
                if (fso.isWithTokenType()) {
                    switch (fso.getTokenType()) {
                        case FORMATTED_PARAM:
                            writeValuesItem(writer, lineValueStore, fieldSelectCtrl, fieldLabel);
                            if (fso.isWithFieldName()) {
                                writeValuesItem(writer, lineValueStore, paramCtrl, usesLabel);
                            } else {
                                writeBlankValuesItem(writer);
                            }
                            break;
                        case GENERATOR_PARAM:
                            writeValuesItem(writer, lineValueStore, generatorSelectCtrl, generatorLabel);
                            writeBlankValuesItem(writer);
                            break;
                        case NEWLINE:
                            writeBlankValuesItem(writer);
                            writeBlankValuesItem(writer);
                            break;
                        case PARAM:
                            writeValuesItem(writer, lineValueStore, fieldSelectCtrl, fieldLabel);
                            writeBlankValuesItem(writer);
                            break;
                        case TEXT:
                            writeValuesItem(writer, lineValueStore, textCtrl, textLabel);
                            writeBlankValuesItem(writer);
                            break;
                        default:
                            break;

                    }
                } else {
                    writeBlankValuesItem(writer);
                    writeBlankValuesItem(writer);
                }               

                writer.write("<td class=\"atab\">");
                moveUpCtrl.setDisabled(i == 0);
                moveDownCtrl.setDisabled(i >= (len - 2));
                writeActionItem(writer, lineValueStore, moveUpCtrl);
                writeActionItem(writer, lineValueStore, moveDownCtrl);
                if (i < last) {
                    writeActionItem(writer, lineValueStore, deleteCtrl);
                }
                writer.write("</td>");
                writer.write("</tr>");
            }

            writer.write("</table>");
            writer.write("</div>");
        }
        writer.write("</td>");
        
        writer.write("</tr>");
        writer.write("</table>");

        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        super.doWriteBehavior(writer, widget);
        TokenSequenceWidget tokenSequenceWidget = (TokenSequenceWidget) widget;
        List<ValueStore> valueStoreList = tokenSequenceWidget.getValueList();
        List<String> csb = new ArrayList<String>();
        if (valueStoreList != null) {
            Control tokenSelectCtrl = tokenSequenceWidget.getTokenSelectCtrl();
            Control fieldSelectCtrl = tokenSequenceWidget.getFieldSelectCtrl();
            Control paramCtrl = tokenSequenceWidget.getParamCtrl();
            Control textCtrl = tokenSequenceWidget.getTextCtrl();
            Control generatorSelectCtrl = tokenSequenceWidget.getGeneratorSelectCtrl();
            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                TokenSequenceEntry fso = (TokenSequenceEntry) lineValueStore.getValueObject();
                writeBehavior(writer, tokenSequenceWidget, lineValueStore, tokenSelectCtrl);
                csb.add(tokenSelectCtrl.getId());
                if (fso.isWithTokenType()) {
                    switch (fso.getTokenType()) {
                        case FORMATTED_PARAM:
                            writeBehavior(writer, tokenSequenceWidget, lineValueStore, fieldSelectCtrl);
                            csb.add(fieldSelectCtrl.getId());
                            if (fso.isWithFieldName()) {
                                writeBehavior(writer, tokenSequenceWidget, lineValueStore, paramCtrl);
                                csb.add(paramCtrl.getId());
                            }
                            break;
                        case GENERATOR_PARAM:
                            writeBehavior(writer, tokenSequenceWidget, lineValueStore, generatorSelectCtrl);
                            csb.add(generatorSelectCtrl.getId());
                            break;
                        case NEWLINE:
                            break;
                        case PARAM:
                            writeBehavior(writer, tokenSequenceWidget, lineValueStore, fieldSelectCtrl);
                            csb.add(fieldSelectCtrl.getId());
                            break;
                        case TEXT:
                            writeBehavior(writer, tokenSequenceWidget, lineValueStore, textCtrl);
                            csb.add(textCtrl.getId());
                            break;
                        default:
                            break;

                    }
                }
            }
        }

        writer.beginFunction("fux.rigLineEntries");
        writer.writeParam("pId", tokenSequenceWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", tokenSequenceWidget.getContainerId());
        writer.writeParam("pMoveUpId", tokenSequenceWidget.getMoveUpCtrl().getBaseId());
        writer.writeParam("pMoveDownId", tokenSequenceWidget.getMoveDownCtrl().getBaseId());
        writer.writeParam("pDelId", tokenSequenceWidget.getDeleteCtrl().getBaseId());
        writer.writeParam("pOnChgId", DataUtils.toArray(String.class, csb));
        writer.endFunction();
    }

    private void writeValuesItem(ResponseWriter writer, ValueStore lineValueStore, Control ctrl, String label)
            throws UnifyException {
        writer.write("<td class=\"vitem\">");
        writer.write("<div style=\"display:table;width:100%;\">");
        writer.write("<div style=\"display:table-row;\">");
        writer.write("<div style=\"display:table-cell;vertical-align:top;\">");
        writer.write("<span class=\"label\">");
        writer.write(label);
        writer.write("</span>");
        writer.write("</div>");
        writer.write("<div style=\"display:table-cell;vertical-align:top;\">");
        ctrl.setValueStore(lineValueStore);
        writer.writeStructureAndContent(ctrl);
        writer.write("</div>");
        writer.write("</div>");
        writer.write("</div>");
        writer.write("</td>");
    }

    private void writeBlankValuesItem(ResponseWriter writer) throws UnifyException {
        writer.write("<td class=\"vitem\">");
        writer.write("</td>");
    }

    private void writeActionItem(ResponseWriter writer, ValueStore lineValueStore, Control ctrl) throws UnifyException {
        writer.write("<span style=\"display:inline-block;\">");
        ctrl.setValueStore(lineValueStore);
        writer.writeStructureAndContent(ctrl);
        writer.write("</span>");
    }

    private void writeBehavior(ResponseWriter writer, TokenSequenceWidget tokenSequenceWidget,
            ValueStore lineValueStore, Control ctrl) throws UnifyException {
        ctrl.setValueStore(lineValueStore);
        writer.writeBehavior(ctrl);
        if (tokenSequenceWidget.isContainerEditable()) {
            addPageAlias(tokenSequenceWidget.getId(), ctrl);
        }
    }
}
