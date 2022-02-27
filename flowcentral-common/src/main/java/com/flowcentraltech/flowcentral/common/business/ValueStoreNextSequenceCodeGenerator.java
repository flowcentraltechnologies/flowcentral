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


package com.flowcentraltech.flowcentral.common.business;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ValueStoreReader;

/**
 * Value store next sequence code generator
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ValueStoreNextSequenceCodeGenerator extends UnifyComponent {

    /**
     * Gets next sequence code based on supplied value store reader.
     * 
     * @param valueStoreReader
     *                         the reader to use
     * @return the next sequence code
     * @throws UnifyException
     *                        if an error occurs
     */
    String getNextSequenceCode(ValueStoreReader valueStoreReader) throws UnifyException;
}
