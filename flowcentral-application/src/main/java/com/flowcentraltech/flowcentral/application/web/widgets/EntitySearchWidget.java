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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.util.RefEncodingUtils;
import com.flowcentraltech.flowcentral.common.annotation.LongName;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.criterion.ILike;
import com.tcdng.unify.core.criterion.Like;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueListStore;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ExtensionType;

/**
 * Entity search widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-entitysearch")
@LongName("application.entitysearch")
@UplAttributes({ @UplAttribute(name = "limit", type = int.class, defaultVal = "32"),
        @UplAttribute(name = "queryLabel", type = String.class, defaultVal = "$m{search.filter}"),
        @UplAttribute(name = "buttonImgSrc", type = String.class, defaultVal = "$t{images/search.png}"),
        @UplAttribute(name = "buttonSymbol", type = String.class, defaultVal = "search"),
        @UplAttribute(name = "valueStoreMemory", type = boolean.class, defaultVal = "true"),
        @UplAttribute(name = "direct", type = boolean.class, defaultVal = "false"),
        @UplAttribute(name = "caseInsensitive", type = boolean.class, defaultVal = "true") })
public class EntitySearchWidget extends AbstractEntityListWidget {

    @Override
    public ExtensionType getExtensionType() {
        return ExtensionType.FACADE_HIDDEN_EDIT;
    }

    @Action
    public final void search() throws UnifyException {
        final String input = getRequestTarget(String.class);
        int triggerDataIndex = getRequestTriggerDataIndex();
        recallValueStore(triggerDataIndex);
        getWriteWork().set(WORK_RESULTLIST, doSearch(input, getUplAttribute(int.class, "limit")));
        commandRefreshSection(getResultPanelId());
    }

    public String getSearchPanelId() throws UnifyException {
        return getPrefixedId("sch_");
    }

    public String getResultPanelId() throws UnifyException {
        return getPrefixedId("rlt_");
    }

    public String getResultId() throws UnifyException {
        return getPrefixedId("rltd_");
    }

    protected List<? extends Listable> doSearch(String input, int limit) throws UnifyException {
        return getResultByRef(input, limit);
    }

    @SuppressWarnings("unchecked")
    protected final List<? extends Listable> getResultByRef(String input, int limit) throws UnifyException {
        final ApplicationModuleService applicationModuleService = application();
        RefDef[] refDefs = getRefDefs();
        if (refDefs != null) {
            List<Listable> fullResult = new ArrayList<Listable>();
            final boolean encode = refDefs.length > 1;
            final ValueStore _valueStore = getValueStore();
            final String listKey = getListkey();
            final boolean isListKey = !StringUtils.isBlank(listKey);
            for (int i = 0; i < refDefs.length; i++) {
                RefDef refDef = refDefs[i];
                final boolean listFormat = refDef.isWithListFormat();
                EntityClassDef entityClassDef = application().getEntityClassDef(refDef.getEntity());
                String searchField = getSearchField(entityClassDef, refDef);
                Restriction br = refDef.isWithFilter() ? refDef.getFilter().getRestriction(
                        entityClassDef.getEntityDef(), _valueStore.getReader(), application().getNow()) : null;

                Query<? extends Entity> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
                query.ignoreEmptyCriteria(true);
                if (br != null) {
                    query.addRestriction(br);
                }

                if (!StringUtils.isBlank(input)) {
                    Restriction like = getUplAttribute(boolean.class, "caseInsensitive")
                            ? new ILike(searchField, input.trim())
                            : new Like(searchField, input.trim());
                    query.addRestriction(like);
                }

                addMoreResultRestriction(entityClassDef, query);
                if (limit > 0) {
                    query.setLimit(limit);
                }

                if (refDef.isWithOrderField()) {
                    query.addOrder(refDef.getOrderField());
                    if (query.isSelect()) {
                        query.addSelect(refDef.getOrderField());
                    }
               } else {
                    query.addOrder(searchField);
                }
                
                List<? extends Listable> result = environment().listAll(query);
                if (encode || listFormat) {
                    ParameterizedStringGenerator generator = null;
                    if (listFormat) {
                        generator = specialParamProvider().getStringGenerator(
                                new BeanValueListStore(result).getReader(), _valueStore.getReader(),
                                refDef.getListFormat());
                    }
                    final int len = result.size();
                    for (int j = 0; j < len; j++) {
                        String formatDesc = listFormat ? generator.setDataIndex(j).generate()
                                : applicationModuleService.getEntityDescription(entityClassDef, (Entity) result.get(j),
                                        refDef.getSearchField());
                        // TODO Concatenate reference prefix to description
                        String key = encode
                                ? RefEncodingUtils.encodeRefValue(i, refDef.getLongName(), result.get(j).getListKey())
                                : (isListKey ? DataUtils.getBeanProperty(String.class, result.get(j), listKey)
                                        : result.get(j).getListKey());
                        fullResult.add(new ListData(key, formatDesc));
                    }
                } else {
                    fullResult.addAll(result);
                }
            }

            return fullResult;
        }

        return Collections.emptyList();
    }

    @Override
    protected void addMoreResultRestriction(Query<? extends Entity> query) throws UnifyException {

    }
}
