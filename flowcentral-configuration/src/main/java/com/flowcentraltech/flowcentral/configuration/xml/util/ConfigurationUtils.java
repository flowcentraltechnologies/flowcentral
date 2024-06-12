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
package com.flowcentraltech.flowcentral.configuration.xml.util;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import com.flowcentraltech.flowcentral.configuration.xml.AppConfig;
import com.flowcentraltech.flowcentral.configuration.xml.BaseNameConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ModuleConfig;
import com.flowcentraltech.flowcentral.configuration.xml.NotifTemplateConfig;
import com.flowcentraltech.flowcentral.configuration.xml.ReportConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfConfig;
import com.flowcentraltech.flowcentral.configuration.xml.WfWizardConfig;
import com.tcdng.unify.core.UnifyError;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.XmlConfigUtils;

/**
 * Configuration utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class ConfigurationUtils {

    private ConfigurationUtils() {

    }

    public static <T> T readConfig(Class<T> configClass, String fileResource, String workingPath)
            throws UnifyException {
        InputStream inputStream = null;
        try {
            inputStream = IOUtils.openFileResourceInputStream(fileResource, workingPath);
            return XmlConfigUtils.readXmlConfig(configClass, inputStream);
        } finally {
            IOUtils.close(inputStream);
        }
    }

    public static <T> T readConfig(Class<T> configClass, InputStream inputStream)
            throws UnifyException {
        try {
            return XmlConfigUtils.readXmlConfig(configClass, inputStream);
        } finally {
            IOUtils.close(inputStream);
        }
    }

    public static void writeConfig(BaseNameConfig config, OutputStream outputStream) throws UnifyException {
        XmlConfigUtils.writeXmlConfig(config, outputStream);
    }

    public static void writeConfigNoEscape(BaseNameConfig config, OutputStream outputStream) throws UnifyException {
        XmlConfigUtils.writeXmlConfigNoEscape(config, outputStream);
    }
    
    public static String readString(String fileResource, String workingPath) throws UnifyException {
        return IOUtils.readAllLines(fileResource, workingPath);
    }
    
    public static List<String> readStringList(String fileResource, String workingPath)
            throws UnifyException {
        return IOUtils.readFileResourceLines(fileResource, workingPath);
    }
    
    public static List<UnifyError> validateModuleConfig(ModuleConfig moduleConfig) {
        // TODO
        return null;
    }

    public static List<UnifyError> validateApplicationConfig(AppConfig applicationConfig) {
        // TODO
        return null;
    }

    public static List<UnifyError> validateReportConfig(ReportConfig reportConfig) {
        // TODO Auto-generated method stub
        return null;
    }

    public static List<UnifyError> validateNotifTemplateConfig(NotifTemplateConfig notifTemplateConfig) {
        // TODO Auto-generated method stub
        return null;
    }

    public static List<UnifyError> validateWfConfig(WfConfig wfConfig) {
        // TODO Auto-generated method stub
        return null;
    }

    public static List<UnifyError> validateWfWizardConfig(WfWizardConfig wfWizardConfig) {
        // TODO Auto-generated method stub
        return null;
    }

}
