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
        
        writer.write("<div style=\"display:table;width:100%;\">");
        writer.write("<div style=\"display:table-row;\">");
        
        //Preview
        writer.write("<div class=\"previewfrm\" style=\"display:table-cell;\">");
        writer.write("<span class=\"previewbdy\">");
        String preview = tokenSequenceWidget.getPreview();
        if (!StringUtils.isBlank(preview)) {
            writer.writeWithHtmlEscape(preview);
        }
        writer.write("</span>");
        writer.write("</div>");
        
        //Editor
        writer.write("<div class=\"editorfrm\" style=\"display:table-cell;\">");
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
            int len = valueStoreList.size();

            final String typeLabel = resolveSessionMessage("$m{tokensequence.type}");
            final String textLabel = resolveSessionMessage("$m{tokensequence.text}");
            final String fieldLabel = resolveSessionMessage("$m{tokensequence.field}");
            final String usesLabel = resolveSessionMessage("$m{tokensequence.usesformatter}");
            final String generatorLabel = resolveSessionMessage("$m{tokensequence.usesgenerator}");
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                TokenSequenceEntry fso = (TokenSequenceEntry) lineValueStore.getValueObject();
                writer.write("<div class=\"line\">");
                writeValuesItem(writer, lineValueStore, tokenSelectCtrl, typeLabel);
                if (fso.isWithTokenType()) {
                    switch(fso.getTokenType()) {
                        case FORMATTED_PARAM:
                            writeValuesItem(writer, lineValueStore, fieldSelectCtrl, fieldLabel);
                            if (fso.isWithFieldName()) {
                                writeValuesItem(writer, lineValueStore, paramCtrl, usesLabel);
                            }
                            break;
                        case GENERATOR_PARAM:
                            writeValuesItem(writer, lineValueStore, generatorSelectCtrl, generatorLabel);
                            break;
                        case NEWLINE:
                            break;
                        case PARAM:
                            writeValuesItem(writer, lineValueStore, fieldSelectCtrl, fieldLabel);
                            break;
                        case TEXT:
                            writeValuesItem(writer, lineValueStore, textCtrl, textLabel);
                            break;
                        default:
                            break;
                        
                    }
                                       
                    writer.write("<div class=\"atab\">");
                    moveUpCtrl.setDisabled(i == 0);
                    moveDownCtrl.setDisabled(i >= (len - 2));
                    writeActionItem(writer, lineValueStore, moveUpCtrl);
                    writeActionItem(writer, lineValueStore, moveDownCtrl);
                    writeActionItem(writer, lineValueStore, deleteCtrl);
                    writer.write("</div>");
                }
                writer.write("</div>");
            }
        }
        writer.write("</div>");
        writer.write("</div>");
        writer.write("</div>");
        
        
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
            Control generatorSelectCtrl = tokenSequenceWidget.getGeneratorSelectCtrl();
            final int len = valueStoreList.size();
            for (int i = 0; i < len; i++) {
                ValueStore lineValueStore = valueStoreList.get(i);
                TokenSequenceEntry fso = (TokenSequenceEntry) lineValueStore.getValueObject();
                writeBehavior(writer, tokenSequenceWidget, lineValueStore, tokenSelectCtrl);
                csb.add(tokenSelectCtrl.getId());
                if (fso.isWithTokenType()) {
                    switch(fso.getTokenType()) {
                        case FORMATTED_PARAM:
                            writeBehavior(writer, tokenSequenceWidget, lineValueStore, fieldSelectCtrl);
                            csb.add(fieldSelectCtrl.getId());
                            if (fso.isWithFieldName()) {
                                writeBehavior(writer, tokenSequenceWidget, lineValueStore, paramCtrl);
                            }
                            break;
                        case GENERATOR_PARAM:
                            writeBehavior(writer, tokenSequenceWidget, lineValueStore, generatorSelectCtrl);
                            break;
                        case NEWLINE:
                            break;
                        case PARAM:
                            writeBehavior(writer, tokenSequenceWidget, lineValueStore, fieldSelectCtrl);
                            break;
                        case TEXT:
                            break;
                        default:
                            break;
                        
                    }
                }
            }
        }

        writer.beginFunction("fux.rigFieldSequence");
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
        writer.write("<span class=\"label\">");
        writer.write(label);
        writer.write("</span>");
        writer.write("<span class=\"item\">");
        ctrl.setValueStore(lineValueStore);
        writer.writeStructureAndContent(ctrl);
        writer.write("</span>");
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
