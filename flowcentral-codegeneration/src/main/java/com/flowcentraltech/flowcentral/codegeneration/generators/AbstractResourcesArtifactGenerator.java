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

/**
 * Convenient abstract base class for resources artifact generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractResourcesArtifactGenerator extends AbstractStaticModuleArtifactGenerator {

    private static final String RESOURCES_FOLDER = "src/main/resources/";

    protected AbstractResourcesArtifactGenerator(String artDir) {
        super(RESOURCES_FOLDER + artDir, artDir);
    }

    protected AbstractResourcesArtifactGenerator() {
        super(RESOURCES_FOLDER, null);
    }

}
