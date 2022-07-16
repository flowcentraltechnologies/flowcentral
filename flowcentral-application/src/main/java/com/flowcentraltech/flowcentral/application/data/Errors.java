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

/**
 * Errors.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Errors {

    private String message;

    private String trace;

    private String document;

    public Errors(String message, String trace, String document) {
        this.message = message;
        this.trace = trace;
        this.document = document;
    }

    public String getMessage() {
        return message;
    }

    public String getTrace() {
        return trace;
    }

    public String getDocument() {
        return document;
    }

}
