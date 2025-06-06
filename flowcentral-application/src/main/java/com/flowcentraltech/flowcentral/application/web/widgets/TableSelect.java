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

package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.List;

import com.tcdng.unify.core.UnifyException;

/**
 * Table select interface.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface TableSelect<T> {

    /**
     * Gets all selected items.
     * 
     * @return list of selected items
     * @throws UnifyException
     *                        if an error occurs
     */
    List<T> getSelectedItems() throws UnifyException;

    /**
     * Checks if row is selected
     * 
     * @param rowIndex
     *                 the row index
     * @return true if selected otherwise false
     * @throws UnifyException
     *                        if an error occurs
     */
    boolean isRowSelected(int rowIndex) throws UnifyException;

    /**
     * Sets row is selected
     * 
     * @param rowIndex
     *                 the row index
     * @param selected
     *                 the row selected flag
     * @throws UnifyException
     *                        if an error occurs
     */
    void setRowSelected(int rowIndex, boolean selected) throws UnifyException;
}
