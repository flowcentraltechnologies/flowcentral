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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.util.ApplicationNameUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.WriteWork;

/**
 * Entity list widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entitylist")
@UplAttributes({ @UplAttribute(name = "styleClass", type = String.class, defaultVal = "$e{ui-select}"),
        @UplAttribute(name = "limit", type = int.class, defaultVal = "100"),
        @UplAttribute(name = "blankOption", type = String.class),
        @UplAttribute(name = "direct", type = boolean.class, defaultVal = "false") })
public class EntityListWidget extends AbstractEntityListWidget {

    @Override
    @SuppressWarnings("unchecked")
    public WriteWork getWriteWork() throws UnifyException {
        WriteWork work = super.getWriteWork();
        if (work.get(WORK_RESULTLIST) == null) {
            RefDef refDef = null;
            EntityClassDef entityClassDef = null;
            Restriction br = null;
            if (isDirect()) {
                entityClassDef = application().getEntityClassDef(getRef(0));
            } else {
                refDef = getRefDef(0);
                entityClassDef = application().getEntityClassDef(refDef.getEntity());
                br = refDef.isWithFilter() ? refDef.getFilter().getRestriction(entityClassDef.getEntityDef(),
                        getValueStore().getReader(), application().getNow()) : null;
            }

            Query<? extends Entity> query = null;
            if (br != null) {
                query = Query.ofDefaultingToAnd((Class<? extends Entity>) entityClassDef.getEntityClass(), br);
            } else {
                query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
                query.ignoreEmptyCriteria(true);
            }

            addMoreResultRestriction(entityClassDef, query);
            int limit = getUplAttribute(int.class, "limit");
            if (limit > 0) {
                query.setLimit(limit);
            }

            String searchField = getSearchField(entityClassDef, refDef);
            if (searchField != null) {
                query.addOrder(searchField);
            }

            List<? extends Listable> listableList = environment().listAll(query);

            final String listKey = getListkey();
            final int len = listableList.size();
            final boolean isListFormat = refDef != null && refDef.isWithListFormat();
            ParameterizedStringGenerator generator = null;
            if (isListFormat) {
                generator = specialParamProvider().getStringGenerator(new BeanValueListStore(listableList).getReader(),
                        getValueStore().getReader(), refDef.getListFormat());
            }

            boolean isListKey = !StringUtils.isBlank(listKey);
            boolean isLongName = isListKey && "longName".equals(listKey);
            Formatter<Object> formatter = getFormatter();
            final String[] selIdArr = new String[len];
            final String[] lblArr = new String[len];
            final String[] keyArr = new String[len];
            for (int i = 0; i < len; i++) {
                Listable listable = listableList.get(i);
                String key = null;
                if (isListKey) {
                    if (isLongName) {
                        key = ApplicationNameUtils.getApplicationEntityLongName(
                                DataUtils.convert(String.class,
                                        ReflectUtils.getBeanProperty(listable, "applicationName")),
                                DataUtils.convert(String.class, ReflectUtils.getBeanProperty(listable, "name")));
                    } else {
                        key = DataUtils.convert(String.class, ReflectUtils.getBeanProperty(listable, listKey));
                    }

                } else {
                    key = listable.getListKey();
                }

                String nindex = getNamingIndexedId(i);
                selIdArr[i] = nindex;
                keyArr[i] = key;

                if (isListFormat) {
                    lblArr[i] = generator.setDataIndex(i).generate();
                } else {
                    if (formatter != null) {
                        lblArr[i] = formatter.format(listable.getListDescription());
                    } else {
                        lblArr[i] = listable.getListDescription();
                    }
                }
            }

            work.set(WORK_RESULTLIST, listableList);
            work.set(WORK_SELECTIDS, selIdArr);
            work.set(WORK_KEYS, keyArr);
            work.set(WORK_LABELS, lblArr);
        }

        return work;
    }

    public String getBlankOption() throws UnifyException {
        return getUplAttribute(String.class, "blankOption");
    }

    public String getBlankOptionId() throws UnifyException {
        return getPrefixedId("blnk_");
    }

    public String getFramePanelId() throws UnifyException {
        return getPrefixedId("frm_");
    }

    public String getListPanelId() throws UnifyException {
        return getPrefixedId("lst_");
    }

    @Override
    protected void addMoreResultRestriction(Query<? extends Entity> query) throws UnifyException {

    }
}
