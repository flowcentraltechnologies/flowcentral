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

package com.flowcentraltech.flowcentral.studio.web.panels;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleSysParamConstants;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.FieldValidationPolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormActionDef;
import com.flowcentraltech.flowcentral.application.data.FormAnnotationDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormRelatedListDef;
import com.flowcentraltech.flowcentral.application.data.FormStatePolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormValidationPolicyDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.data.WidgetTypeDef;
import com.flowcentraltech.flowcentral.application.util.InputWidgetUtils;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.HeaderWithTabsForm;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.FormType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.configuration.constants.WidgetColor;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor.Design;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor.FormField;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor.FormSection;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor.FormTab;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Form preview object
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormPreview {

    private final AppletUtilities au;

    private final FormEditor formEditor;

    private HeaderWithTabsForm form;

    private Design oldDesign;

    public FormPreview(AppletUtilities au, FormEditor formEditor) {
        this.au = au;
        this.formEditor = formEditor;
    }

    public HeaderWithTabsForm getForm() {
        return form;
    }

    public void reload() throws UnifyException {
        Design design = formEditor.getDesign();
        if (oldDesign != design) {
            final FormDef originFormDef = formEditor.getFormDef();
            final EntityDef entityDef = originFormDef.getEntityDef();
            final boolean useFormColorScheme = au.getSysParameterValue(boolean.class,
                    ApplicationModuleSysParamConstants.USE_APPLICATION_FORM_COLOR_SCHEME);
            FormDef.Builder fdb = FormDef.newBuilder(FormType.INPUT, entityDef, null, null, null, null,
                    "studio.previewForm", "Preview Form", 0L, 0L);
            if (design != null && design.getTabs() != null) {
                int tabIndex = -1;
                for (FormTab formTab : design.getTabs()) {
                    tabIndex++;
                    fdb.addFormTab(TabContentType.fromName(formTab.getContentType()), formTab.getName(),
                            formTab.getLabel(), formTab.getApplet(), formTab.getReference(), formTab.getFilter(),
                            formTab.getEditAction(), formTab.getEditFormless(), formTab.getEditFixedRows(),
                            formTab.isShowSearch(), formTab.isVisible(), formTab.isEditable(), formTab.isDisabled());
                    int sectionIndex = -1;
                    for (FormSection formSection : formTab.getSections()) {
                        sectionIndex++;
                        fdb.addFormSection(tabIndex, formSection.getName(), formSection.getLabel(),
                                FormColumnsType.fromCode(formSection.getColumns()), formSection.isVisible(),
                                formSection.isEditable(), formSection.isDisabled());
                        for (FormField formField : formSection.getFields()) {
                            final String fieldName = formField.getName();
                            if (entityDef.isWithFieldDef(fieldName)) {
                                EntityFieldDef entityFieldDef = entityDef.getFieldDef(fieldName);
                                WidgetTypeDef widgetTypeDef = !StringUtils.isBlank(formField.getInputWidget())
                                        ? au.getWidgetTypeDef(formField.getInputWidget())
                                        : entityFieldDef.getInputWidgetTypeDef();
                                widgetTypeDef = widgetTypeDef != null ? widgetTypeDef
                                        : au.getWidgetTypeDef("application.text");
                                WidgetColor color = formField.getColor() != null
                                        ? WidgetColor.fromCode(formField.getColor())
                                        : null;
                                if (useFormColorScheme) {
                                    color = au.isEntitySearchWidget(widgetTypeDef.getLongName()) ? WidgetColor.PURPLE
                                            : entityFieldDef.isWithAutoFormat() ? WidgetColor.NAVY_GRAY : color;
                                }

                                String renderer = InputWidgetUtils.constructEditorWithBinding(widgetTypeDef,
                                        entityFieldDef, formField.getReference(), color);
                                RefDef inputRefDef = !StringUtils.isBlank(formField.getReference())
                                        ? au.getRefDef(formField.getReference())
                                        : entityFieldDef.getRefDef();

                                String label = formField.getLabel();
                                if (label == null) {
                                    label = entityFieldDef.getFieldLabel();
                                }

                                fdb.addFormField(tabIndex, sectionIndex, entityFieldDef, widgetTypeDef, inputRefDef,
                                        label, renderer, formField.getColumn(), formField.isSwitchOnChange(),
                                        formField.isSaveAs(), formField.isRequired(), formField.isVisible(),
                                        formField.isEditable(), formField.isDisabled());
                            }
                        }
                    }
                }

                for (FormAnnotationDef formAnnotationDef : originFormDef.getFormAnnotationDefList()) {
                    fdb.addFormAnnotation(formAnnotationDef);
                }

                for (FormActionDef formActionDef : originFormDef.getFormActionDefList()) {
                    fdb.addFormAction(formActionDef);
                }

                for (FormRelatedListDef formRelatedListDef : originFormDef.getFormRelatedListDefList()) {
                    fdb.addRelatedList(formRelatedListDef);
                }

                for (FormStatePolicyDef formStatePolicyDef : originFormDef.getFormStatePolicyDefList()) {
                    fdb.addFormStatePolicy(formStatePolicyDef);
                }

                for (FieldValidationPolicyDef fieldValidationPolicyDef : originFormDef.getFieldValidationPolicies()) {
                    fdb.addFieldValidationPolicy(fieldValidationPolicyDef);
                }

                for (FormValidationPolicyDef formValidationPolicyDef : originFormDef.getFormValidationPolicies()) {
                    fdb.addFormValidationPolicy(formValidationPolicyDef);
                }

            }

            final FormDef formDef = fdb.build();
            final Object inst = ReflectUtils
                    .newInstance(au.getEntityClassDef(formDef.getEntityDef().getListKey()).getEntityClass());
            form = au.constructHeaderWithTabsForm(null, "Preview Subtitle", "Preview Title", formDef, (Entity) inst,
                    FormMode.MAINTAIN, null, null);
            oldDesign = design;
        }
    }
}
