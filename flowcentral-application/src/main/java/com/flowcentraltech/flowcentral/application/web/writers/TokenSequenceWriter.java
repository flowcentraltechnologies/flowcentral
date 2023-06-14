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

import com.flowcentraltech.flowcentral.application.web.widgets.TokenSequence;
import com.flowcentraltech.flowcentral.application.web.widgets.TokenSequenceWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.util.DataUtils;
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
        writer.write("<td class=\"editorfrm\">");
        writer.write("<span class=\"caption\">");
        writer.write(resolveSessionMessage("$m{tokensequence.template}"));
        writer.write("</span>");
        writer.write("<div class=\"editorbox\" style=\"display:block;width:100%;\">");
        writer.writeStructureAndContent(tokenSequenceWidget.getPreviewTextCtrl());
        writer.write("</div>");
        writer.write("</td>");

        // Editor
        writer.write("<td class=\"optionsfrm\">");
        writer.write("<span class=\"caption\">");
        writer.write(resolveSessionMessage("$m{tokensequence.availableparameters}"));
        writer.write("</span>");
        writer.write("<div class=\"optionsbox\" style=\"display:block;width:100%;\">");
        TokenSequence tokenSequence = tokenSequenceWidget.getTokenSequence();
        if (tokenSequence != null) {
            Control fieldSelectCtrl = tokenSequenceWidget.getFieldSelectCtrl();
            fieldSelectCtrl.setDisabled(tokenSequenceWidget.isContainerDisabled());
            fieldSelectCtrl.setEditable(tokenSequenceWidget.isContainerEditable());
            List<? extends Listable> fieldParamList = tokenSequence.getEntityDef().getSortedFieldDefList();
            ValueStore fieldValueStore = new BeanValueListStore(fieldParamList);
            final int len = fieldValueStore.size();
            for (int i = 0; i < len; i++) {
                fieldValueStore.setDataIndex(i);
                fieldSelectCtrl.setValueStore(fieldValueStore);
                writer.writeStructureAndContent(fieldSelectCtrl);
            }
        }
        writer.write("</div></td>");

        writer.write("</tr>");
        writer.write("</table>");

        writer.write("</div>");
    }

    @Override
    protected void doWriteBehavior(ResponseWriter writer, Widget widget) throws UnifyException {
        super.doWriteBehavior(writer, widget);
        TokenSequenceWidget tokenSequenceWidget = (TokenSequenceWidget) widget;
        List<String> fsb = new ArrayList<String>();
        TokenSequence tokenSequence = tokenSequenceWidget.getTokenSequence();
        if (tokenSequence != null) {
            Control fieldSelectCtrl = tokenSequenceWidget.getFieldSelectCtrl();
            fieldSelectCtrl.setDisabled(tokenSequenceWidget.isContainerDisabled());
            fieldSelectCtrl.setEditable(tokenSequenceWidget.isContainerEditable());
            List<? extends Listable> fieldParamList = tokenSequence.getEntityDef().getSortedFieldDefList();
            ValueStore fieldValueStore = new BeanValueListStore(fieldParamList);
            final int len = fieldValueStore.size();
            for (int i = 0; i < len; i++) {
                fieldValueStore.setDataIndex(i);
                fieldSelectCtrl.setValueStore(fieldValueStore);
                writer.writeBehavior(fieldSelectCtrl);
                fsb.add(fieldSelectCtrl.getId());
            }
        }

        writer.beginFunction("fux.rigLineEntries");
        writer.writeParam("pId", tokenSequenceWidget.getId());
        writer.writeCommandURLParam("pCmdURL");
        writer.writeParam("pContId", tokenSequenceWidget.getContainerId());
        writer.writeParam("pPreviewId", tokenSequenceWidget.getPreviewId());
        writer.writeParam("pOnFldId", DataUtils.toArray(String.class, fsb));
        writer.endFunction();
    }
}
