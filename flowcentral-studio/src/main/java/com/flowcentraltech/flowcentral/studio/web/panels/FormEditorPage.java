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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.TabSheetDef;
import com.flowcentraltech.flowcentral.application.entities.AppForm;
import com.flowcentraltech.flowcentral.application.entities.AppFormElement;
import com.flowcentraltech.flowcentral.application.web.widgets.BreadCrumbs;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheet.TabSheetItem;
import com.flowcentraltech.flowcentral.application.web.widgets.TabSheetEventHandler;
import com.flowcentraltech.flowcentral.configuration.constants.FormColumnsType;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.flowcentraltech.flowcentral.configuration.constants.RendererType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.flowcentraltech.flowcentral.configuration.constants.WidgetColor;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor.FormField;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor.FormSection;
import com.flowcentraltech.flowcentral.studio.web.widgets.FormEditor.FormTab;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Form editor page.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class FormEditorPage extends AbstractStudioEditorPage implements TabSheetEventHandler {

    private static final int DESIGN_INDEX = 0;

    private static final int PREVIEW_INDEX = 1;

    private final FormDef formDef;

    private final Object baseId;

    private TabSheet tabSheet;

    private FormEditor formEditor;

    private FormPreview formPreview;

    public FormEditorPage(AppletUtilities au, FormDef formDef, Object baseId, BreadCrumbs breadCrumbs) {
        super(au, breadCrumbs);
        this.formDef = formDef;
        this.baseId = baseId;
    }

    public TabSheet getTabSheet() {
        return tabSheet;
    }

    public FormEditor getFormEditor() {
        return formEditor;
    }

    public FormPreview getFormPreview() {
        return formPreview;
    }

    public FormDef getFormDef() {
        return formDef;
    }

    public Object getBaseId() {
        return baseId;
    }

    @Override
    public void onChoose(TabSheetItem tabSheetItem) throws UnifyException {
        switch (tabSheetItem.getIndex()) {
            case DESIGN_INDEX:
                break;
            case PREVIEW_INDEX:
                formPreview.reload();
                break;
            default:
        }
    }

    public void commitDesign() throws UnifyException {
        AppForm appForm = getAu().environment().find(AppForm.class, baseId);
        List<AppFormElement> elementList = Collections.emptyList();
        if (formEditor.getDesign() != null && formEditor.getDesign().getTabs() != null) {
            elementList = new ArrayList<AppFormElement>();
            for (FormTab formTab : formEditor.getDesign().getTabs()) {
                AppFormElement appFormElement = new AppFormElement();
                appFormElement.setType(FormElementType.TAB);
                appFormElement.setTabContentType(TabContentType.fromName(formTab.getContentType()));
                appFormElement.setElementName(formTab.getName());
                appFormElement.setLabel(formTab.getLabel());
                appFormElement.setTabApplet(formTab.getApplet());
                appFormElement.setTabReference(formTab.getReference());
                appFormElement.setFilter(formTab.getFilter());
                appFormElement.setEditAction(formTab.getEditAction());
                appFormElement.setShowSearch(formTab.isShowSearch());
                appFormElement.setVisible(formTab.isVisible());
                appFormElement.setEditable(formTab.isEditable());
                appFormElement.setDisabled(formTab.isDisabled());
                elementList.add(appFormElement);

                for (FormSection formSection : formTab.getSections()) {
                    appFormElement = new AppFormElement();
                    appFormElement.setType(FormElementType.SECTION);
                    appFormElement.setSectionColumns(FormColumnsType.fromCode(formSection.getColumns()));
                    appFormElement.setElementName(formSection.getName());
                    appFormElement.setLabel(formSection.getLabel());
                    appFormElement.setVisible(formSection.isVisible());
                    appFormElement.setEditable(formSection.isEditable());
                    appFormElement.setDisabled(formSection.isDisabled());
                    elementList.add(appFormElement);

                    for (FormField formField : formSection.getFields()) {
                        appFormElement = new AppFormElement();
                        appFormElement.setType(FormElementType.FIELD);
                        appFormElement.setElementName(formField.getName());
                        appFormElement.setLabel(formField.getLabel());
                        appFormElement.setInputWidget(formField.getInputWidget());
                        appFormElement.setInputReference(formField.getReference());
                        WidgetColor color = !StringUtils.isBlank(formField.getColor())
                                ? WidgetColor.fromCode(formField.getColor())
                                : null;
                        appFormElement.setColor(color);
                        appFormElement.setFieldColumn(formField.getColumn());
                        appFormElement.setSwitchOnChange(formField.isSwitchOnChange());
                        appFormElement.setRequired(formField.isRequired());
                        appFormElement.setVisible(formField.isVisible());
                        appFormElement.setEditable(formField.isEditable());
                        appFormElement.setDisabled(formField.isDisabled());
                        elementList.add(appFormElement);
                    }
                }
            }
        }

        appForm.setElementList(elementList);
        getAu().environment().updateByIdVersion(appForm);
    }

    public void newEditor() throws UnifyException {
        FormEditor.Builder feb = FormEditor.newBuilder(formDef);
        for (AppFormElement appFormElement : getAu().environment()
                .findAll(Query.of(AppFormElement.class).addEquals("appFormId", baseId).addOrder("id"))) {
            switch (appFormElement.getType()) {
                case FIELD:
                    if (formDef.getEntityDef().isWithFieldDef(appFormElement.getElementName())) {
                        EntityFieldDef entityFieldDef = formDef.getEntityDef()
                                .getFieldDef(appFormElement.getElementName());
                        feb.addField(formDef.getEntityDef().getLongName(), entityFieldDef.getDataType(),
                                appFormElement.getElementName(), appFormElement.getLabel(),
                                appFormElement.getInputWidget(), appFormElement.getInputReference(),
                                appFormElement.getColor(), appFormElement.getFieldColumn(),
                                appFormElement.isSwitchOnChange(), appFormElement.isSaveAs(),
                                appFormElement.isRequired(), appFormElement.isVisible(), appFormElement.isEditable(),
                                appFormElement.isDisabled());
                    }
                    break;
                case SECTION:
                    feb.addSection(appFormElement.getElementName(), appFormElement.getLabel(),
                            appFormElement.getSectionColumns(), appFormElement.isVisible(), appFormElement.isEditable(),
                            appFormElement.isDisabled());
                    break;
                case TAB:
                    feb.addTab(appFormElement.getTabContentType().name(), appFormElement.getElementName(),
                            appFormElement.getLabel(), appFormElement.getTabApplet(), appFormElement.getTabReference(),
                            appFormElement.getFilter(), appFormElement.getEditAction(), appFormElement.isShowSearch(),
                            appFormElement.isVisible(), appFormElement.isEditable(), appFormElement.isDisabled());
                    break;
                default:
                    break;

            }
        }

        formEditor = feb.build(getAu());

        TabSheetDef.Builder tsdb = TabSheetDef.newBuilder(null, 1L);
        tsdb.addTabDef("editor", getAu().resolveSessionMessage("$m{studio.appform.form.design}"), "!fc-formeditor",
                RendererType.SIMPLE_WIDGET);
        tsdb.addTabDef("preview", getAu().resolveSessionMessage("$m{studio.appform.form.preview}"),
                "fc-formpreviewpanel", RendererType.STANDALONE_PANEL);
        formPreview = new FormPreview(getAu(), formEditor);
//        formPreview.reload();
        final String appletName = null;
        tabSheet = new TabSheet(tsdb.build(),
                Arrays.asList(new TabSheetItem("formEditor", appletName, formEditor, DESIGN_INDEX, true),
                        new TabSheetItem("formPreview", appletName, formPreview, PREVIEW_INDEX, true)));
        tabSheet.setEventHandler(this);
    }
}
