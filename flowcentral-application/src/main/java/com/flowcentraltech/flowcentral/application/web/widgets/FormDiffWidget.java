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

package com.flowcentraltech.flowcentral.application.web.widgets;

import com.flowcentraltech.flowcentral.application.data.Diff;
import com.flowcentraltech.flowcentral.common.web.widgets.AbstractFlowCentralControl;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Form diff widget.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
@Component("fc-formdiff")
public class FormDiffWidget extends AbstractFlowCentralControl {

    public Diff getDiff() throws UnifyException {
        return getValue(Diff.class);
    }
}
