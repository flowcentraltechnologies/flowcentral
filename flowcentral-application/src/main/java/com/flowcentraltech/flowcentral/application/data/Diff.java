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

package com.flowcentraltech.flowcentral.application.data;

import java.util.Collections;
import java.util.List;

import com.tcdng.unify.core.util.StringUtils;

/**
 * Difference.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class Diff {

    private DiffEntity left;

    private DiffEntity right;

    private List<Diff> children;

    public Diff(DiffEntity left, DiffEntity right, List<Diff> children) {
        this.left = left;
        this.right = right;
        this.children = children;
    }

    public Diff(DiffEntity left, DiffEntity right) {
        this.left = left;
        this.right = right;
        this.children = Collections.emptyList();
    }

    public DiffEntity getLeft() {
        return left;
    }

    public DiffEntity getRight() {
        return right;
    }

    public List<Diff> getChildren() {
        return children;
    }

    public String toString() {
        return StringUtils.toXmlString(this);
    }

}
