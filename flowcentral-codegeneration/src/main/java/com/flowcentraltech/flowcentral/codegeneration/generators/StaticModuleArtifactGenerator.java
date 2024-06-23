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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Static module artifact generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface StaticModuleArtifactGenerator extends FlowCentralComponent {

    /**
     * Generates static module artifact.
     * 
     * @param ctx
     *                   the module context
     * @param entityName
     *                   the entity name
     * @param zos
     *                   the zip output stream
     * @throws UnifyException
     *                        if an error occurs
     */
    void generate(ExtensionModuleStaticFileBuilderContext ctx, String entityName, ZipOutputStream zos)
            throws UnifyException;
}
