/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.ArrayList;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.data.PropertyRuleDef;
import com.flowcentraltech.flowcentral.application.data.TabSheetDef;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniFormScope;
import com.flowcentraltech.flowcentral.application.web.widgets.SectorIcon;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.configuration.constants.RendererType;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.MapValues;

/**
 * Edit property list object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EditPropertyList {

    private final AppletContext ctx;

    private final SweepingCommitPolicy sweepingCommitPolicy;

    private final PropertyRuleDef propertyRuleDef;

    private final Entity inst;

    private final SectorIcon sectorIcon;

    private final BreadCrumbs breadCrumbs;

    private MapValues propValues;

    private String childFkFieldName;

    private HeadlessTabsForm headlessForm;

    private String displayItemCounter;

    private String displayItemCounterClass;

    public EditPropertyList(AppletContext ctx, SweepingCommitPolicy sweepingCommitPolicy,
            PropertyRuleDef propertyRuleDef, Entity inst, SectorIcon sectorIcon, BreadCrumbs breadCrumbs,
            String childFkFieldName) {
        this.ctx = ctx;
        this.sweepingCommitPolicy = sweepingCommitPolicy;
        this.propertyRuleDef = propertyRuleDef;
        this.inst = inst;
        this.sectorIcon = sectorIcon;
        this.breadCrumbs = breadCrumbs;
        this.childFkFieldName = childFkFieldName;
    }

    public String getMainTitle() {
        return breadCrumbs.getLastBreadCrumb().getTitle();
    }

    public String getSubTitle() {
        return breadCrumbs.getLastBreadCrumb().getSubTitle();
    }

    public SectorIcon getSectorIcon() {
        return sectorIcon;
    }

    public BreadCrumbs getBreadCrumbs() {
        return breadCrumbs;
    }

    public AppletUtilities getAu() {
        return ctx.au();
    }

    public AppletContext getCtx() {
        return ctx;
    }

    public PropertyRuleDef getPropertyRuleDef() {
        return propertyRuleDef;
    }

    public EntityDef getEntityDef() {
        return propertyRuleDef.getEntityDef();
    }

    public HeadlessTabsForm getHeadlessForm() {
        return headlessForm;
    }

    public void setHeadlessForm(HeadlessTabsForm headlessForm) {
        this.headlessForm = headlessForm;
    }

    public String getDisplayItemCounter() {
        return displayItemCounter;
    }

    public void setDisplayItemCounter(String displayItemCounter) {
        this.displayItemCounter = displayItemCounter;
    }

    public String getDisplayItemCounterClass() {
        return displayItemCounterClass;
    }

    public void setDisplayItemCounterClass(String displayItemCounterClass) {
        this.displayItemCounterClass = displayItemCounterClass;
    }

    public void clearDisplayItem() {
        displayItemCounter = null;
        displayItemCounterClass = null;
    }

    public boolean isWithSectorIcon() {
        return sectorIcon != null;
    }

    public void loadPropertyList(FormContext ctx) throws UnifyException {
        propValues = ctx.au().getPropertyListValues(inst, childFkFieldName, propertyRuleDef);

        FormContext formContext = new FormContext(ctx.getAppletContext(), ctx.getFormDef(), ctx.getFormEventHandlers(),
                propValues);
        TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(null, 1L);
        List<TabSheetItem> tabSheetItemList = new ArrayList<TabSheetItem>();
        final List<FormTabDef> formTabDefs = propertyRuleDef.getPropertyListDef(inst).getFormTabDefs();
        final int len = formTabDefs.size();
        for (int i = 0; i < len; i++) {
            final FormTabDef formTabDef = formTabDefs.get(i);
            tsdb.addTabDef(formTabDef.getName(), formTabDef.getLabel(), "!fc-miniform",
                    RendererType.SIMPLE_WIDGET);
            tabSheetItemList.add(new TabSheetItem(formTabDef.getName(), formTabDef.getApplet(),
                    new MiniForm(MiniFormScope.CHILD_FORM, formContext, formTabDef), i, true));
        }

        headlessForm = new HeadlessTabsForm(ctx.au(), new TabSheet(tsdb.build(), tabSheetItemList, true));
    }

    public void commitPropertyList() throws UnifyException {
        ctx.au().savePropertyListValues(sweepingCommitPolicy, inst, childFkFieldName, propertyRuleDef, propValues);
    }
}
