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

package com.flowcentraltech.flowcentral.application.data;

import java.math.BigDecimal;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.Widget;

/**
 * Entity field total summary.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityFieldTotalSummary {

    private EntityFieldDef entityFieldDef;

    private Widget renderer;
    
    private Object total;

    private boolean visible;
    
    public EntityFieldTotalSummary(EntityFieldDef entityFieldDef, Widget renderer, boolean visible)
            throws UnifyException {
        if (!entityFieldDef.isNumber()) {
            throw new IllegalArgumentException("Total summary can not be computed for field ["
                    + entityFieldDef.getFieldName() + "] of type [" + entityFieldDef.getDataType() + "]");
        }

        this.entityFieldDef = entityFieldDef;
        this.renderer = renderer;
        this.visible = visible;
        clear();
    }
    
    public Widget getRenderer() {
        return renderer;
    }

    public String getFieldName() {
        return entityFieldDef.getFieldName();
    }
    
    public Object getTotal() {
        return total;
    }

    public void replaceTotalWith(Number total) throws UnifyException {
        this.total = DataUtils.convert(entityFieldDef.getDataType().dataType().javaClass(), total);
    }

    public void replaceTotalWith(EntityFieldTotalSummary summary) throws UnifyException {
        this.total = DataUtils.convert(entityFieldDef.getDataType().dataType().javaClass(), summary.total);
    }
    
    public boolean isVisible() {
        return visible;
    }

    public void clear() throws UnifyException {
        this.total = DataUtils.convert(entityFieldDef.getDataType().dataType().javaClass(), 0);
    }
    
    public void add(Object val) throws UnifyException {
        if (val != null) {
            switch(entityFieldDef.getDataType()) {
                case DECIMAL:
                    total = ((BigDecimal) total).add(DataUtils.convert(BigDecimal.class, val));
                    break;
                case DOUBLE:
                    total = ((Double) total) + DataUtils.convert(Double.class, val);
                    break;
                case FLOAT:
                    total = ((Float) total) + DataUtils.convert(Float.class, val);
                    break;
                case INTEGER:
                    total = ((Integer) total) + DataUtils.convert(Integer.class, val);
                    break;
                case LONG:
                    total = ((Long) total) + DataUtils.convert(Long.class, val);
                    break;
                case SHORT:
                    total = ((Short) total) + DataUtils.convert(Short.class, val);
                    break;
                default:
                    break;                
            }
        }
    }
}
