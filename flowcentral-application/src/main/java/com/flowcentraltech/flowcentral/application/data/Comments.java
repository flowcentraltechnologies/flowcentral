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

package com.flowcentraltech.flowcentral.application.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tcdng.unify.core.util.DataUtils;

/**
 * Comments.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Comments {

    private String newCommentCaption;

    private String newComment;

    private List<Comment> oldComments;
    
    public Comments(String newCommentCaption, List<Comment> oldComments) {
        this.newCommentCaption = newCommentCaption;
        this.oldComments = oldComments;
    }

    public String getNewCommentCaption() {
        return newCommentCaption;
    }

    public String getNewComment() {
        return newComment;
    }

    public void setNewComment(String newComment) {
        this.newComment = newComment;
    } 
    
    public List<Comment> getOldComments() {
        return oldComments;
    }

    public static Builder newBuilder() {
        return new Builder();
    }
    
    public static class Builder {

        private String newCommentCaption;
        
        private List<Comment> oldComments;
        
        public Builder() {
            this.oldComments = new ArrayList<Comment>();
        }
        
        public Builder newCommentCaption(String newCommentCaption) {
            this.newCommentCaption = newCommentCaption;
            return this;
        }
        
        public Builder addOldComment(String message, String commentBy, String action, Date timestamp) {
            oldComments.add(new Comment(message, commentBy, action, timestamp));
            return this;
        }
        
        public Comments build() {
            return new Comments(newCommentCaption, DataUtils.unmodifiableList(oldComments));
        }
    }
}
