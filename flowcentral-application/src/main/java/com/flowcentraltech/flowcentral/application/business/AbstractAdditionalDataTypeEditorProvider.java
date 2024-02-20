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
package com.flowcentraltech.flowcentral.application.business;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.DataType;
import com.tcdng.unify.core.data.Listable;

/**
 * Convenient abstract base class for additional data type editor provider. 
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractAdditionalDataTypeEditorProvider extends AbstractFlowCentralComponent
        implements AdditionalDataTypeEditorProvider {

    @Override
    public final List<? extends Listable> provide(Locale locale, DataType dataType) throws UnifyException {
        List<? extends Listable> list = doProvide(locale, dataType);
        return list != null ? list : Collections.emptyList();
    }

    /**
     * Do provide additional data type editors.
     * 
     * @param locale
     *                 the locale
     * @param dataType
     *                 the data type
     * @return list of editors
     * @throws UnifyException
     *                        if an error occurs
     */
    protected abstract List<? extends Listable> doProvide(Locale locale, DataType dataType) throws UnifyException;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

}
