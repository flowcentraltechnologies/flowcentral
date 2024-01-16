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

package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.data.TableDef;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySelect;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralPopupTextField;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ExtensionType;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Entity select widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entityselect")
@UplAttributes({ @UplAttribute(name = "limit", type = int.class, defaultVal = "20"),
        @UplAttribute(name = "ref", type = String.class, mandatory = true),
        @UplAttribute(name = "buttonImgSrc", type = String.class, defaultVal = "$t{images/search.png}"),
        @UplAttribute(name = "buttonSymbol", type = String.class, defaultVal = "search"),
        @UplAttribute(name = "listKey", type = String.class), @UplAttribute(name = "fieldA", type = String.class),
        @UplAttribute(name = "fieldB", type = String.class),
        @UplAttribute(name = "space", type = boolean.class, defaultVal = "false"),
        @UplAttribute(name = "special", type = boolean.class, defaultVal = "false") })
public class EntitySelectWidget extends AbstractFlowCentralPopupTextField {

    @Configurable
    private AppletUtilities au;

    @Action
    public final void search() throws UnifyException {
        String input = getRequestTarget(String.class);
        RefDef refDef = getRefDef();
        TableDef tableDef = application().getTableDef(refDef.getSearchTable());
        int limit = getUplAttribute(int.class, "limit");
        String fieldA = getUplAttribute(String.class, "fieldA");
        String fieldB = getUplAttribute(String.class, "fieldB");
        EntitySelect entitySelect = new EntitySelect(au, tableDef, refDef.getSearchField(), fieldA, fieldB,
                getValueStore(), refDef.getSelectHandler(), limit);
        entitySelect.setEnableFilter(true);
        String label = tableDef.getEntityDef().getFieldDef(refDef.getSearchField()).getFieldLabel() + ":";
        String labelA = !StringUtils.isBlank(fieldA) ? tableDef.getEntityDef().getFieldDef(fieldA).getFieldLabel() + ":"
                : null;
        String labelB = !StringUtils.isBlank(fieldB) ? tableDef.getEntityDef().getFieldDef(fieldB).getFieldLabel() + ":"
                : null;
        entitySelect.setLabel(label);
        entitySelect.setLabelA(labelA);
        entitySelect.setLabelB(labelB);
        if (input != null && !input.trim().isEmpty()) {
            entitySelect.setFilter(input);
        }

        entitySelect.applyFilterToSearch();
        String title = resolveSessionMessage("$m{entityselect.select.entity}",
                entitySelect.getEntityTable().getEntityDef().getLabel());
        entitySelect.setTitle(title);
        entitySelect.setSpace(isSpace());
        entitySelect.setSpecial(isSpecial());
        commandShowPopup(new Popup(ApplicationResultMappingConstants.SHOW_ENTITY_SELECT, entitySelect));
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
        return false;
    }

    public boolean isSpace() throws UnifyException {
        return getUplAttribute(boolean.class, "space");
    }

    public boolean isSpecial() throws UnifyException {
        return getUplAttribute(boolean.class, "special");
    }

    public String getRef() throws UnifyException {
        return getUplAttribute(String.class, "ref");
    }

    public String getListkey() throws UnifyException {
        return getUplAttribute(String.class, "listKey");
    }

    public String getFormPanelId() throws UnifyException {
        return getPageAttribute(String.class, "formPanel.id");
    }

    public Listable getCurrentSelect() throws UnifyException {
        Object keyVal = getValue(Object.class);
        if (keyVal != null) {
            Listable select = doCurrentSelect(keyVal);
            if (select != null) {
                return select;
            }

            setValue(null);
        }

        return null;
    }

    @SuppressWarnings("unchecked")
    private Listable doCurrentSelect(Object keyVal) throws UnifyException {
        logDebug("Decoding reference value [{0}]...", keyVal);
        RefDef refDef = getRefDef();
        final EntityClassDef entityClassDef = application().getEntityClassDef(refDef.getEntity());
        Query<? extends Entity> query = null;
        Restriction br = refDef.isWithFilter() ? refDef.getFilter().getRestriction(entityClassDef.getEntityDef(),
                getValueStore().getReader(), application().getNow()) : null;
        if (br != null) {
            query = Query.ofDefaultingToAnd((Class<? extends Entity>) entityClassDef.getEntityClass(), br);
        } else {
            query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
        }

        String listKey = getListkey();
        if (StringUtils.isBlank(listKey)) {
            query.addEquals("id", keyVal);
        } else {
            query.addEquals(listKey, keyVal);
        }

        Listable result = environment().listLean(query);
        if (result != null) {
            String formatDesc = refDef.isWithListFormat()
                    ? specialParamProvider().getStringGenerator(new BeanValueStore(result).getReader(),
                            getValueStore().getReader(), refDef.getListFormat()).generate()
                    : application().getEntityDescription(entityClassDef, (Entity) result, refDef.getSearchField());
            return new ListData(result.getListKey(), formatDesc);
        }

        return result;
    }

    private RefDef getRefDef() throws UnifyException {
        return application().getRefDef(getRef());
    }

    protected ApplicationModuleService application() {
        return au.application();
    }

    protected EnvironmentService environment() {
        return au.environment();
    }

    protected SpecialParamProvider specialParamProvider() {
        return au.specialParamProvider();
    }
}
