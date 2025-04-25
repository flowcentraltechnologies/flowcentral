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

package com.flowcentraltech.flowcentral.codegeneration.generators;

import java.util.zip.ZipOutputStream;

import com.flowcentraltech.flowcentral.common.FlowCentralComponent;
import com.tcdng.unify.core.UnifyException;

/**
 * Static artifact generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface StaticArtifactGenerator extends FlowCentralComponent {

    /**
     * Generates static artifact.
     * 
     * @param ctx
     *                   the context
     * @param zos
     *                   the zip output stream
     * @throws UnifyException
     *                        if an error occurs
     */
    void generate(ExtensionStaticFileBuilderContext ctx, ZipOutputStream zos) throws UnifyException;
}
