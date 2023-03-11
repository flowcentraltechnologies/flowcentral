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

import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs.BreadCrumb;
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
 * @since 1.0
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
            writer.write("<ul class=\"base\">");
            // History
            for (BreadCrumb breadCrumb : frmBreadCrumbs.getHistCrumbList()) {
                writeCrumb(writer, breadCrumb, true);
            }

            // Current
            writeCrumb(writer, frmBreadCrumbs.getLastBreadCrumb(), false);
            writer.write("</ul>");
        }
        writer.write("</div>");
    }

    private void writeCrumb(ResponseWriter writer, BreadCrumb breadCrumb, boolean history) {
        writer.write("<li class=\"");
        if (!history) {
            writer.write("last");
        }
        writer.write("\">");
        if (breadCrumb.getStepIndex() > 0) {
            writer.write("<span class=\"hdstep\">").write(breadCrumb.getStepIndex()).write("</span>");
        }

        writer.write("<div style=\"display: inline-block;\">");
        writer.write("<span class=\"hdtitle");
        if (history) {
            writer.write(" hist");
        }
        writer.write("\"");
        
        writer.write(" title=\"").writeWithHtmlEscape(breadCrumb.getTitle()).write("\"");
        writer.write(">").writeWithHtmlEscape(breadCrumb.getTitle()).write("</span>");

        writer.write("<span class=\"hdsubtitle");
        if (history) {
            writer.write(" hist");
        }
        writer.write("\"");
        
        writer.write(" title=\"").writeWithHtmlEscape(breadCrumb.getSubTitle()).write("\"");
        writer.write(">").writeWithHtmlEscape(breadCrumb.getSubTitle()).write("</span>");

        writer.write("</div>");
        if (history) {
            writer.write("<i class=\"chev\"></i>");
        }
        writer.write("</li>");
    }
}
