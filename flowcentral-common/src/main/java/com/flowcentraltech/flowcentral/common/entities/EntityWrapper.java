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
package com.flowcentraltech.flowcentral.common.entities;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.database.Entity;

/**
 * Entity wrapper.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EntityWrapper {

    /**
     * Gets wrapper value store
     * 
     * @return the value store
     */
    ValueStore getValueStore();

    /**
     * Returns the entity wrapper value object.
     */
    Entity getValueObject();

    /**
     * Returns the entity wrapper value list object.
     */
    List<? extends Entity> getValueListObject();

    /**
     * Returns the entity wrapper value object at data index.
     */
    Entity getValueObjectAtDataIndex();

    /**
     * Returns true if wrapper is backed by indexed data.
     */
    boolean isIndexed();
    
    /**
     * Returns the wrapper data index.
     */
    int getDataIndex();

    /**
     * Sets the wrapper data index.
     * 
     * @param dataIndex
     *                  the data index to set
     */
    void setDataIndex(int dataIndex);

    /**
     * Returns the storage size for this wrapper
     * 
     * @return the storage size
     */
    int size();

    /**
     * Compares between wrapper.
     * 
     * @param source
     *               the source wrapper
     * @return Zero is same otherwise non-zero
     * @throws UnifyException
     *                        if an error occurs
     */
    int compare(EntityWrapper source) throws UnifyException;

    /**
     * Compares between wrapper.
     * 
     * @param source
     *                            the source wrapper
     * @param inclusionFieldNames
     *                            the fields to include in difference check.
     * @return Zero is same otherwise non-zero
     * @throws UnifyException
     *                        if an error occurs
     */
    int compare(EntityWrapper source, String... inclusionFieldNames) throws UnifyException;

    /**
     * Compares between wrapper.
     * 
     * @param source
     *                            the source wrapper
     * @param inclusionFieldNames
     *                            the fields to include in difference check.
     * @return Zero is same otherwise non-zero
     * @throws UnifyException
     *                        if an error occurs
     */
    int compare(EntityWrapper source, Collection<String> inclusionFieldNames) throws UnifyException;

    /**
     * Compares between wrapper.
     * 
     * @param source
     *                         source wrapper
     * @param inclusionMapping
     *                         the fields to include in difference check.
     * @return Zero is same otherwise non-zero
     * @throws UnifyException
     *                        if an error occurs
     */
    int compare(EntityWrapper source, Map<String, String> inclusionMapping) throws UnifyException;

    /**
     * Copies supplied entity wrapper to this object.
     * 
     * @param source
     *               the source entity wrapper
     * @throws UnifyException
     *                        if an error occurs
     */
    void copy(EntityWrapper source) throws UnifyException;

    /**
     * Copies supplied entity wrapper to this object with exclusions.
     * 
     * @param source
     *                            the source entity wrapper
     * @param exclusionFieldNames
     *                            the fields to exclude from copy.
     * @throws UnifyException
     *                        if an error occurs
     */
    void copyWithExclusions(EntityWrapper source, String... exclusionFieldNames) throws UnifyException;

    /**
     * Copies supplied entity wrapper to this object with inclusions.
     * 
     * @param source
     *                            the source entity wrapper
     * @param inclusionFieldNames
     *                            the fields to include in copy.
     * @throws UnifyException
     *                        if an error occurs
     */
    void copyWithInclusions(EntityWrapper source, String... inclusionFieldNames) throws UnifyException;

    /**
     * Copies supplied entity wrapper to this object with exclusions.
     * 
     * @param source
     *                            the source entity wrapper
     * @param exclusionFieldNames
     *                            the fields to exclude from copy.
     * @throws UnifyException
     *                        if an error occurs
     */
    void copyWithExclusions(EntityWrapper source, Collection<String> exclusionFieldNames) throws UnifyException;

    /**
     * Copies supplied entity wrapper to this object with inclusions.
     * 
     * @param source
     *                            the source entity wrapper
     * @param inclusionFieldNames
     *                            the fields to include in copy.
     * @throws UnifyException
     *                        if an error occurs
     */
    void copyWithInclusions(EntityWrapper source, Collection<String> inclusionFieldNames) throws UnifyException;

}
