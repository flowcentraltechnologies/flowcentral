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
package com.flowcentraltech.flowcentral.application.listing;

import com.flowcentraltech.flowcentral.application.data.ListingProperties;
import com.flowcentraltech.flowcentral.application.web.widgets.FormListing;
import com.flowcentraltech.flowcentral.application.web.widgets.FormListingWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractContainerWriter;

/**
 * Form listing writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Writes(FormListingWidget.class)
@Component("fc-formlisting-writer")
public class FormListingWriter extends AbstractContainerWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        FormListingWidget formListingWidget = (FormListingWidget) widget;
        writer.write("<div");
        writeTagAttributes(writer, formListingWidget);
        writer.write(">");

        FormListing formListing = formListingWidget.getFormListing();
        FormListingGenerator generator = (FormListingGenerator) getComponent(formListing.getListingGenerator());
        ValueStoreReader reader = formListing.getFormValueStore().getReader();
        //reader.setReadFormats(generator.getFormats());
        generator.generateListing(reader, new ListingProperties(), writer);
        writer.write("</div>");
    }
}
