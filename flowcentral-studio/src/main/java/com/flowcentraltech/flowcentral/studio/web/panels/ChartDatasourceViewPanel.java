/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.annotation.Action;

/**
 * Chart datasource view panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-chartdatasourceviewpanel")
@UplBinding("web/studio/upl/chartdatasourceviewpanel.upl")
public class ChartDatasourceViewPanel extends AbstractStudioEditorPagePanel {

    @Override
    public void switchState() throws UnifyException {
        final ChartDatasourceView view = getChartDatasourceView();
        setVisible("snapshotBodyPanel", view != null && view.isWithSnapshot());
    }

    @Action
    public void refreshDesign() throws UnifyException {
        final ChartDatasourceView view = getChartDatasourceView();
        if (view != null) {
            view.reloadContent();
        }
    }

    @Action
    public void saveDesign() throws UnifyException {

    }

    @Override
    protected boolean isAppletContextReadOnly() throws UnifyException {
        return false;
    }

    protected ChartDatasourceView getChartDatasourceView() throws UnifyException {
        return getValue(ChartDatasourceView.class);
    }
}
