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
package com.flowcentraltech.flowcentral.messaging.data;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Message.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Message {

    private MessageHeader header;

    private String body;

    private boolean sendHeaderless;

    private Message(MessageHeader header, String body, boolean sendHeaderless) {
        this.header = header;
        this.body = body;
        this.sendHeaderless = sendHeaderless;
    }

    public MessageHeader getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }

    public boolean isSendHeaderless() {
        return sendHeaderless;
    }

    public static Builder newBuilder(String config, String destination) {
        return new Builder(config, destination);
    }

    public static class Builder {

        private String config;

        private String destination;

        private String handler;

        private String body;

        private boolean sendHeaderless;

        public Builder(String config, String destination) {
            this.config = config;
            this.destination = destination;
        }

        public Builder() {

        }

        public Builder config(String config) {
            this.config = config;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder handler(String handler) {
            this.handler = handler;
            return this;
        }

        public Builder body(String body) {
            this.body = body;
            return this;
        }

        public Builder sendHeaderless(boolean sendHeaderless) {
            this.sendHeaderless = sendHeaderless;
            return this;
        }

        public Message build() {
            if (StringUtils.isBlank(config)) {
                throw new IllegalArgumentException("Configuration is required.");
            }

            if (StringUtils.isBlank(destination)) {
                throw new IllegalArgumentException("Message destination is required.");
            }

            if (StringUtils.isBlank(body)) {
                throw new IllegalArgumentException("Message body is required.");
            }

            return new Message(new MessageHeader(config, destination, handler), body, sendHeaderless);
        }
    }
}
