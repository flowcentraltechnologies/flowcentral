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

import java.text.Format;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.tcdng.unify.common.util.ParamToken;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.common.util.StringTokenUtils;
import com.tcdng.unify.convert.FormatContext;

/**
 * Connect string utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ConnectStringUtil {

    private ConnectStringUtil() {

    }
    
    public static String applyBeanToTemplate(String template, Object bean) throws Exception {
        List<StringToken> _template = StringTokenUtils.breakdownParameterizedString(template);
        return ConnectStringUtil.applyBeanToTemplate(_template, bean);
    }
    
    public static String applyBeanToTemplate(List<StringToken> template, Object bean) throws Exception {
        StringBuilder sb = new StringBuilder();
        if (template != null && !template.isEmpty()) {
            FormatContext ctx = new FormatContext();
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
}
