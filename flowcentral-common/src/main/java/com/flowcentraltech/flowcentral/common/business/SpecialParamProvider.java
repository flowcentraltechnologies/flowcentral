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

package com.flowcentraltech.flowcentral.common.business;

import java.util.List;

import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.ParameterizedStringGenerator;
import com.tcdng.unify.core.data.ValueStore;

/**
 * Special parameter provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface SpecialParamProvider extends UnifyComponent {
    
    /**
     * Gets a generator instance.
     * 
     * @param paramValueStore
     *                        the parameter value store
     * @param tokenList
     *                        the token list
     * @return the generator instance
     * @throws UnifyException
     *                        if an error occurs
     */
    ParameterizedStringGenerator getStringGenerator(ValueStore paramValueStore, List<StringToken> tokenList)
            throws UnifyException;
    
    /**
     * Gets a generator instance.
     * 
     * @param paramValueStore
     *                            the parameter value store
     * @param generatorValueStore
     *                            the generator value store
     * @param tokenList
     *                            the token list
     * @return the generator instance
     * @throws UnifyException
     *                        if an error occurs
     */
    ParameterizedStringGenerator getStringGenerator(ValueStore paramValueStore, ValueStore generatorValueStore,
            List<StringToken> tokenList) throws UnifyException;

    /**
     * Resolves a special parameter.
     * 
     * @param param
     *              the parameter to resolve
     * @return the resolved parameter otherwise null
     * @throws UnifyException
     *                        if an error occurs
     */
    Object resolveSpecialParameter(String param) throws UnifyException;
}
