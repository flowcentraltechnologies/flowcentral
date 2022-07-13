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

package com.flowcentraltech.flowcentral.application.data;

import java.util.Date;

/**
 * Comment.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Comment {

    private String message;
    
    private String commentBy;
    
    private String action;
    
    private Date timestamp;

    public Comment(String message, String commentBy, String action, Date timestamp) {
        this.message = message;
        this.commentBy = commentBy;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getCommentBy() {
        return commentBy;
    }

    public String getAction() {
        return action;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
