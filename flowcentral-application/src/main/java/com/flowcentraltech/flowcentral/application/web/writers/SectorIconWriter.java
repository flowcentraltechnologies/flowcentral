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

package com.flowcentraltech.flowcentral.application.web.writers;

import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIconWidget;
import com.flowcentraltech.flowcentral.system.constants.SystemColorType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Writes;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.ResponseWriter;
import com.tcdng.unify.web.ui.widget.Widget;
import com.tcdng.unify.web.ui.widget.writer.AbstractControlWriter;

/**
 * Sector icon writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Writes(SectorIconWidget.class)
@Component("sectoricon-writer")
public class SectorIconWriter extends AbstractControlWriter {

    @Override
    protected void doWriteStructureAndContent(ResponseWriter writer, Widget widget) throws UnifyException {
        SectorIconWidget sectorIconWidget = (SectorIconWidget) widget;
        SectorIcon sectorIcon = sectorIconWidget.icon();
        String color = sectorIcon != null && sectorIcon.getColor() != null ? sectorIcon.getColor()
                : SystemColorType.GRAY.code();
        String shortCode = sectorIcon != null ? sectorIcon.getShortCode() : null;
        writer.write("<span");
        writeTagVisualAttributesWithTrailingExtraStyle(writer, sectorIconWidget, "background-color:" + color + ";");
        writer.write(">");
        if (!StringUtils.isBlank(shortCode)) {
            writer.writeWithHtmlEscape(shortCode);
        } else {
            writer.writeHtmlFixedSpace();
        }
        writer.write("</span>");
    }
}
