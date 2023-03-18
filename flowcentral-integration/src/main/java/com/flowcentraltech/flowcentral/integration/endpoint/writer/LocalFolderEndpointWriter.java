/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.integration.endpoint.writer;

import java.io.File;
import java.util.Map;

import com.flowcentraltech.flowcentral.integration.data.WriteConfigDef;
import com.flowcentraltech.flowcentral.integration.endpoint.data.EventMessage;
import com.flowcentraltech.flowcentral.integration.endpoint.data.WriteEventInst;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;
import com.tcdng.unify.core.util.IOUtils;

/**
 * Local folder end-point writer.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = "localfolder-endpointwriter", description = "$m{endpointwriter.localfolder}")
@Parameters({ @Parameter(name = LocalFolderEndpointWriterConstants.LOCALPATH,
        description = "$m{localfolderendpointwriter.localpath}", editor = "!ui-text size:48", mandatory = true) })
public class LocalFolderEndpointWriter extends AbstractEndpointWriter {

    private String localPath;

    @Override
    public void setup(WriteConfigDef writeConfigDef) throws UnifyException {
        Map<String, Object> parameters = writeConfigDef.getWriterParamsDef().getValueMap();
        localPath = (String) parameters.get(LocalFolderEndpointWriterConstants.LOCALPATH);
    }

    @Override
    public void beginWatch() throws UnifyException {
        File folder = new File(localPath);
        if (!folder.isDirectory()) {
            folder.mkdirs();
        }
    }

    @Override
    public boolean setEvent(WriteEventInst writeEventInst) throws UnifyException {
        for (EventMessage eventMessage : writeEventInst.getEventMessages()) {
            File folder = new File(localPath);
            if (eventMessage.isText()) {
                IOUtils.writeToFile(new File(folder, eventMessage.getFileName()), eventMessage.getText().getBytes());
            } else {
                IOUtils.writeToFile(new File(folder, eventMessage.getFileName()), eventMessage.getFile());
            }
        }
        return true;
    }

    @Override
    public void endWatch() throws UnifyException {

    }

}
