package com.flowcentraltech.flowcentral.application.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Filter group definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FilterGroupDef {

    public enum FilterType {
        BASE,
        TAB,
        MAINTAIN_UPDATE,
        MAINTAIN_DELETE
    }

    private EntityDef entityDef;
    
    private Map<FilterType, FilterDef> filterDefs;

    private FilterGroupDef(EntityDef entityDef, Map<FilterType, FilterDef> filterDefs) {
        this.entityDef = entityDef;
        this.filterDefs = filterDefs;
    }

    public boolean match(FilterType type, Object bean, Date now) throws UnifyException {
        FilterDef _filterDef = filterDefs.get(type);
        return _filterDef != null ? _filterDef.getObjectFilter(entityDef, now).match(bean) : true;
    }

    public boolean match(FilterType type, ValueStore beanValueStore, Date now) throws UnifyException {
        FilterDef _filterDef = filterDefs.get(type);
        return _filterDef != null ? _filterDef.getObjectFilter(entityDef, now).match(beanValueStore) : true;
    }

    public Restriction getRestriction(FilterType type, Date now) throws UnifyException {
        FilterDef _filterDef = filterDefs.get(type);
        return _filterDef != null ? _filterDef.getRestriction(entityDef, now) : null;
    }

    public boolean isWithFilterType(FilterType type) {
        return filterDefs.containsKey(type);
    }

    public static Builder newBuilder(EntityDef entityDef) {
        return new Builder(entityDef);
    }

    public static class Builder {

        private EntityDef entityDef;

        private Map<FilterType, FilterDef> filterDefs;

        public Builder(EntityDef entityDef) {
            this.entityDef = entityDef;
            this.filterDefs = new HashMap<FilterType, FilterDef>();
        }

        public Builder addFilter(FilterType type, FilterDef filterDef) throws UnifyException {
            if (filterDefs.containsKey(type)) {
                throw new IllegalArgumentException("Filter of type [" + type + "] already added to this group.");
            }

            filterDefs.put(type, filterDef);
            return this;
        }

        public FilterGroupDef build() throws UnifyException {
            return new FilterGroupDef(entityDef, filterDefs);
        }
    }
}
