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
package com.flowcentraltech.flowcentral.application.business;

import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.DataType;

/**
 * Additional data type editor provider.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface AdditionalDataTypeEditorProvider extends FlowCentralComponent {

    /**
     * Provides additional data type editors.
     * 
     * @param locale
     *                 the locale
     * @param dataType
     *                 the data type
     * @return list of editors
     * @throws UnifyException
     *                        if an error occurs
     */
    List<? extends Listable> provide(Locale locale, DataType dataType) throws UnifyException;

}
