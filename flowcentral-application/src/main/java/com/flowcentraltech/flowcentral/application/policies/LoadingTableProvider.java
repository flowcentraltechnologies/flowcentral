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
package com.flowcentraltech.flowcentral.application.policies;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.EntityItem;
import com.flowcentraltech.flowcentral.application.data.LoadingWorkItemInfo;
import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.common.entities.WorkEntity;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;

/**
 * Loading table provider..
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface LoadingTableProvider extends UnifyComponent {

    /**
     * Gets the loading label
     * 
     * @return the loading label
     * @throws UnifyException
     *                        if an error occurs
     */
    String getLoadingLabel() throws UnifyException;

    /**
     * Count items for table loading.
     * 
     * @param restriction
     *                    optional restriction
     * @return loading item count
     * @throws UnifyException
     *                        if an error occurs
     */
    int countLoadingItems(Restriction restriction) throws UnifyException;

    /**
     * Gets items for table loading.
     * 
     * @param restriction
     *                    optional restriction
     * @return list of entities
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Entity> getLoadingItems(Restriction restriction) throws UnifyException;

    /**
     * Gets the source entity items using supplied ID
     * 
     * @param sourceItemId
     *                     the id to use
     * @return the source entity item
     * @throws UnifyException
     *                        if an error occurs
     */
    EntityItem getSourceItem(Long sourceItemId) throws UnifyException;

    /**
     * Gets the source entity item form applet.
     * 
     * @return the source entity item form applet
     * @throws UnifyException
     *                        if an error occurs
     */
    String getSourceItemFormApplet() throws UnifyException;

    /**
     * Gets loading work item information.
     * 
     * @param inst
     *             the work item instance
     * @return the loading work item information
     * @throws UnifyException
     *                        if an error occurs.
     */
    LoadingWorkItemInfo getLoadingWorkItemInfo(WorkEntity inst) throws UnifyException;
    
    /**
     * Applies user action to work item.
     * 
     * @param wfEntityInst
     *                     the work item instance
     * @param sourceItemId
     *                     the source item ID
     * @param userAction
     *                     the user action to apply
     * @param comment
     *                     the comment on this action
     * @param emails
     *                     the emails
     * @return if successful
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean applyUserAction(WorkEntity wfEntityInst, Long sourceItemId, String userAction, String comment,
            InputArrayEntries emails) throws UnifyException;
    
    /**
     * Commit change to entity
     * 
     * @param itemValueStore
     *                       the item value store
     * @throws UnifyException
     *                        if an error occurs
     */
    void commitChange(ValueStore itemValueStore) throws UnifyException;
}
