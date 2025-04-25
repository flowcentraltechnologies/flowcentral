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

package com.flowcentraltech.flowcentral.common.data;

import com.tcdng.unify.core.UnifyException;

/**
 * Entity field attributes.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface EntityFieldAttributes {

    String getSuggestionType() throws UnifyException;
    
    String getReferences() throws UnifyException;
    
    int getMinLen() throws UnifyException;

    int getMaxLen() throws UnifyException;
    
    int getPrecision() throws UnifyException;
    
    int getScale() throws UnifyException;
    
    boolean isBranchScoping() throws UnifyException;
    
    boolean isTrim() throws UnifyException;
    
    boolean isAllowNegative() throws UnifyException;
}
