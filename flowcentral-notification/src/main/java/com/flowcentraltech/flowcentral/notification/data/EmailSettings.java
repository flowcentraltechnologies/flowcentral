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
package com.flowcentraltech.flowcentral.notification.data;

/**
 * Email settings.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EmailSettings {
    
    private String senderName;
    
    private String senderEmail;
    
    private int messagesPerMinute;
    
    private String smtpHostAddress;
    
    private String smtpUsername;
    
    private String smtpPassword;
    
    private String smtpSecurityType;
   
    private int smtpHostPort;
    
    private int maxBatchSize;
    
    private int maxRetries;
    
    private int retriesInMinutes;

    public EmailSettings(String senderName, String senderEmail, int messagesPerMinute, String smtpHostAddress,
            String smtpUsername, String smtpPassword, String smtpSecurityType, int smtpHostPort, int maxBatchSize,
            int maxRetries, int retriesInMinutes) {
        this.senderName = senderName;
        this.senderEmail = senderEmail;
        this.messagesPerMinute = messagesPerMinute;
        this.smtpHostAddress = smtpHostAddress;
        this.smtpUsername = smtpUsername;
        this.smtpPassword = smtpPassword;
        this.smtpSecurityType = smtpSecurityType;
        this.smtpHostPort = smtpHostPort;
        this.maxBatchSize = maxBatchSize;
        this.maxRetries = maxRetries;
        this.retriesInMinutes = retriesInMinutes;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public int getMessagesPerMinute() {
        return messagesPerMinute;
    }

    public String getSmtpHostAddress() {
        return smtpHostAddress;
    }

    public String getSmtpUsername() {
        return smtpUsername;
    }

    public String getSmtpPassword() {
        return smtpPassword;
    }

    public String getSmtpSecurityType() {
        return smtpSecurityType;
    }

    public int getSmtpHostPort() {
        return smtpHostPort;
    }

    public int getMaxBatchSize() {
        return maxBatchSize;
    }

    public int getMaxRetries() {
        return maxRetries;
    }

    public int getRetriesInMinutes() {
        return retriesInMinutes;
    }
}
