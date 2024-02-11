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

package com.flowcentraltech.flowcentral.system.web.writers;

import com.flowcentraltech.flowcentral.system.web.widgets.FileAttachmentImageWidget;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * File attachment image writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(FileAttachmentImageWidget.class)
@Component("fileattachmentimage-writer")
public class FileAttachmentImageWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        FileAttachmentImageWidget fileAttachmentImageWidget = (FileAttachmentImageWidget) widget;
        writer.writeStructureAndContent(fileAttachmentImageWidget.getImageControl());
    }

}
