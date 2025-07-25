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
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.EntitySearchInputDef;
import com.flowcentraltech.flowcentral.application.data.LabelSuggestionDef;
import com.flowcentraltech.flowcentral.application.data.SearchInputDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.common.business.SearchInputRestrictionGenerator;
import com.flowcentraltech.flowcentral.common.data.Entries;
import com.flowcentraltech.flowcentral.configuration.constants.EntityFieldDataType;
import com.flowcentraltech.flowcentral.configuration.constants.SearchConditionType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.And;
import com.tcdng.unify.core.criterion.BeginsWith;
import com.tcdng.unify.core.criterion.Between;
import com.tcdng.unify.core.criterion.EndsWith;
import com.tcdng.unify.core.criterion.Equals;
import com.tcdng.unify.core.criterion.Greater;
import com.tcdng.unify.core.criterion.GreaterOrEqual;
import com.tcdng.unify.core.criterion.ILike;
import com.tcdng.unify.core.criterion.Less;
import com.tcdng.unify.core.criterion.LessOrEqual;
import com.tcdng.unify.core.criterion.Like;
import com.tcdng.unify.core.criterion.NotBeginWith;
import com.tcdng.unify.core.criterion.NotBetween;
import com.tcdng.unify.core.criterion.NotEndWith;
import com.tcdng.unify.core.criterion.NotEquals;
import com.tcdng.unify.core.criterion.NotLike;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.util.CalendarUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Search entries object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class SearchEntries {

    private final EntityDef entityDef;

    private final LabelSuggestionDef labelSuggestion;

    private final AppletUtilities au;

    private final String searchConfigName;

    private final int columns;

    private final boolean showConditions;

    private final String preferredEvent;

    private String restrictionResolverName;

    private List<SearchEntry> entryList;
    
    public SearchEntries(AppletUtilities au, EntityDef entityDef, LabelSuggestionDef labelSuggestion,
            String searchConfigName, String preferredEvent, int columns, boolean showConditions) {
        this.au = au;
        this.entityDef = entityDef;
        this.labelSuggestion = labelSuggestion;
        this.searchConfigName = searchConfigName;
        this.preferredEvent = preferredEvent;
        this.columns = columns;
        this.showConditions = showConditions;
    }

    public SearchEntries(AppletUtilities au, EntityDef entityDef, String searchConfigName, String preferredEvent,
            int columns, boolean showConditions) {
        this.au = au;
        this.entityDef = entityDef;
        this.labelSuggestion = null;
        this.searchConfigName = searchConfigName;
        this.preferredEvent = preferredEvent;
        this.columns = columns;
        this.showConditions = showConditions;
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public List<SearchEntry> getEntryList() {
        return entryList;
    }

    public SearchEntry getEntry(int index) {
        return entryList.get(index);
    }

    public void clear() {
        if (entryList != null) {
            for (SearchEntry searchEntry : entryList) {
                if (!searchEntry.isFixed()) {
                    searchEntry.getParamInput().setValue(null);
                }
            }
        }
    }

    public String getPreferredEvent() {
        return preferredEvent;
    }

    public boolean isEmpty() {
        return entryList == null || entryList.isEmpty();
    }

    public int size() {
        return entryList.size();
    }

    public int getColumns() {
        return columns;
    }

    public boolean isShowConditions() {
        return showConditions;
    }

    public Entries getEntries() throws UnifyException {
        if (entryList != null) {
            Map<String, Object> inputs = new HashMap<String, Object>();
            if (!StringUtils.isBlank(restrictionResolverName)) {
                for (SearchEntry searchEntry : entryList) {
                    Object val = searchEntry.getParamInput().getValue();
                    inputs.put(searchEntry.getFieldName(), val);
                }

                SearchEntriesRestrictionResolver resolver = au.getComponent(SearchEntriesRestrictionResolver.class,
                        restrictionResolverName);
                Restriction restriction = resolver.resolveRestriction(inputs);
                return new Entries(inputs, restriction);
            } else {
                And and = new And();
                for (SearchEntry searchEntry : entryList) {
                    Object val = searchEntry.getParamInput().getValue();
                    inputs.put(searchEntry.getFieldName(), val);
                    if (val != null) {
                        if (searchEntry.isFieldEntry()) {
                            final EntityFieldDef entityFieldDef = searchEntry.getEntityFieldDef();
                            final EntityFieldDataType dataType = entityFieldDef.isWithResolvedTypeFieldDef()
                                    ? entityFieldDef.getResolvedTypeFieldDef().getDataType()
                                    : entityFieldDef.getDataType();
                            final String fieldName = entityFieldDef.getFieldName();
                            switch (searchEntry.getConditionType()) {
                                case BEGINS_WITH:
                                    and.add(new BeginsWith(fieldName, val));
                                    break;
                                case ENDS_WITH:
                                    and.add(new EndsWith(fieldName, val));
                                    break;
                                case EQUALS:
                                    if (dataType.isTimestamp()) {
                                        Date lower = CalendarUtils.getMidnightDate((Date) val);
                                        Date upper = CalendarUtils.getLastSecondDate((Date) val);
                                        and.add(new Between(fieldName, lower, upper));
                                    } else {
                                        and.add(new Equals(fieldName, val));
                                    }
                                    break;
                                case GREATER_OR_EQUAL:
                                    if (dataType.isTimestamp()) {
                                        val = CalendarUtils.getMidnightDate((Date) val);
                                    }

                                    and.add(new GreaterOrEqual(fieldName, val));
                                    break;
                                case GREATER_THAN:
                                    if (dataType.isTimestamp()) {
                                        val = CalendarUtils.getMidnightDate((Date) val);
                                    }

                                    and.add(new Greater(fieldName, val));
                                    break;
                                case ILIKE:
                                    and.add(new ILike(fieldName, val));
                                    break;
                                case LESS_OR_EQUAL:
                                    if (dataType.isTimestamp()) {
                                        val = CalendarUtils.getLastSecondDate((Date) val);
                                    }

                                    and.add(new LessOrEqual(fieldName, val));
                                    break;
                                case LESS_THAN:
                                    if (dataType.isTimestamp()) {
                                        val = CalendarUtils.getLastSecondDate((Date) val);
                                    }

                                    and.add(new Less(fieldName, val));
                                    break;
                                case LIKE:
                                    and.add(new Like(fieldName, val));
                                    break;
                                case NOT_BEGIN_WITH:
                                    and.add(new NotBeginWith(fieldName, val));
                                    break;
                                case NOT_END_WITH:
                                    and.add(new NotEndWith(fieldName, val));
                                    break;
                                case NOT_EQUALS:
                                    if (dataType.isTimestamp()) {
                                        Date lower = CalendarUtils.getMidnightDate((Date) val);
                                        Date upper = CalendarUtils.getLastSecondDate((Date) val);
                                        and.add(new NotBetween(fieldName, lower, upper));
                                    } else {
                                        and.add(new NotEquals(fieldName, val));
                                    }
                                    break;
                                case NOT_LIKE:
                                    and.add(new NotLike(fieldName, val));
                                    break;
                                default:
                                    break;
                            }
                        } else if (searchEntry.isGeneratorEntry()) {
                            SearchInputRestrictionGenerator generator = au
                                    .getComponent(SearchInputRestrictionGenerator.class, searchEntry.getGenerator());
                            Restriction restriction = generator.generate(val);
                            if (restriction != null) {
                                and.add(restriction);
                            }
                        }
                    }
                }

                return new Entries(inputs, !and.isEmpty() ? and : null);
            }
        }

        return new Entries();
    }

    public void normalize() throws UnifyException {
        if (entryList == null) {
            entryList = new ArrayList<SearchEntry>();
            if (!StringUtils.isBlank(searchConfigName)) {
                EntitySearchInputDef entitySearchInputDef = entityDef.getEntitySearchInputDef(searchConfigName);
                restrictionResolverName = entitySearchInputDef.getRestrictionResolverName();
                for (SearchInputDef searchInputDef : entitySearchInputDef.getSearchInputsDef()
                        .getSearchInputDefList()) {
                    final String label = au.resolveSessionMessage(searchInputDef.getLabel());
                    final SearchEntry searchEntry = searchInputDef.getFieldName().startsWith("s:")
                            ? new SearchEntry(au, entityDef, label, searchInputDef.getFieldName().substring(2),
                                    SearchConditionType.SESSION_ATTRIBUTE, searchInputDef.getDefVal(), searchInputDef.isFixed(), preferredEvent)
                            : (searchInputDef.getFieldName().startsWith("f:")
                                    ? new SearchEntry(au, entityDef, label, searchInputDef.getFieldName().substring(2),
                                            searchInputDef.getType(), searchInputDef.getDefVal(), searchInputDef.isFixed(), preferredEvent)
                                    : new SearchEntry(au, entityDef, label,
                                            searchInputDef.getFieldName().substring(2), searchInputDef.getDefVal(), searchInputDef.isFixed(), preferredEvent));
                    WidgetTypeDef widgetTypeDef = au.getWidgetTypeDef(searchInputDef.getWidget());
                    searchEntry.normalize(widgetTypeDef);
                    entryList.add(searchEntry);
                }
            } else {
                for (EntityFieldDef entityFieldDef : entityDef.getBasicSearchFieldDefList()) {
                    String label = getLabelSuggestion(entityFieldDef.getFieldName());
                    label = label != null ? label
                            : (entityFieldDef.isWithInputLabel() ? entityFieldDef.getInputLabel()
                                    : entityFieldDef.getFieldLabel());
                    EntityFieldDataType dataType = entityFieldDef.isWithResolvedTypeFieldDef()
                            ? entityFieldDef.getResolvedTypeFieldDef().getDataType()
                            : entityFieldDef.getDataType();
                    SearchConditionType conditionType = EntityFieldDataType.STRING.equals(dataType)
                            ? SearchConditionType.ILIKE
                            : SearchConditionType.EQUALS;
                    SearchEntry searchEntry = new SearchEntry(au, entityDef, label, entityFieldDef.getFieldName(),
                            conditionType, null, false, preferredEvent);
                    searchEntry.normalize();
                    entryList.add(searchEntry);
                }
            }
        }
    }

    private String getLabelSuggestion(String fieldName) {
        return labelSuggestion != null ? labelSuggestion.getSuggestedLabel(fieldName) : null;
    }
}
