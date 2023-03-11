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

package com.flowcentraltech.flowcentral.security.business;

import com.flowcentraltech.flowcentral.application.business.CommentUserPhotoGenerator;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.Comment;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;

/**
 * Comment user photo generator implementation.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ApplicationModuleNameConstants.COMMENT_USER_PHOTO_GENERATOR)
public class CommentUserPhotoGeneratorImpl extends AbstractUserPhotoGenerator implements CommentUserPhotoGenerator {

    private Comment comment;

    @Override
    public void setComment(Comment comment) throws UnifyException {
        this.comment = comment;
    }

    @Override
    protected String getUserLoginId() throws UnifyException {
        return comment != null ? comment.getCommentBy() : null;
    }

}
