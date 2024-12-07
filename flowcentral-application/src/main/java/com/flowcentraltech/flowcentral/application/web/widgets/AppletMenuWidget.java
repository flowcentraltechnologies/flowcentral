/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.annotation.Action;
import com.tcdng.unify.web.constant.ResultMappingConstants;
import com.tcdng.unify.web.constant.UnifyWebRequestAttributeConstants;
import com.tcdng.unify.web.ui.widget.Control;
import com.tcdng.unify.web.ui.widget.data.RefreshSection;

/**
 * Applet menu widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-appletmenu")
@UplAttributes({
    @UplAttribute(name = "searchable", type = boolean.class, defaultVal = "false"),
    @UplAttribute(name = "horizontal", type = boolean.class, defaultVal = "false")})
public class AppletMenuWidget extends AbstractMenuWidget {

    private Control searchCtrl;

    private String searchInput;

    @Action
    public void clear() throws UnifyException {
        searchInput = null;
        refereshSection();
    }

    @Action
    public void search() throws UnifyException {
        searchInput = getRequestTarget(String.class);
        searchInput = searchInput != null ? searchInput.toLowerCase() : null;
        refereshSection();
    }
    
    @Override
    public String getMenuSectionId() throws UnifyException {
        return getPrefixedId("msc_");
    }

    public String getClearId() throws UnifyException {
        return getPrefixedId("clr_");
    }

    public boolean isSearchable() throws UnifyException {
        return getUplAttribute(boolean.class, "searchable");
    }

    @Override
    public boolean isHorizontal() throws UnifyException {
        return getUplAttribute(boolean.class, "horizontal");
    }

    public String getSearchInput() {
        return searchInput;
    }

    public void setSearchInput(String searchInput) {
        this.searchInput = searchInput;
    }

    public Control getSearchCtrl() {
        return searchCtrl;
    }

    @Override
    public void onPageConstruct() throws UnifyException {
        super.onPageConstruct();
        if (isSearchable()) {
            searchCtrl = (Control) addInternalWidget("!ui-text styleClass:$s{search}  binding:searchInput");
        }
    }

    private void refereshSection() throws UnifyException {
        setCollapsedInitial(StringUtils.isBlank(searchInput));
        setRequestAttribute(UnifyWebRequestAttributeConstants.REFRESH_SECTION,
                new RefreshSection(this, getMenuSectionId()));
        setCommandResultMapping(ResultMappingConstants.REFRESH_SECTION);
    }

}
