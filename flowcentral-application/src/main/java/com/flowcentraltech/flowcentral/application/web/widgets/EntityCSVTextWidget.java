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

import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.AbstractControl;

/**
 * Entity comma separated values text widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-entitycsvtext")
@UplAttributes({
    @UplAttribute(name = "key", type = String.class, defaultVal = "id"),
    @UplAttribute(name = "property", type = String.class, defaultVal = "description"),
    @UplAttribute(name = "entity", type = String.class, mandatory = true) })
public class EntityCSVTextWidget extends AbstractControl {

    @Configurable
    private AppletUtilities au;

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    @SuppressWarnings("unchecked")
    public final String getCommaSeparatedText() throws UnifyException {
        Object val = getValue();
        if (val != null) {
            EntityClassDef entityClassDef = au.getEntityClassDef(getUplAttribute(String.class, "entity"));
            Query<? extends Entity> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass())
                    .addEquals(getUplAttribute(String.class, "key"), val);
            addMoreCriteria(query);
            List<String> descriptions = au.environment().valueList(String.class,
                    getUplAttribute(String.class, "property"), query);
            return StringUtils.buildSpacedCommaSeparatedString(descriptions);
        }

        return null;
    }
    
    protected void addMoreCriteria(Query<? extends Entity> query) throws UnifyException {
        
    }
}
