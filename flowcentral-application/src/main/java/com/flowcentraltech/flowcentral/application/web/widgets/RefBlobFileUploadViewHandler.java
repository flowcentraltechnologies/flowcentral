/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.SpecialParamProvider;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.constant.FileAttachmentType;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.ui.widget.control.AbstractFileUploadViewHandler;
import com.tcdng.unify.web.ui.widget.data.FileAttachmentInfo;

/**
 * File upload view handler for blob references.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("refblob-fileuploadviewhandler")
public class RefBlobFileUploadViewHandler extends AbstractFileUploadViewHandler {

    @Configurable
    private AppletUtilities au;

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Object save(Object id, String category, FileAttachmentType type, String filename, byte[] attachment)
            throws UnifyException {
        final RefDef refDef = application().getRefDef(category);
        final EntityClassDef entityClassDef = application().getEntityClassDef(refDef.getEntity());
        final EntityDef entityDef = entityClassDef.getEntityDef();
        final String blobFieldName = entityDef.getBlobFieldName();
        final String blobDescFieldName = entityDef.getBlobDescFieldName();
        if (id == null) {
            Entity inst = entityClassDef.newInst();
            DataUtils.setBeanProperty(inst, blobFieldName, attachment);
            if (!StringUtils.isBlank(blobDescFieldName)) {
                DataUtils.setBeanProperty(inst, blobDescFieldName, filename);
            }

            return environment().create(inst);
        }

        Update update = new Update().add(blobFieldName, attachment);
        if (!StringUtils.isBlank(blobDescFieldName)) {
            update.add(blobDescFieldName, filename);
        }
        environment().updateById((Class<? extends Entity>) entityClassDef.getEntityClass(), id, update);
        return id;
    }

    @SuppressWarnings("unchecked")
    @Override
    public FileAttachmentInfo peek(Object id, String category, FileAttachmentType type) throws UnifyException {
        final RefDef refDef = application().getRefDef(category);
        final EntityClassDef entityClassDef = application().getEntityClassDef(refDef.getEntity());
        final EntityDef entityDef = entityClassDef.getEntityDef();
        final String blobFieldName = entityDef.getBlobFieldName();
        final String blobDescFieldName = entityDef.getBlobDescFieldName();

        Query<? extends Entity> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass())
                .addEquals("id", id);
        final String fileName = !StringUtils.isBlank(blobDescFieldName)
                ? environment().value(String.class, blobDescFieldName, query)
                : null;
        query.addIsNotNull(blobFieldName);
        final boolean present = environment().countAll(query) > 0;

        FileAttachmentInfo fileAttachmentInfo = new FileAttachmentInfo(type);
        fileAttachmentInfo.setFilename(fileName);
        fileAttachmentInfo.setPresent(present);
        return fileAttachmentInfo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public FileAttachmentInfo retrive(Object id, String category, FileAttachmentType type) throws UnifyException {
        final RefDef refDef = application().getRefDef(category);
        final EntityClassDef entityClassDef = application().getEntityClassDef(refDef.getEntity());
        final EntityDef entityDef = entityClassDef.getEntityDef();
        final String blobFieldName = entityDef.getBlobFieldName();
        final String blobDescFieldName = entityDef.getBlobDescFieldName();
        final Entity inst = environment().find((Class<? extends Entity>) entityClassDef.getEntityClass(), id);
        final byte[] attachment = DataUtils.getBeanProperty(byte[].class, inst, blobFieldName);
        final String filename = !StringUtils.isBlank(blobDescFieldName)
                ? DataUtils.getBeanProperty(String.class, inst, blobDescFieldName)
                : null;

        FileAttachmentInfo fileAttachmentInfo = new FileAttachmentInfo(type);
        fileAttachmentInfo.setFilename(filename);
        fileAttachmentInfo.setAttachment(attachment);
        return fileAttachmentInfo;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void delete(Object id, String category, Object parentId, String parentCategory, String parentFieldName)
            throws UnifyException {
        final RefDef refDef = application().getRefDef(category);
        final EntityClassDef entityClassDef = application().getEntityClassDef(refDef.getEntity());
        if (parentId != null && !StringUtils.isBlank(parentCategory) || !StringUtils.isBlank(parentFieldName)) {
            final EntityClassDef _parentEntityClassDef = application().getEntityClassDef(parentCategory);
            environment().updateById((Class<? extends Entity>) _parentEntityClassDef.getEntityClass(), parentId,
                    new Update().add(parentFieldName, null));
        }
        environment().delete((Class<? extends Entity>) entityClassDef.getEntityClass(), id);
    }

    protected ApplicationModuleService application() {
        return au.application();
    }

    protected EnvironmentService environment() {
        return au.environment();
    }

    protected SpecialParamProvider specialParamProvider() {
        return au.specialParamProvider();
    }

}
