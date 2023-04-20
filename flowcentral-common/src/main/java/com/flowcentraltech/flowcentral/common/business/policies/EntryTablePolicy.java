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

package com.flowcentraltech.flowcentral.common.business.policies;

import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.common.constants.EntryActionType;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.constants.TableChangeType;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.data.RowChangeInfo;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Entry table policy.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface EntryTablePolicy extends UnifyComponent {

    /**
     * Validate entries,
     * 
     * @param evaluationMode
     *                        the evaluation mode.
     * 
     * @param parentReader
     *                        optional parent value store reader
     * @param tableValueStore
     *                        the list data value store object
     * @param errors
     *                        errors
     * @throws UnifyException
     *                        if an error occurs
     */
    void validateEntries(EvaluationMode evaluationMode, ValueStoreReader parentReader, ValueStore tableValueStore,
            FormValidationErrors errors) throws UnifyException;

    /**
     * Handles on entry table data load.
     * 
     * @param parentReader
     *                        optional parent value store reader
     * @param tableValueStore
     *                        the list data value store object
     * @param selected
     *                        selected item index
     * @throws UnifyException
     *                        if an error occurs
     */
    void onEntryTableLoad(ValueStoreReader parentReader, ValueStore tableValueStore, Set<Integer> selected)
            throws UnifyException;

    /**
     * Handles on entry table data change.
     * 
     * @param parentReader
     *                        optional parent value store reader
     * @param tableValueStore
     *                        the list data value store object
     * @param selected
     *                        selected item index
     * @return the entry action type
     * @throws UnifyException
     *                        if an error occurs
     */
    EntryActionType onEntryTableChange(ValueStoreReader parentReader, ValueStore tableValueStore, Set<Integer> selected,
            TableChangeType changeType) throws UnifyException;

    /**
     * Handles on entry table data change.
     * 
     * @param parentReader
     *                        optional parent value store reader
     * @param tableValueStore
     *                        the list data store object
     * @param rowChangeInfo
     *                        the row change information
     * @return the entry action type
     * @throws UnifyException
     *                        if an error occurs
     */
    EntryActionType onEntryRowChange(ValueStoreReader parentReader, ValueStore tableValueStore,
            RowChangeInfo rowChangeInfo) throws UnifyException;

    /**
     * Gets pre-table summary lines.
     * 
     * @param parentReader
     *                        optional parent value store reader
     * @param tableValueStore
     *                        the list data store object
     * @return the pre-table summary lines
     * @throws UnifyException
     *                        if an error occurs
     */
    List<TableSummaryLine> getPreTableSummaryLines(ValueStoreReader parentReader, ValueStore tableValueStore)
            throws UnifyException;

    /**
     * Gets post table summary lines.
     * 
     * @param parentReader
     *                        optional parent value store reader
     * @param tableValueStore
     *                        the list data store object
     * @return the post table summary lines
     * @throws UnifyException
     *                        if an error occurs
     */
    List<TableSummaryLine> getPostTableSummaryLines(ValueStoreReader parentReader, ValueStore tableValueStore)
            throws UnifyException;

    /**
     * Apply table state overrides.
     * 
     * @param parentReader
     *                           optional parent value store reader
     * @param rowValueStore
     *                           the value store for the current row item
     * @param tableStateOverride
     *                           the table state override
     * @throws UnifyException
     *                        if an error occurs
     */
    void applyTableStateOverride(ValueStoreReader parentReader, ValueStore rowValueStore,
            TableStateOverride tableStateOverride) throws UnifyException;

    /**
     * Applies a fixed action to a record.
     * 
     * @param parentReader
     *                        optional parent value store reader
     * @param valueStore
     *                        the value store
     * @param index
     *                        the value store index
     * @param fixedActionType
     *                        the action type to apply
     * @throws UnifyException
     *                        if an error occurs
     */
    void applyFixedAction(ValueStoreReader parentReader, ValueStore valueStore, int index,
            FixedRowActionType fixedActionType) throws UnifyException;

    /**
     * Resolves the table fixed index for the supplied value store.
     * 
     * @param parentReader
     *                     optional parent value store reader
     * @param valueStore
     *                     the value store
     * @param index
     *                     the value store index
     * @param size
     *                     the number of records in entry table
     * @return the action index
     * @throws UnifyException
     *                        if an error occurs
     */
    FixedRowActionType resolveFixedIndex(ValueStoreReader parentReader, ValueStore valueStore, int index, int size)
            throws UnifyException;

    /**
     * Resolves the table action index for the supplied value store.
     * 
     * @param parentReader
     *                     optional parent value store reader
     * @param valueStore
     *                     the value store
     * @param index
     *                     the value store index
     * @param size
     *                     the number of records in entry table
     * @return the action index
     * @throws UnifyException
     *                        if an error occurs
     */
    int resolveActionIndex(ValueStoreReader parentReader, ValueStore valueStore, int index, int size)
            throws UnifyException;
}
