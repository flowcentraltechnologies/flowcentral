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
package com.flowcentraltech.flowcentral.repository.web.lists;

import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.common.constants.RecordStatus;
import com.flowcentraltech.flowcentral.repository.entities.RemoteRepoConfigQuery;
import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.list.ZeroParams;

/**
 * Remote repository configuration list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("remoterepoconfiglist")
public class RemoteRepoConfigListCommand extends AbstractRepositoryListCommand<ZeroParams> {

    public RemoteRepoConfigListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams param) throws UnifyException {
        return repository()
                .findRemoteRepoConfigs((RemoteRepoConfigQuery) new RemoteRepoConfigQuery().status(RecordStatus.ACTIVE));
    }

}
