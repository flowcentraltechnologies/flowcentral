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

import com.flowcentraltech.flowcentral.application.util.PrivilegeNameUtils;
import com.flowcentraltech.flowcentral.configuration.constants.HighlightType;
import com.flowcentraltech.flowcentral.configuration.constants.UIActionType;

/**
 * Form action definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormActionDef {

    private UIActionType type;

    private HighlightType highlightType;

    private String name;

    private String description;

    private String label;

    private String symbol;

    private String styleClass;

    private String policy;

    private String privilege;

    private int orderIndex;

    private boolean showOnCreate;

    private boolean showOnMaintain;

    private boolean validateForm;

    public FormActionDef(UIActionType type, HighlightType highlightType, String name, String description, String label,
            String symbol, String styleClass, int orderIndex, boolean validateForm) {
        this.type = type;
        this.highlightType = highlightType;
        this.name = name;
        this.description = description;
        this.label = label;
        this.symbol = symbol;
        this.styleClass = styleClass;
        this.orderIndex = orderIndex;
        this.validateForm = validateForm;
        this.showOnCreate = true;
        this.showOnMaintain = true;
    }

    public FormActionDef(UIActionType type, HighlightType highlightType, String name, String description, String label,
            String symbol, String styleClass, String policy, int orderIndex, boolean showOnCreate,
            boolean showOnMaintain, boolean validateForm) {
        this.type = type;
        this.highlightType = highlightType;
        this.name = name;
        this.description = description;
        this.label = label;
        this.symbol = symbol;
        this.styleClass = styleClass;
        this.policy = policy;
        this.orderIndex = orderIndex;
        this.showOnCreate = showOnCreate;
        this.showOnMaintain = showOnMaintain;
        this.validateForm = validateForm;
        this.privilege = PrivilegeNameUtils.getFormActionPrivilegeName(name);
    }

    public UIActionType getType() {
        return type;
    }

    public HighlightType getHighlightType() {
        return highlightType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getLabel() {
        return label;
    }

    public String getSymbol() {
        return symbol;
    }

    public String getStyleClass() {
        return highlightType != null ? highlightType.buttonClass() : styleClass;
    }

    public String getPolicy() {
        return policy;
    }

    public String getPrivilege() {
        return privilege;
    }

    public int getOrderIndex() {
        return orderIndex;
    }

    public boolean isShowOnCreate() {
        return showOnCreate;
    }

    public boolean isShowOnMaintain() {
        return showOnMaintain;
    }

    public boolean isValidateForm() {
        return validateForm;
    }

    public boolean isButtonType() {
        return UIActionType.BUTTON.equals(type);
    }

    public boolean isLinkType() {
        return UIActionType.LINK.equals(type);
    }

    public boolean isWithPrivilege() {
        return privilege != null;
    }

    public boolean isWithPolicy() {
        return policy != null;
    }

    @Override
    public String toString() {
        return "FormActionDef [type=" + type + ", name=" + name + ", description=" + description + ", label=" + label
                + ", symbol=" + symbol + ", styleClass=" + styleClass + ", policy=" + policy + ", privilege="
                + privilege + ", orderIndex=" + orderIndex + ", showOnCreate=" + showOnCreate + ", showOnMaintain="
                + showOnMaintain + ", validateForm=" + validateForm + "]";
    }
}
