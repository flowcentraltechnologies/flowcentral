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
package com.flowcentraltech.flowcentral.integration.endpoint.reader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.integration.constants.IntegrationModuleErrorConstants;
import com.flowcentraltech.flowcentral.integration.data.ReadConfigDef;
import com.flowcentraltech.flowcentral.integration.endpoint.data.ReadEventInst;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Parameter;
import com.tcdng.unify.core.annotation.Parameters;
import com.tcdng.unify.core.util.IOUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Local folder end-point reader.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(name = "localfolder-endpointreader", description = "$m{endpointreader.localfolder}")
@Parameters({
        @Parameter(name = LocalFolderEndpointReaderConstants.LOCALPATH,
                description = "$m{localfolderendpointreader.localpath}", editor = "!ui-text size:48", mandatory = true),
        @Parameter(name = LocalFolderEndpointReaderConstants.SUCCESSPATH,
                description = "$m{localfolderendpointreader.successpath}", editor = "!ui-text size:48"),
        @Parameter(name = LocalFolderEndpointReaderConstants.ERRORPATH,
                description = "$m{localfolderendpointreader.errorpath}", editor = "!ui-text size:48") })
public class LocalFolderEndpointReader extends AbstractEndpointReader {

    private List<File> files;

    private String localPath;

    private String successPath;

    private String errorPath;

    private int index;

    @Override
    public void setup(ReadConfigDef readConfigDef) throws UnifyException {
        Map<String, Object> parameters = readConfigDef.getReaderParamsDef().getValueMap();
        localPath = (String) parameters.get(LocalFolderEndpointReaderConstants.LOCALPATH);
        successPath = (String) parameters.get(LocalFolderEndpointReaderConstants.SUCCESSPATH);
        errorPath = (String) parameters.get(LocalFolderEndpointReaderConstants.ERRORPATH);
    }

    @Override
    public void beginWatch() throws UnifyException {
        File folder = new File(localPath);
        if (!folder.isDirectory()) {
            throw new UnifyException(IntegrationModuleErrorConstants.LOCALFILE_TRANSPORTREADER_UNKNOWNFOLDER, getName(),
                    localPath);
        }

        files = new ArrayList<File>();
        for (File file : folder.listFiles()) {
            if (file.isFile() && file.canWrite()) {
                files.add(file);
            }
        }

        index = -1;
    }

    @Override
    public boolean next() throws UnifyException {
        if (index >= 0) {
            if (!files.get(index).delete()) {
                // We can have duplicates later if files are not deleted
                // TODO warning or error?
            }
        }

        index++;
        if (index < files.size()) {
            return true;
        }

        return false;
    }

    @Override
    public ReadEventInst getEvent() throws UnifyException {
        File file = files.get(index);
        byte[] data = IOUtils.readAll(file);
        return new ReadEventInst().addEventMessage(file.getName(), data);
    }

    @Override
    public void endWatch() throws UnifyException {
        files = null;
    }

    @Override
    public void housekeepWatch(ReadEventInst event, EndpointReadEventStatus status) throws UnifyException {
        String housekeepPath = null;
        if (EndpointReadEventStatus.SUCCESSFUL.equals(status)) {
            housekeepPath = successPath;
        } else if (EndpointReadEventStatus.FAILED.equals(status)) {
            housekeepPath = errorPath;
        }

        if (!StringUtils.isBlank(housekeepPath)) {
            File folder = new File(housekeepPath);
            if (!folder.isDirectory()) {
                folder.mkdirs();
            }

            for (ReadEventInst.EventMessage eventMessage : event.getEventMessages()) {
                IOUtils.writeToFile(new File(folder, eventMessage.getFileName()), eventMessage.getFile());
            }
        }
    }

}
