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
package com.flowcentraltech.flowcentral.configuration.constants;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Applet type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Table("FC_APPLETTYPE")
@StaticList(name = "applettypelist", description = "$m{staticlist.applettypelist}")
public enum AppletType implements EnumConst {

    MANAGE_ENTITYLIST(
            "MEL",
            FlowCentralAppletPathConstants.MANAGE_ENTITYLIST,
            false,
            true),
    MANAGE_ENTITYLIST_ASSIGN(
            "MEA",
            FlowCentralAppletPathConstants.MANAGE_ENTITYLIST,
            false,
            true),
    MANAGE_ENTITYLIST_SUBMISSION(
            "MEB",
            FlowCentralAppletPathConstants.MANAGE_ENTITYLIST,
            false,
            true),
    MANAGE_ENTITYLIST_SINGLEFORM(
            "MLS",
            FlowCentralAppletPathConstants.MANAGE_ENTITYLIST_SINGLEFORM,
            false,
            true),
    MANAGE_LOADINGLIST(
            "MLL",
            FlowCentralAppletPathConstants.MANAGE_LOADINGLIST,
            false,
            true),
    HEADLESS_TABS(
            "HDL",
            FlowCentralAppletPathConstants.HEADLESS_TABS,
            false,
            true),
    CREATE_ENTITY(
            "CEN",
            FlowCentralAppletPathConstants.CREATE_ENTITY,
            true,
            true),
    CREATE_ENTITY_SUBMISSION(
            "CEB",
            FlowCentralAppletPathConstants.CREATE_ENTITY,
            true,
            true),
    CREATE_ENTITY_SINGLEFORM(
            "CNS",
            FlowCentralAppletPathConstants.CREATE_ENTITY_SINGLEFORM,
            true,
            true),
    FORM_WIZARD(
            "FWZ",
            FlowCentralAppletPathConstants.FORM_WIZARD,
            true,
            true),
    LISTING(
            "LST",
            FlowCentralAppletPathConstants.LISTING,
            true,
            false),
    MANAGE_PROPERTYLIST(
            "MPL",
            null,
            false,
            true),
    MY_WORKITEM(
            "MWI",
            FlowCentralAppletPathConstants.MY_WORKITEM,
            true,
            true),
    TASK_EXECUTION(
            "TEX",
            FlowCentralAppletPathConstants.TASK_EXECUTION,
            true,
            true),
    DATA_IMPORT(
            "DIM",
            FlowCentralAppletPathConstants.DATA_IMPORT,
            true,
            true),
    FACADE(
            "FAC",
            null,
            false,
            false),
    FACADE_MULTIPLE(
            "FCM",
            null,
            false,
            false),
    PATH_PAGE(
            "PPG",
            null,
            false,
            true),
    PATH_WINDOW(
            "PWN",
            null,
            false,
            true),
    REVIEW_WORKITEMS(
            "RWK",
            FlowCentralAppletPathConstants.REVIEW_WORKITEMS,
            false,
            true),
    REVIEW_SINGLEFORMWORKITEMS(
            "RWS",
            FlowCentralAppletPathConstants.REVIEW_SINGLEFORMWORKITEMS,
            false,
            true),
    REVIEW_WIZARDWORKITEMS(
            "RWZ",
            FlowCentralAppletPathConstants.REVIEW_WIZARDWORKITEMS,
            false,
            true),
    STUDIO_FC_COMPONENT(
            "SAC",
            null,
            true,
            true);

    public static final List<AppletType> MANAGE_ENTITY_LIST_TYPES = Collections.unmodifiableList(Arrays.asList(
            MANAGE_ENTITYLIST, MANAGE_ENTITYLIST_ASSIGN, MANAGE_ENTITYLIST_SUBMISSION, MANAGE_ENTITYLIST_SINGLEFORM));

    public static final List<AppletType> UNRESERVED_LIST = Collections.unmodifiableList(Arrays.asList(MANAGE_ENTITYLIST,
            MANAGE_ENTITYLIST_ASSIGN, MANAGE_ENTITYLIST_SUBMISSION, MANAGE_ENTITYLIST_SINGLEFORM, MANAGE_LOADINGLIST,
            HEADLESS_TABS, CREATE_ENTITY, CREATE_ENTITY_SUBMISSION, CREATE_ENTITY_SINGLEFORM, FORM_WIZARD, LISTING, TASK_EXECUTION, FACADE,
            FACADE_MULTIPLE, PATH_WINDOW, PATH_PAGE));

    private final String code;

    private final String path;

    private final boolean formInitial;

    private final boolean replication;

    private AppletType(String code, String path, boolean formInitial, boolean replication) {
        this.code = code;
        this.path = path;
        this.formInitial = formInitial;
        this.replication = replication;
    }

    @Override
    public String code() {
        return this.code;
    }

    @Override
    public String defaultCode() {
        return MANAGE_ENTITYLIST.code;
    }

    public String path() {
        return path;
    }

    public boolean isFormInitial() {
        return formInitial;
    }

    public boolean isSupportReplication() {
        return replication;
    }

    public boolean reserved() {
        return !UNRESERVED_LIST.contains(this);
    }

    public boolean workflow() {
        return REVIEW_WIZARDWORKITEMS.equals(this) || REVIEW_WORKITEMS.equals(this)
                || REVIEW_SINGLEFORMWORKITEMS.equals(this);
    }

    public boolean isOpenWindow() {
        return PATH_WINDOW.equals(this);
    }

    public boolean isFacade() {
        return FACADE.equals(this);
    }

    public boolean isMultiFacade() {
        return FACADE_MULTIPLE.equals(this);
    }

    public boolean isEntityList() {
        return MANAGE_ENTITYLIST.equals(this) || MANAGE_ENTITYLIST_ASSIGN.equals(this)
                || MANAGE_ENTITYLIST_SUBMISSION.equals(this) || MANAGE_ENTITYLIST_SINGLEFORM.equals(this);
    }

    public boolean isAssignment() {
        return MANAGE_ENTITYLIST_ASSIGN.equals(this);
    }

    public boolean isSubmission() {
        return MANAGE_ENTITYLIST_SUBMISSION.equals(this) || CREATE_ENTITY_SUBMISSION.equals(this);
    }

    public boolean isSingleForm() {
        return CREATE_ENTITY_SINGLEFORM.equals(this) || MANAGE_ENTITYLIST_SINGLEFORM.equals(this);
    }

    public boolean isCreate() {
        return CREATE_ENTITY.equals(this) || CREATE_ENTITY_SUBMISSION.equals(this)
                || CREATE_ENTITY_SINGLEFORM.equals(this);
    }

    public boolean isStudioComponent() {
        return STUDIO_FC_COMPONENT.equals(this);
    }

    public boolean supportsDetachedMaintain() {
        return MANAGE_ENTITYLIST.equals(this) || MANAGE_ENTITYLIST_ASSIGN.equals(this)
                || MANAGE_ENTITYLIST_SUBMISSION.equals(this);
    }

    public static AppletType fromCode(String code) {
        return EnumUtils.fromCode(AppletType.class, code);
    }

    public static AppletType fromName(String name) {
        return EnumUtils.fromName(AppletType.class, name);
    }
}
