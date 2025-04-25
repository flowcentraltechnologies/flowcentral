/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

import java.util.Date;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.flowcentraltech.flowcentral.common.data.SequenceDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Sequence code generator
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface SequenceCodeGenerator extends FlowCentralComponent {

    /**
     * Gets the skeleton string for the supplied the sequence definition;
     * 
     * @param sequenceDefintion
     *                          the sequence definition
     * @return the sequence skeleton.
     * @throws UnifyException
     *                        if an error occurs
     */
    String getCodeSkeleton(String sequenceDefintion) throws UnifyException;

    /**
     * Gets sequence definition.
     * 
     * @param sequenceDefintion the definition string
     * @return the definition object
     * @throws UnifyException if an error occurs
     */
    SequenceDef getSequenceDefinition(String sequenceDefintion) throws UnifyException;
    
    /**
     * Gets the next sequence code for the supplied sequence definition and current
     * date.
     * 
     * @param ownerId
     *                          the sequence owner Id
     * @param sequenceDefintion
     *                          the sequence definition
     * @param valueStoreReader
     *                          the value store reader (optional)
     * @return the next sequence code
     * @throws UnifyException
     *                        if an error occurs
     */
    String getNextSequenceCode(String ownerId, String sequenceDefintion, ValueStoreReader valueStoreReader)
            throws UnifyException;

    /**
     * Gets the next sequence code for the supplied sequence definition and date.
     * 
     * @param ownerId the sequence owner Id
     * @param sequenceDefintion
     *                          the sequence definition
     * @param date
     *                          the sequence date
     * @param valueStoreReader
     *                          the value store reader (optional)
     * @return the next sequence code
     * @throws UnifyException
     *                        if an error occurs
     */
    String getNextSequenceCode(String ownerId, String sequenceDefintion, Date date, ValueStoreReader valueStoreReader)
            throws UnifyException;
}
