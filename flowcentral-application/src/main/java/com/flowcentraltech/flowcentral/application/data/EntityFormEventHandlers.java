/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.application.data;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.tcdng.unify.web.ui.widget.EventHandler;

/**
 * Entity form event handlers.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class EntityFormEventHandlers {

    private List<EventHandler> formSwitchOnChangeHandlers;

    private List<EventHandler> assnSwitchOnChangeHandlers;

    private List<EventHandler> entrySwitchOnChangeHandlers;

    private List<EventHandler> crudActionHandlers;

    private List<EventHandler> saveAsSwitchOnChangeHandlers;

    public EntityFormEventHandlers(EventHandler[] formSwitchOnChangeHandlers, EventHandler[] assnSwitchOnChangeHandlers,
            EventHandler[] entrySwitchOnChangeHandlers, 
            EventHandler[] crudActionHandlers, EventHandler[] saveAsSwitchOnChangeHandlers) {
        this.formSwitchOnChangeHandlers = formSwitchOnChangeHandlers != null
                ? Collections.unmodifiableList(Arrays.asList(formSwitchOnChangeHandlers))
                : Collections.emptyList();
        this.assnSwitchOnChangeHandlers = assnSwitchOnChangeHandlers != null
                ? Collections.unmodifiableList(Arrays.asList(assnSwitchOnChangeHandlers))
                : Collections.emptyList();
        this.entrySwitchOnChangeHandlers = entrySwitchOnChangeHandlers != null
                ? Collections.unmodifiableList(Arrays.asList(entrySwitchOnChangeHandlers))
                : Collections.emptyList();
        this.crudActionHandlers = crudActionHandlers != null
                ? Collections.unmodifiableList(Arrays.asList(crudActionHandlers))
                : Collections.emptyList();
        this.saveAsSwitchOnChangeHandlers = saveAsSwitchOnChangeHandlers != null
                ? Collections.unmodifiableList(Arrays.asList(saveAsSwitchOnChangeHandlers))
                : Collections.emptyList();
    }

    public List<EventHandler> getFormSwitchOnChangeHandlers() {
        return formSwitchOnChangeHandlers;
    }

    public List<EventHandler> getAssnSwitchOnChangeHandlers() {
        return assnSwitchOnChangeHandlers;
    }

    public List<EventHandler> getEntrySwitchOnChangeHandlers() {
        return entrySwitchOnChangeHandlers;
    }

    public List<EventHandler> getCrudActionHandlers() {
        return crudActionHandlers;
    }

    public List<EventHandler> getSaveAsSwitchOnChangeHandlers() {
        return saveAsSwitchOnChangeHandlers;
    }
}
