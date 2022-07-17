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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.List;

import com.flowcentraltech.flowcentral.application.data.Comment;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.UplAttribute;
import com.tcdng.unify.core.annotation.UplAttributes;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.web.ui.widget.AbstractControl;

/**
 * Comment listing.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("fc-commentlisting")
@UplAttributes({ @UplAttribute(name = "userImageSrc", type = String.class, defaultVal = "$t{/images/user_comment.png}"),
        @UplAttribute(name = "timestampFormatter", type = Formatter.class,
                defaultVal = "$d{!datetimeformat style:long}") })
public class CommentListing extends AbstractControl {

    public Formatter<?> getTimestampFormatter() throws UnifyException {
        return getUplAttribute(Formatter.class, "timestampFormatter");
    }
    
    public String getUserImageSrc() throws UnifyException {
        return getUplAttribute(String.class, "userImageSrc");
    }
    
    @SuppressWarnings("unchecked")
    public List<Comment> getComments() throws UnifyException {
        return (List<Comment>) getValue(List.class);
    }
}
