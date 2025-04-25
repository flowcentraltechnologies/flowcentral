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

package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;

/**
 * Convenient abstract base class for studio editor pages.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public abstract class AbstractStudioEditorPage {

    private final AppletUtilities au;

    private final BreadCrumbs breadCrumbs;

    private String displayItemCounter;

    private String displayItemCounterClass;

    public AbstractStudioEditorPage(AppletUtilities au, BreadCrumbs breadCrumbs) {
        this.au = au;
        this.breadCrumbs = breadCrumbs;
    }

    public final String getMainTitle() {
        return breadCrumbs.getLastBreadCrumb().getTitle();
    }

    public final String getSubTitle() {
        return breadCrumbs.getLastBreadCrumb().getSubTitle();
    }

    public final BreadCrumbs getBreadCrumbs() {
        return breadCrumbs;
    }

    public final AppletUtilities getAu() {
        return au;
    }

    public final String getDisplayItemCounter() {
        return displayItemCounter;
    }

    public final void setDisplayItemCounter(String displayItemCounter) {
        this.displayItemCounter = displayItemCounter;
    }

    public final String getDisplayItemCounterClass() {
        return displayItemCounterClass;
    }

    public final void setDisplayItemCounterClass(String displayItemCounterClass) {
        this.displayItemCounterClass = displayItemCounterClass;
    }

}
