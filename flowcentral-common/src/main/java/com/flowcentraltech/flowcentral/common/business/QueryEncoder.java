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

package com.flowcentraltech.flowcentral.common.business;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.database.Query;

/**
 * Represents a query encoder
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface QueryEncoder extends FlowCentralComponent {

    /**
     * Encodes a query filter.
     * 
     * @param restriction
     *              the restriction to encode
     * @return the encoded filter
     * @throws UnifyException
     *                        if an error occurs
     */
    String encodeQueryFilter(Restriction restriction) throws UnifyException;

    /**
     * Encodes a query filter.
     * 
     * @param query
     *              the query to encode
     * @return the encoded filter
     * @throws UnifyException
     *                        if an error occurs
     */
    String encodeQueryFilter(Query<? extends Entity> query) throws UnifyException;

    /**
     * Encodes a query order.
     * 
     * @param query
     *              the query to encode
     * @return the encoded order
     * @throws UnifyException
     *                        if an error occurs
     */
    String encodeQueryOrder(Query<? extends Entity> query) throws UnifyException;

    /**
     * Encodes an update object.
     * 
     * @param update
     *               the update to encode
     * @return the encoded update
     * @throws UnifyException
     *                        if an error occurs
     */
    String encodeUpdate(Update update) throws UnifyException;

    /**
     * Decode entity query
     * 
     * @param entity
     *                    the entity long name
     * @param query
     *                    the query definition
     * @param order
     *                    the order definition (optional)
     * @return the decoded query object
     * @throws UnifyException
     *                        if an error occurs
     */
    Query<? extends Entity> decodeQuery(String entity, String query, String order)
            throws UnifyException;
}
