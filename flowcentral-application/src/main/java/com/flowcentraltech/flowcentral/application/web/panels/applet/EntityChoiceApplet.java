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

package com.flowcentraltech.flowcentral.application.web.panels.applet;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.web.panels.EntityChoice;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.web.ui.widget.Page;
import com.tcdng.unify.web.ui.widget.data.BadgeInfo;

/**
 * Entity choice applet.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityChoiceApplet extends AbstractApplet {

    private final String entityChoiceTitle;

    private EntityChoice entityChoice;

    public EntityChoiceApplet(Page page, AppletUtilities au, String appletName, BadgeInfo choiceBadgeInfo)
            throws UnifyException {
        super(page, au, appletName);
        entityChoiceTitle = getRootAppletDef().getLabel();
        setAltSubCaption(entityChoiceTitle);

        entityChoice = au.constructEntityChoice(appletName, EntityChoice.ENABLE_ALL);
        entityChoice.setChoiceConfig(choiceBadgeInfo);
    }

    public String getEntityChoiceTitle() {
        return entityChoiceTitle;
    }

    public EntityChoice getEntityChoice() {
        return entityChoice;
    }

    public void setBaseRestriction(Restriction baseRestriction) throws UnifyException {
        entityChoice.setBaseRestriction(baseRestriction, null);
    }
    
    public void applyFilterToSearch() throws UnifyException {
        entityChoice.applyFilterToSearch();
    }
}
