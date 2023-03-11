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

package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.web.widgets.InputArrayEntries;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.web.ui.widget.AbstractPanel;

/**
 * Emails panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-emailspanel")
@UplBinding("web/application/upl/emailspanel.upl")
public class EmailsPanel extends AbstractPanel implements FormPanel {

    @Override
    public List<FormValidationErrors> validate(EvaluationMode evaluationMode) throws UnifyException {
        InputArrayEntries entries = getValue(InputArrayEntries.class);
        if (entries != null) {
            if (!entries.validate()) {
                FormValidationErrors errors = new FormValidationErrors();
                errors.addValidationError(resolveSessionMessage("$m{emailspanel.email.invalid}"));
                return Arrays.asList(errors);
            }
        }
        
        return Collections.emptyList();
    }

}
