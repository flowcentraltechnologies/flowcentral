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
package com.flowcentraltech.flowcentral.connect.common.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Executor utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class ExecutorUtils {
    
    private static final int MIN_POOL_THREADS = 32;
    
    private static ExecutorUtils inst;
    
    private ScheduledExecutorService executor;
    
    private ExecutorUtils() {
        this.executor = Executors.newScheduledThreadPool(MIN_POOL_THREADS);
    }
    
    public static ExecutorUtils getInstance() {
        if (inst == null) {
            synchronized(ExecutorUtils.class)  {
                if (inst == null) {
                    inst = new ExecutorUtils();
                }            
            }            
        }
        
        return inst;
    }
    
    public void executeDelayed(long delayInMillisec, Runnable runnable) {
        executor.schedule(runnable, delayInMillisec, TimeUnit.MILLISECONDS);
    }
 }
