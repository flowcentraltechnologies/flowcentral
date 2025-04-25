package com.flowcentraltech.flowcentral.application.data;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Filter group definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class FilterGroupDef {

    public enum FilterType {
        BASE,
        TAB,
        MAINTAIN_UPDATE,
        MAINTAIN_DELETE
    }

    private EntityDef entityDef;

    private Map<FilterType, AppletFilterDef> filterDefs;

    private FilterGroupDef(EntityDef entityDef, Map<FilterType, AppletFilterDef> filterDefs) {
        this.entityDef = entityDef;
        this.filterDefs = filterDefs;
    }

    public boolean matchObject(FilterType type, Object bean, Date now) throws UnifyException {
        return matchReader(type, bean != null ? new BeanValueStore(bean).getReader() : null, now);
    }

    public boolean matchReader(FilterType type, ValueStoreReader reader, Date now) throws UnifyException {
        AppletFilterDef _filterDef = filterDefs.get(type);
        return _filterDef != null
                ? _filterDef.getFilterDef()
                        .getObjectFilter(entityDef, reader != null ? reader : null, now)
                        .matchReader(reader)
                : true;
    }

    public Restriction getRestriction(FilterType type, ValueStoreReader reader, Date now) throws UnifyException {
        AppletFilterDef _filterDef = filterDefs.get(type);
        return _filterDef != null ? _filterDef.getFilterDef().getRestriction(entityDef,
                reader != null ? reader : null, now) : null;
    }

    public boolean isWithFilterType(FilterType type) {
        return filterDefs.containsKey(type);
    }

    public static Builder newBuilder(EntityDef entityDef) {
        return new Builder(entityDef);
    }

    public static class Builder {

        private EntityDef entityDef;

        private Map<FilterType, AppletFilterDef> filterDefs;

        public Builder(EntityDef entityDef) {
            this.entityDef = entityDef;
            this.filterDefs = new HashMap<FilterType, AppletFilterDef>();
        }

        public Builder addFilter(FilterType type, AppletFilterDef filterDef) throws UnifyException {
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
