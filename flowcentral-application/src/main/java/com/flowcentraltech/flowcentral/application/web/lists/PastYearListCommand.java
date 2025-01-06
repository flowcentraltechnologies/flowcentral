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

package com.flowcentraltech.flowcentral.application.web.lists;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import com.tcdng.unify.common.data.Listable;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.data.ListNumberData;
import com.tcdng.unify.core.list.IntegerParam;

/**
 * Past year list.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("pastyearlist")
public class PastYearListCommand extends AbstractApplicationListCommand<IntegerParam> {

    public PastYearListCommand() {
        super(IntegerParam.class);
    }

    @Override
    public List<? extends Listable> execute(Locale locale, IntegerParam param) throws UnifyException {
        Calendar cal = Calendar.getInstance();
        cal.setTime(application().getNow());
        final int currentYear = cal.get(Calendar.YEAR);
        int start = currentYear;
        if (param.isPresent()) {
            start = param.getValue();
            start = start > currentYear ? currentYear : start;
        }

        List<ListNumberData> list = new ArrayList<ListNumberData>();
        for (int i = currentYear; i >= start; i--) {
            list.add(new ListNumberData(i));
        }

        return list;
    }

}
