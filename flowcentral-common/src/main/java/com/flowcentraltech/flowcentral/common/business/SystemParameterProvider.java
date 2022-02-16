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

/**
 * System parameter provider.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public interface SystemParameterProvider extends UnifyComponent {

    /**
     * Gets a system parameter.
     * 
     * @param dataType
     *                 the result type
     * @param code
     *                 the system parameter code
     * @return the system parameter value
     * @throws UnifyException
     *                        if system parameter with code doesn't exists
     */
    <T> T getSysParameterValue(Class<T> dataType, String code) throws UnifyException;
}
