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
package com.flowcentraltech.flowcentral.connect.configuration.xml.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLInputFactory;
import javax.xml.transform.sax.SAXSource;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.flowcentraltech.flowcentral.connect.configuration.xml.ApplicationConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.InterconnectAppConfig;
import com.flowcentraltech.flowcentral.connect.configuration.xml.InterconnectConfig;

/**
 * XML utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class XmlUtils {

//    private static final Logger LOGGER = Logger.getLogger(XmlUtils.class.getName());

    private XmlUtils() {

    }

    public static List<ApplicationConfig> readInterconnectConfig(String resourceName) throws Exception {
//        LOGGER.log(Level.INFO, "Reading interconnect configuration...");
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

//                LOGGER.log(Level.INFO, "[{0}] application interconnect configuration read.", resultList.size());
            }
        }

        return resultList;
    }

    @SuppressWarnings("unchecked")
    private static <T> T readConfig(Class<T> clazz, String resourceName) throws Exception {
        T config = null;
        File file = new File(resourceName);
        try (InputStream in = file.exists() ? new FileInputStream(file)
                : XmlUtils.class.getClassLoader().getResourceAsStream(XmlUtils.conformJarSeparator(resourceName));) {
//            LOGGER.log(Level.INFO, "Reading interconnect resource : [{0}]", resourceName);

            JAXBContext jaxbContext = JAXBContext.newInstance(clazz);
            Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
            XMLInputFactory xmlInputFactory = XMLInputFactory.newFactory();
            xmlInputFactory.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            xmlInputFactory.setProperty(XMLInputFactory.IS_SUPPORTING_EXTERNAL_ENTITIES, false);
            jaxbUnmarshaller.setEventHandler(new ValidationEventHandler()
                {
                    @Override
                    public boolean handleEvent(ValidationEvent event) {
                        return false;
                    }
                });

            XMLReader xmlReader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            // Disable JAXB DTD validation
//            xmlReader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
//            xmlReader.setFeature("http://xml.org/sax/features/validation", false);
            config = (T) jaxbUnmarshaller.unmarshal(new SAXSource(xmlReader, new InputSource(in)));
        } catch (Exception e) {
//            LOGGER.log(Level.SEVERE, "Error reading interconnect configuration", e);
            throw e;
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
