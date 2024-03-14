/*
 * Copyright 2021-2024 FlowCentral Technologies Limited.
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

import java.text.DecimalFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.beanutils.PropertyUtils;

import com.tcdng.unify.common.constants.StandardFormatType;
import com.tcdng.unify.common.util.ParamToken;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.common.util.StringTokenUtils;

/**
 * Executor utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
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
    
    public static String applyBeanToTemplate(String template, Object bean) throws Exception {
        List<StringToken> _template = StringTokenUtils.breakdownParameterizedString(template);
        return ConnectStringUtil.applyBeanToTemplate(_template, bean);
    }
    
    public static String applyBeanToTemplate(List<StringToken> template, Object bean) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (template != null && !template.isEmpty()) {
            TemplateContext ctx = new TemplateContext();
            for (StringToken token : template) {
                Object val = null;
                switch(token.getType()) {
                    case FORMATTED_PARAM:
                        val = PropertyUtils.getProperty(bean, ((ParamToken) token).getParam());
                        if (val != null) {
                            Format format = ctx.getFormat(((ParamToken) token).getFormatType());
                            val = format.format(val);
                        }
                        break;
                    case GENERATOR_PARAM:
                        break;
                    case NEWLINE:
                        val = "\n";
                        break;
                    case PARAM:
                        val = PropertyUtils.getProperty(bean, ((ParamToken) token).getParam());
                        break;
                    case TEXT:
                        val = token.getToken();
                        break;
                    default:
                        break;                  
                }
                
                if (val != null) {
                    sb.append(val);
                }
            }
        }

        return sb.toString();
    }
    
    private static class TemplateContext {
        
        private Map<StandardFormatType, Format> formats;
        
        public TemplateContext() {
            this.formats = new HashMap<StandardFormatType, Format>();
        }
        
        public Format getFormat(StandardFormatType type) {
            Format format = formats.get(type);
            if (format == null) {
                if (type.isDateType()) {
                    format = new SimpleDateFormat(type.format());
                } else {
                    format = new DecimalFormat(type.format());
                }
                formats.put(type, format);
            }

            return format;
        }
    }
}
