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

package com.flowcentraltech.flowcentral.common.business;

import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.constants.FileAttachmentCategoryType;
import com.flowcentraltech.flowcentral.common.data.Attachment;
import com.flowcentraltech.flowcentral.common.data.AttachmentDetails;
import com.tcdng.unify.core.UnifyException;

/**
 * File attachment provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface FileAttachmentProvider extends FlowCentralComponent {

    /**
     * Count number of file attachments for an entity instance.
     * 
     * @param category
     *                        the file attachment category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @return the number of attachments
     * @throws UnifyException
     *                        if an error occurs
     */
    int countFileAttachments(FileAttachmentCategoryType category, String ownerEntityName, Long ownerInstId)
            throws UnifyException;

    /**
     * Creates or updates a file attachment for an entity instance. An entity
     * instance can have multiple file attachments with different names.
     * 
     * @param category
     *                        the file attachment category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @param attachment
     *                        the attachment to save
     * @return true if new file attachment was created otherwise false if it was
     *         updated
     * @throws UnifyException
     *                        if entity instance with supplied ID does not exist. If
     *                        an error occurs
     */
    boolean saveFileAttachment(FileAttachmentCategoryType category, String ownerEntityName, Long ownerInstId,
            Attachment attachment) throws UnifyException;

    /**
     * Retrieves a file attachment for an entity instance.
     * 
     * @param category
     *                        the file attachment category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @param attachmentName
     *                        the attachment name
     * @return the attachment if exists otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    Attachment retrieveFileAttachment(FileAttachmentCategoryType category, String ownerEntityName, Long ownerInstId,
            String attachmentName) throws UnifyException;

    /**
     * Retrieves all file attachment for an entity instance.
     * 
     * @param category
     *                        the file attachment category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @return the attachment list
     * @throws UnifyException
     *                        if an error occurs
     */
    List<AttachmentDetails> retrieveAllFileAttachments(FileAttachmentCategoryType category, String ownerEntityName,
            Long ownerInstId) throws UnifyException;

    /**
     * Retrieves all file attachment for an entity instance.
     * 
     * @param category
     *                        the file attachment category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @return file names by attachment name
     * @throws UnifyException
     */
    Map<String, String> retrieveAllAttachmentFileNames(FileAttachmentCategoryType category, String ownerEntityName,
            Long ownerInstId) throws UnifyException;

    /**
     * Deletes a file attachment for an entity instance.
     * 
     * @param category
     *                        the file attachment category
     * @param ownerEntityName
     *                        the entity type long name
     * @param ownerInstId
     *                        the entity instance ID
     * @param attachmentName
     *                        the attachment name
     * @return true if attachment was deleted otherwise false when not existing
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean deleteFileAttachment(FileAttachmentCategoryType category, String ownerEntityName, Long ownerInstId,
            String attachmentName) throws UnifyException;

    /**
     * Synchronize file attachments from a source entity to a destination entity.
     * 
     * @param category
     *                         the file attachment category
     * @param ownerEntityName
     *                         the entity type long name
     * @param destEntityInstId
     *                         destination entity instance ID
     * @param srcEntityInstId
     *                         source entity instance ID
     * @return true if synchronized otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean sychFileAttachments(FileAttachmentCategoryType category, String ownerEntityName, Long destEntityInstId,
            Long srcEntityInstId) throws UnifyException;
}
