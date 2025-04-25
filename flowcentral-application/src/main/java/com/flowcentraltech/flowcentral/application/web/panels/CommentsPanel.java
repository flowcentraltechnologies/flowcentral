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

package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.Comments;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.data.FormValidationErrors;
import com.flowcentraltech.flowcentral.common.web.panels.AbstractFlowCentralPanel;
import com.flowcentraltech.flowcentral.common.web.widgets.Form;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplBinding;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Comments panel.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-commentspanel")
@UplBinding("web/application/upl/commentspanel.upl")
public class CommentsPanel extends AbstractFlowCentralPanel implements FormPanel {

    @Override
    public List<FormValidationErrors> validate(FormValidationContext ctx) throws UnifyException {
        Comments comments = getValue(Comments.class);
        if (comments != null) {
            Form commentsForm = getWidgetByShortName(Form.class, "commentsForm");
            commentsForm.validate(ctx);
            if (StringUtils.isBlank(comments.getNewComment())) {
                commentsForm.addFieldRequiredError("newComment",
                        resolveSessionMessage("$m{commentspanel.comment.add}"));
            }
            
            return Arrays.asList(commentsForm.getFormValidationErrors());
        }
        
        return Collections.emptyList();
    }

}
