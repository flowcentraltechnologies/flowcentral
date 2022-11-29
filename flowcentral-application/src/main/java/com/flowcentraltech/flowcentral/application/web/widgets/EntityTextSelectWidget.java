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

package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySelect;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.flowcentraltech.flowcentral.configuration.constants.TextType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ExtensionType;
import com.tcdng.unify.web.ui.widget.control.AbstractPopupTextField;

/**
 * Entity text select widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entitytextselect")
@UplAttributes({ @UplAttribute(name = "limit", type = int.class, defaultVal = "20"),
        @UplAttribute(name = "ref", type = String.class, mandatory = true),
        @UplAttribute(name = "buttonImgSrc", type = String.class, defaultVal = "$t{images/search.png}"),
        @UplAttribute(name = "buttonSymbol", type = String.class, defaultVal = "table-list"),
        @UplAttribute(name = "selectOnly", type = boolean.class, defaultVal = "false"),
        @UplAttribute(name = "selectOnlyBinding", type = String.class),
        @UplAttribute(name = "filterBinding", type = String.class),
        @UplAttribute(name = "listKey", type = String.class),
        @UplAttribute(name = "space", type = boolean.class, defaultVal = "false"),
        @UplAttribute(name = "special", type = boolean.class, defaultVal = "false"),
        @UplAttribute(name = "acceptPlus", type = boolean.class),
        @UplAttribute(name = "acceptMinus", type = boolean.class),
        @UplAttribute(name = "fieldA", type = String.class),
        @UplAttribute(name = "fieldB", type = String.class),
        @UplAttribute(name = "text", type = TextType.class, defaultVal = "text") })
public class EntityTextSelectWidget extends AbstractPopupTextField {

    @Configurable
    private AppletUtilities appletUtilities;

    public void setAppletUtilities(AppletUtilities appletUtilities) {
        this.appletUtilities = appletUtilities;
    }

    @Action
    public final void search() throws UnifyException {
        RefDef refDef = getRefDef();
        int limit = getUplAttribute(int.class, "limit");
        String defaultFilter = getDefaultFilter();
        String filter = !StringUtils.isBlank(defaultFilter) ? defaultFilter : getRequestTarget(String.class);
        String fieldA = getUplAttribute(String.class, "fieldA");
        String fieldB = getUplAttribute(String.class, "fieldB");
        EntitySelect entitySelect = appletUtilities.constructEntitySelect(refDef, getValueStore(), fieldA, fieldB,
                filter, limit);
        entitySelect.setEnableFilter(true);
        entitySelect.applyFilterToSearch();
        String title = resolveSessionMessage("$m{entityselect.select.entity}",
                entitySelect.getEntityTable().getEntityDef().getLabel());
        entitySelect.setTitle(title);
        entitySelect.setSpace(isSpace());
        entitySelect.setSpecial(isSpecial());
        setSessionAttribute(FlowCentralSessionAttributeConstants.ENTITYSELECT, entitySelect);
        setCommandResultMapping(ApplicationResultMappingConstants.SHOW_ENTITY_SELECT);
    }

    @Override
    public ExtensionType getExtensionType() {
        return ExtensionType.FACADE_HIDDEN_EDIT;
    }

    @Override
    public boolean isPopupOnEditableOnly() {
        return true;
    }

    @Override
    public boolean isUseFacade() throws UnifyException {
        return true;
    }

    @Override
    public boolean isBindEventsToFacade() throws UnifyException {
        return true;
    }

    public boolean isSpace() throws UnifyException {
        return getUplAttribute(boolean.class, "space");
    }

    public String getFormPanelId() throws UnifyException {
        return getPageAttribute(String.class, "formPanel.id");
    }

    public boolean isSpecial() throws UnifyException {
        return getUplAttribute(boolean.class, "special");
    }

    public boolean isAcceptPlus() throws UnifyException {
        return getUplAttribute(boolean.class, "acceptPlus");
    }

    public boolean isAcceptMinus() throws UnifyException {
        return getUplAttribute(boolean.class, "acceptMinus");
    }

    public String getRef() throws UnifyException {
        return getUplAttribute(String.class, "ref");
    }

    public String getListkey() throws UnifyException {
        return getUplAttribute(String.class, "listKey");
    }

    public boolean selectOnly() throws UnifyException {
        String selectOnlyBinding = getUplAttribute(String.class, "selectOnlyBinding");
        return !StringUtils.isBlank(selectOnlyBinding) ? getValue(boolean.class, selectOnlyBinding)
                : getUplAttribute(boolean.class, "selectOnly");
    }

    public TextType type() throws UnifyException {
        return getUplAttribute(TextType.class, "text");
    }

    public Listable getCurrentSelect() throws UnifyException {
        Object keyVal = getValue(Object.class);
        if (keyVal != null) {
            String val = String.valueOf(keyVal);
            return new ListData(val, val);
        }

        return null;
    }

    private String getDefaultFilter() throws UnifyException {
        String filterBinding = getUplAttribute(String.class, "filterBinding");
        return !StringUtils.isBlank(filterBinding) ? getValue(String.class, filterBinding) : null;
    }

    private RefDef getRefDef() throws UnifyException {
        return appletUtilities.application().getRefDef(getRef());
    }
}
