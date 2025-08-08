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
package com.flowcentraltech.flowcentral.messaging.os.data;


/**
 * OS messaging header.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class OSMessagingHeader {

    private String target;

    private String source;

    private String processor;

    private String versionNo;

    public OSMessagingHeader(String target, String source, String processor, String versionNo) {
        this.target = target;
        this.source = source;
        this.processor = processor;
        this.versionNo = versionNo;
    }

    public String getTarget() {
        return target;
    }

    public String getSource() {
        return source;
    }

    public String getProcessor() {
        return processor;
    }

    public String getVersionNo() {
        return versionNo;
    }

}
