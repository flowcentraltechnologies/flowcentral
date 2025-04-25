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
package com.flowcentraltech.flowcentral.messaging.data;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Messaging execution context.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class MessagingExecContext {

    private ExecutorService pool;

    private String config;

    private String target;

    private String component;

    private int maxLoading;

    private int running;

    public MessagingExecContext(String config, String target, String component, int maxLoading) {
        this.config = config;
        this.target = target;
        this.component = component;
        this.maxLoading = maxLoading;
        this.pool = Executors.newFixedThreadPool(maxLoading);
        this.running = 0;
    }

    public ExecutorService getPool() {
        return pool;
    }

    public String getConfig() {
        return config;
    }

    public String getTarget() {
        return target;
    }

    public String getComponent() {
        return component;
    }

    public int getMaxLoading() {
        return maxLoading;
    }

    public int getRunning() {
        return running;
    }

    public int getLoadingAvailable() {
        return maxLoading - running;
    }

    public boolean isLoadingEmpty() {
        return running == 0;
    }

    public synchronized boolean incLoading() {
        if (running < maxLoading) {
            running++;
            return true;
        }

        return false;
    }

    public synchronized boolean decLoading() {
        if (running == 0) {
            return false;
        }

        running--;
        return true;
    }
}
