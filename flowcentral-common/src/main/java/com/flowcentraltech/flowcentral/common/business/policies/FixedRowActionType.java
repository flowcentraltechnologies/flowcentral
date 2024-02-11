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

package com.flowcentraltech.flowcentral.common.business.policies;

/**
 * Fixed row action type.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public enum FixedRowActionType {
    FIXED(
            0,
            "fix_",
            "$m{table.row.fixed}"),
    REMOVE(
            1,
            "rem_",
            "$m{table.row.remove}"),
    REMOVE_EDITABLE(
            1,
            "reme_",
            "$m{table.row.remove}"),
    ATTACH(
            2,
            "add_",
            "$m{table.row.attach}"),
    ATTACH_EDITABLE(
            2,
            "adde_",
            "$m{table.row.attach}"),
    DELETE(
            3,
            "del_",
            "$m{table.row.delete}"),
    DELETE_EDITABLE(
            3,
            "dele_",
            "$m{table.row.delete}");

    private final int index;

    private final String prefix;

    private final String label;

    private FixedRowActionType(int index, String prefix, String label) {
        this.index = index;
        this.prefix = prefix;
        this.label = label;
    }

    public int index() {
        return index;
    }

    public String prefix() {
        return prefix;
    }

    public String label() {
        return label;
    }

    public boolean fixed() {
        return FIXED.equals(this);
    }

    public boolean editable() {
        return REMOVE_EDITABLE.equals(this) || ATTACH_EDITABLE.equals(this) || DELETE_EDITABLE.equals(this);
    }

    public boolean updateLean() {
        return ATTACH.equals(this) || REMOVE.equals(this) || REMOVE_EDITABLE.equals(this)
                || ATTACH_EDITABLE.equals(this);
    }

    public boolean delete() {
        return DELETE.equals(this);
    }

}
