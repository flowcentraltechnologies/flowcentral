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

package com.flowcentraltech.flowcentral.application.data;

import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.common.constants.MaintainType;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Entity item.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityItem implements FormAppendables {

    private MaintainType maintainType;

    private Entity entity;

    private InputArrayEntries emails;

    private Comments comments;

    private Attachments attachments;

    private Errors errors;

    private FormListingOptions listingOptions;

    private String listingGenerator;

    protected EntityItem(MaintainType maintainType, Entity entity, InputArrayEntries emails, Comments comments,
            Attachments attachments, Errors errors, FormListingOptions listingOptions, String listingGenerator) {
        this.maintainType = maintainType;
        this.entity = entity;
        this.emails = emails;
        this.comments = comments;
        this.attachments = attachments;
        this.errors = errors;
        this.listingOptions = listingOptions;
        this.listingGenerator = listingGenerator;
    }

    protected EntityItem(MaintainType maintainType, Entity entity, FormListingOptions listingOptions, String listingGenerator) {
        this.maintainType = maintainType;
        this.entity = entity;
        this.listingOptions = listingOptions;
        this.listingGenerator = listingGenerator;
    }

    public MaintainType getMaintainType() {
        return maintainType;
    }

    public FormListingOptions getListingOptions() {
        return listingOptions;
    }

    public String getListingGenerator() {
        return listingGenerator;
    }

    public boolean isEdit() {
        return maintainType.isEdit();
    }

    public boolean isReport() {
        return maintainType.isReport();
    }

    public boolean isWorkItem() {
        return maintainType.isWorkItem();
    }

    public boolean isWorkItemSingleForm() {
        return maintainType.isWorkItemSingleForm();
    }

    public boolean isWithListingParams() {
        return listingOptions != null && !StringUtils.isBlank(listingGenerator);
    }

    public Entity getEntity() {
        return entity;
    }

    @Override
    public InputArrayEntries getEmails() {
        return emails;
    }

    @Override
    public Comments getComments() {
        return comments;
    }

    @Override
    public Attachments getAttachments() {
        return attachments;
    }

    @Override
    public Errors getErrors() {
        return errors;
    }
}
