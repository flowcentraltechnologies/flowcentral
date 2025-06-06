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
package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.web.panels.TextTemplate;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Text template widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-texttemplate")
@UplAttributes({ @UplAttribute(name = "buttonSymbol", type = String.class, defaultVal = "text"),
        @UplAttribute(name = "entity", type = String.class),
        @UplAttribute(name = "entityBinding", type = String.class) })
public class TextTemplateWIdget extends AbstractPopupWindowTextField {

    @Override
    protected Popup preparePopup() throws UnifyException {
        final EntityDef entityDef = au().getEntityDef(getUplAttribute(String.class, "entity", "entityBinding"));
        final String template = getValue(String.class);
        TokenSequence tokenSequence = new TokenSequence(entityDef, template);
        return new Popup(ApplicationResultMappingConstants.SHOW_TEXT_TEMPLATE_EDITOR,
                new TextTemplate(tokenSequence, getValueStore(), getBinding()));
    }
}
