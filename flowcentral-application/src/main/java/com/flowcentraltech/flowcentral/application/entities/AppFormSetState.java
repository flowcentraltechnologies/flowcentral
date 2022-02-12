/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */

package com.flowcentraltech.flowcentral.application.entities;

import com.flowcentraltech.flowcentral.common.entities.BaseConfigEntity;
import com.flowcentraltech.flowcentral.configuration.constants.FormElementType;
import com.tcdng.unify.core.annotation.Column;
import com.tcdng.unify.core.annotation.ForeignKey;
import com.tcdng.unify.core.annotation.ListOnly;
import com.tcdng.unify.core.annotation.Table;
import com.tcdng.unify.core.annotation.UniqueConstraint;
import com.tcdng.unify.core.constant.TriState;

/**
 * Application form set state.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
@Table(name = "FC_FORMSETSTATE",
        uniqueConstraints = { @UniqueConstraint({ "appFormStatePolicyId", "type", "target" }) })
public class AppFormSetState extends BaseConfigEntity {

    @ForeignKey(AppFormStatePolicy.class)
    private Long appFormStatePolicyId;

    @ForeignKey(name = "ELEMENT_TY")
    private FormElementType type;

    @Column(length = 512)
    private String target;

    @Column
    private TriState required;

    @Column
    private TriState visible;

    @Column
    private TriState editable;

    @Column
    private TriState disabled;

    @ListOnly(key = "type", property = "description")
    private String typeDesc;

    public AppFormSetState() {
        required = TriState.CONFORMING;
        visible = TriState.CONFORMING;
        editable = TriState.CONFORMING;
        disabled = TriState.CONFORMING;
    }

    @Override
    public String getDescription() {
        return target;
    }

    public Long getAppFormStatePolicyId() {
        return appFormStatePolicyId;
    }

    public void setAppFormStatePolicyId(Long appFormStatePolicyId) {
        this.appFormStatePolicyId = appFormStatePolicyId;
    }

    public FormElementType getType() {
        return type;
    }

    public void setType(FormElementType type) {
        this.type = type;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public TriState getRequired() {
        return required;
    }

    public void setRequired(TriState required) {
        this.required = required;
    }

    public TriState getVisible() {
        return visible;
    }

    public void setVisible(TriState visible) {
        this.visible = visible;
    }

    public TriState getEditable() {
        return editable;
    }

    public void setEditable(TriState editable) {
        this.editable = editable;
    }

    public TriState getDisabled() {
        return disabled;
    }

    public void setDisabled(TriState disabled) {
        this.disabled = disabled;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

}
