/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.FormBreadCrumbsWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Form bread crumbs writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(FormBreadCrumbsWidget.class)
@Component("formbreadcrumbs-writer")
public class FormBreadCrumbsWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        FormBreadCrumbsWidget frmBreadCrumbsWidget = (FormBreadCrumbsWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, frmBreadCrumbsWidget);
        writer.write(">");
        BreadCrumbs frmBreadCrumbs = frmBreadCrumbsWidget.getBreadCrumbs();
        if (frmBreadCrumbs != null) {
            writer.write("<span class=\"base\">");
            if (frmBreadCrumbs.getDepth() == 0) {
                writeFontIcon(writer, "stop");
            } else {
                writeFontIcon(writer, "arrow-left");
            }
            
            writer.write("</span>");
        }
        
        writer.write("</div>");
    }

}
