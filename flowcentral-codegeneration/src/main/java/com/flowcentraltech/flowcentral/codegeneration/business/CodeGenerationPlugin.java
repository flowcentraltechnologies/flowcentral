/*
 * Copyright (c) 2021-2026 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.codegeneration.business;

import java.util.List;

import com.tcdng.unify.core.UnifyComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Implementation of code generation plugin.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface CodeGenerationPlugin extends UnifyComponent {

    /**
     * Gets release Java version.
     * 
     * @return the Java version
     * @throws UnifyException
     *                        if an error occurs
     */
    String getReleaseJavaVersion() throws UnifyException;
    
    /**
     * Gets additional utilities exclusion modules.
     * 
     * @return the list of modules
     * @throws UnifyException
     *                        if an error occurs
     */
    List<String> getAdditionalUtilitiesExclusionModules() throws UnifyException;

    /**
     * Gets the generated utilities JAR file name
     * 
     * @return the file name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getUtilitiesJarFileName() throws UnifyException;

    /**
     * Gets the generated utilities JAR POM file
     * 
     * @return the POM
     * @throws UnifyException
     *                        if an error occurs
     */
    String getUtilitiesJarPOM() throws UnifyException;

    /**
     * Gets the generated extension JAR file name
     * 
     * @return the file name
     * @throws UnifyException
     *                        if an error occurs
     */
    String getExtensionJarFileName() throws UnifyException;

    /**
     * Gets the generated extension JAR POM file
     * 
     * @return the POM
     * @throws UnifyException
     *                        if an error occurs
     */
    String getExtensionJarPOM() throws UnifyException;
}
