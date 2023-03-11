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
package com.flowcentraltech.flowcentral.application.constants;

/**
 * Application module error constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface ApplicationModuleErrorConstants {

    /**
     * Attempt to swap filter condition information compound type to simple type.
     */
    String FILTERCONDITIONINFO_ATTEMPT_SWAP_COMPOUND_SIMPLE = "APPLICATION_0001";

    /**
     * Attempt to insert filter condition in simple condition.
     */
    String FILTERCONDITIONINFO_INSERT_IN_SIMPLE_CONDITION = "APPLICATION_0002";

    /**
     * Filter condition type {0} is not supported for field type {1}.
     */
    String FILTERCONDITIONINFO_FIELD_UNSUPPORTED_CONDITION = "APPLICATION_0003";

    /** Column {0} is unknown for table definition {1}. */
    String TABLE_DEFINTION_UNKNOWN_COLUMN = "APPLICATION_0004";

    /**
     * Can not find entity with name {0} of type {1} in application {2}.
     */
    String CANNOT_FIND_APPLICATION_ENTITY = "APPLICATION_0005";

    /** Unable to resolve reserved applet {0}. */
    String UNABLE_TO_RESOLVE_RESERVED_APPLET = "APPLICATION_0006";

    /**
     * Unable to import data {0} with existing unique record {1}.
     */
    String UNABLE_LOAD_DATA_WITH_EXISTING_UNIQUE_RECORD = "APPLICATION_0007";

    /**
     * Invalid list-only field [{0}]. List-only field key must always be a reference data type.
     */
    String INVALID_LIST_ONLY_FIELD = "APPLICATION_0008";

    /**
     * No entry table configured for applet [{0}] quick edit.
     */
    String NO_ENTRY_TABLE_CONFIGURED_FOR_APPLET_QUICK_EDIT = "APPLICATION_0009";

    /**
     * No form configured for applet [{0}] quick edit.
     */
    String NO_FORM_CONFIGURED_FOR_APPLET_QUICK_EDIT = "APPLICATION_000A";

}
