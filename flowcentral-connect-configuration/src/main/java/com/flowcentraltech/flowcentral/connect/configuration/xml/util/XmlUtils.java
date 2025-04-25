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
package com.flowcentraltech.flowcentral.connect.configuration.xml.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.flowcentraltech.flowcentral.connect.configuration.xml.ApplicationConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.InterconnectAppConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.InterconnectConfig;

/**
 * XML utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class XmlUtils {

    private XmlUtils() {

    }

    public static List<ApplicationConfig> readInterconnectConfig(String resourceName) throws Exception {
        List<ApplicationConfig> resultList = new ArrayList<ApplicationConfig>();
        InterconnectConfig interconnectConfig = XmlUtils.readConfig(InterconnectConfig.class, resourceName);
        
        // Root application
        if (interconnectConfig.getEntitiesConfig() != null) {
            ApplicationConfig applicationConfig = new ApplicationConfig();
            applicationConfig.setName(interconnectConfig.getName());
            applicationConfig.setDescription(interconnectConfig.getDescription());
            applicationConfig.setDataSourceAlias(interconnectConfig.getDataSourceAlias());
            applicationConfig.setEntitiesConfig(interconnectConfig.getEntitiesConfig());
            applicationConfig.setEntityManagerFactory(interconnectConfig.getEntityManagerFactory());
            applicationConfig.setRedirect(interconnectConfig.getRedirect());
            resultList.add(applicationConfig);
        }
        
        // Applications in external files
        if (interconnectConfig.getInterconnectAppConfigs() != null) {
            final String applicationName = interconnectConfig.getInterconnectAppConfigs().getApplication();
            final List<InterconnectAppConfig> appConfigList = interconnectConfig.getInterconnectAppConfigs()
                    .getAppConfigList();
            if (appConfigList != null && !appConfigList.isEmpty()) {
                for (InterconnectAppConfig interconnectAppConfig : appConfigList) {
                    ApplicationConfig applicationConfig = XmlUtils.readConfig(ApplicationConfig.class,
                            interconnectAppConfig.getConfigFile());
                    applicationConfig.setRedirect(interconnectConfig.getRedirect());
                    if (applicationName != null && !applicationName.isEmpty()) {
                        applicationConfig.setName(applicationName);
                    }
                    
                    resultList.add(applicationConfig);
                }
            }
        }

        return resultList;
    }

    private static <T> T readConfig(Class<T> clazz, String resourceName) throws Exception {
        T config = null;
        File file = new File(resourceName);
        try (InputStream in = file.exists() ? new FileInputStream(file)
                : XmlUtils.class.getClassLoader().getResourceAsStream(XmlUtils.conformJarSeparator(resourceName));) {
            XmlMapper unmarshaller = new XmlMapper();
            config = unmarshaller.readValue(in, clazz);
        }

        return config;
    }

    private static String conformJarSeparator(String filename) {
        filename = XmlUtils.conform("/", filename);
        if (filename.startsWith("/")) {
            return filename.substring("/".length());
        }
        return filename;
    }

    private static String conform(String separator, String name) {
        if (name == null) {
            return "";
        }

        if (separator.equals("\\")) {
            return name.replace('/', '\\');
        }
        return name.replace('\\', '/');
    }

}
