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
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationEntityNameParts;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.flowcentraltech.flowcentral.application.util.RefDecodedValue;
import com.flowcentraltech.flowcentral.application.util.RefEncodingUtils;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.tcdng.unify.core.UnifyException;
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
import com.tcdng.unify.web.constant.ExtensionType;
import com.tcdng.unify.web.ui.widget.control.AbstractPopupTextField;

/**
 * Convenient abstract base class for entity lists.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@UplAttributes({ @UplAttribute(name = "ref", type = String[].class, mandatory = true),
        @UplAttribute(name = "direct", type = boolean.class, defaultVal = "true"),
        @UplAttribute(name = "listKey", type = String.class) })
public abstract class AbstractEntityListWidget extends AbstractPopupTextField {

    public static final String WORK_RESULTLIST = "resultList";

    public static final String WORK_SELECTIDS = "labelIds";

    public static final String WORK_LABELS = "labels";

    public static final String WORK_KEYS = "keys";

    @Configurable
    private AppletUtilities au;

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    @Override
    public ExtensionType getExtensionType() {
        return ExtensionType.FACADE_HIDDEN;
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

    public final Listable getCurrentSelect() throws UnifyException {
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

    public String getListkey() throws UnifyException {
        return getUplAttribute(String.class, "listKey");
    }

    public String[] getRef() throws UnifyException {
        return getUplAttribute(String[].class, "ref");
    }

    public String getRef(int index) throws UnifyException {
        return getUplAttribute(String[].class, "ref")[index];
    }

    public boolean isDirect() throws UnifyException {
        return getUplAttribute(boolean.class, "direct");
    }

    protected Listable doCurrentSelect(Object keyVal) throws UnifyException {
        return doCurrentSelectByRef(keyVal);
    }

    protected String getSearchField(EntityClassDef entityClassDef, RefDef refDef) {
        if (refDef == null) {
            return entityClassDef.getEntityDef().isWithDescriptionField() ? "description" : null;
        }

        return refDef.isWithSearchField() ? refDef.getSearchField()
                : (entityClassDef.getEntityDef().isWithDescriptionField() ? "description" : null);
    }

    @SuppressWarnings("unchecked")
    protected Listable doCurrentSelectByRef(Object keyVal) throws UnifyException {
        logDebug("Decoding reference value [{0}]...", keyVal);
        RefDecodedValue decodedKey = RefEncodingUtils.decodeRefValue(keyVal);
        logDebug("Decoded reference value [{0}]...", decodedKey);
        String entityName = getRef(decodedKey.getIndex());
        if (StringUtils.isBlank(entityName)) {
            return null;
        }

        RefDef refDef = null;
        Restriction br = null;
        if (!isDirect()) {
            refDef = getRefDef(decodedKey.getIndex());
            entityName = refDef.getEntity();
            logDebug("Using listing reference value [{0}]...", refDef.getLongName());
            if (refDef.isWithFilterGenerator()) {
                br = refDef.getFilter().getRestriction(null, getValueStore().getReader(), null);
            }
        }

        final EntityClassDef entityClassDef = application().getEntityClassDef(entityName);
        Query<? extends Entity> query = null;
        if (br != null) {
            query = Query.ofDefaultingToAnd((Class<? extends Entity>) entityClassDef.getEntityClass(), br);
        } else {
            query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
        }

        String listKey = getListkey();
        if (StringUtils.isBlank(listKey)) {
            query.addEquals("id", decodedKey.getValue());
        } else {
            if ("longName".equals(listKey)) {
                String longName = (String) decodedKey.getValue();
                if (longName.indexOf('.') < 0) {
                    return null;
                }

                ApplicationEntityNameParts np = ApplicationNameUtils.getApplicationEntityNameParts(longName);
                query.addEquals("name", np.getEntityName());
                query.addEquals("applicationName", np.getApplicationName());
            } else {
                query.addEquals(listKey, decodedKey.getValue());
            }
        }

        Listable result = environment().listLean(query);
        if (result != null && refDef != null) {
            String formatDesc = refDef.isWithListFormat()
                    ? specialParamProvider().getStringGenerator(new BeanValueStore(result).getReader(),
                            getValueStore().getReader(), refDef.getListFormat()).generate()
                    : application().getEntityDescription(entityClassDef, (Entity) result, refDef.getSearchField());
            String key = decodedKey.isLongNameRef()
                    ? RefEncodingUtils.encodeRefValue(decodedKey.getIndex(), decodedKey.getRefLongName(),
                            result.getListKey())
                    : result.getListKey();
            return new ListData(key, formatDesc);
        }

        return result;
    }

    protected RefDef[] getRefDefs() throws UnifyException {
        String[] ref = getRef();
        if (ref != null) {
            RefDef[] refDefs = new RefDef[ref.length];
            for (int i = 0; i < ref.length; i++) {
                refDefs[i] = application().getRefDef(ref[i]);
            }

            return refDefs;
        }

        return null;
    }

    protected RefDef getRefDef(int index) throws UnifyException {
        String[] ref = getRef();
        if (ref != null && index >= 0 && index < ref.length) {
            return application().getRefDef(ref[index]);
        }

        return null;
    }

    protected AppletUtilities au() {
        return au;
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

    protected abstract void addMoreResultRestriction(Query<? extends Entity> query) throws UnifyException;

    protected final void addMoreResultRestriction(EntityClassDef entityClassDef, Query<? extends Entity> query)
            throws UnifyException {
        addMoreResultRestriction(query);
        if (entityClassDef.isWithTenantId() && isTenancyEnabled()) {
            applyOverrideTenantId(query, entityClassDef.getTenantIdDef().getFieldName());
        }
    }

}
