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

package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.constants.LinkActConstants;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListData;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.list.AbstractListCommand;
import com.tcdng.unify.core.list.ZeroParams;

/**
 * Link action type list command.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("linkacttypelist")
public class LinkActTypeListCommand extends AbstractListCommand<ZeroParams> {

    private static List<ListData> list;

    public LinkActTypeListCommand() {
        super(ZeroParams.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, ZeroParams zeroParams) throws UnifyException {
        if (list == null) {
            synchronized (LinkActTypeListCommand.class) {
                if (list == null) {
                    list = Collections.unmodifiableList(Arrays.asList(
                            new ListData(LinkActConstants.MAINTAIN_ACTION,
                                    getApplicationMessage("application.maintain.action")),
                            new ListData(LinkActConstants.LISTING_ACTION,
                                    getApplicationMessage("application.listing.action")),
                            new ListData(LinkActConstants.DECISION_ACTION,
                                    getApplicationMessage("application.decision.action")),
                            new ListData(LinkActConstants.SELECT_ACTION,
                                    getApplicationMessage("application.select.action")),
                            new ListData(LinkActConstants.DETAILS_ACTION,
                                    getApplicationMessage("application.details.action")),
                            new ListData(LinkActConstants.BUTTON_ACTION,
                                    getApplicationMessage("application.button.action"))));
                }
            }
        }

        return list;
    }

}
