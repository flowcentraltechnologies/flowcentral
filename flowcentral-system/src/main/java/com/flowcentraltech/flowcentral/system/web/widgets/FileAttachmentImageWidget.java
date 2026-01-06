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

package com.flowcentraltech.flowcentral.system.web.widgets;

import com.flowcentraltech.flowcentral.common.constants.FileAttachmentCategoryType;
import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralMultiControl;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.web.ui.widget.Control;

/**
 * File attachment image.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-fileattachmentimage")
@UplAttributes({ @UplAttribute(name = "category", type = String.class, mandatory = true),
        @UplAttribute(name = "entityName", type = String.class, mandatory = true),
        @UplAttribute(name = "attachmentName", type = String.class, mandatory = true) })
public class FileAttachmentImageWidget extends AbstractFlowCentralMultiControl {

    @Configurable
    private FileAttachmentImageGenerator imageGenerator;

    private Control imageControl;

    public Control getImageControl() {
        return imageControl;
    }

    public FileAttachmentImageGenerator getImageGenerator() throws UnifyException {
        Long entityInstId = getValue(Long.class);
        imageGenerator.prepareGenerationFor(entityInstId);
        return imageGenerator;
    }

    @Override
    public boolean isSupportStretch() {
        return false;
    }

    @Override
    protected void doOnPageConstruct() throws UnifyException {
        imageGenerator.init(FileAttachmentCategoryType.fromCode(getUplAttribute(String.class, "category")),
                getUplAttribute(String.class, "entityName"), getUplAttribute(String.class, "attachmentName"));
        StringBuilder sb = new StringBuilder();
        sb.append("!ui-image src:$t{images/blank.png} binding:imageGenerator");
        appendUplAttribute(sb, "styleClass");
        appendUplAttribute(sb, "style");
        imageControl = (Control) addInternalChildWidget(sb.toString());
    }

}
