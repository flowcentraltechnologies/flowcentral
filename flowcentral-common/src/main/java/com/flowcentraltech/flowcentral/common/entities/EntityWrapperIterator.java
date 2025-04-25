/*
 * Copyright (c) 2021-2025 FlowCentral Technologies Limited.
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
package com.flowcentraltech.flowcentral.common.entities;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Entity wrapper iterator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class EntityWrapperIterator<T extends EntityWrapper> implements Iterator<T> {

    private T wrapper;

    private int currentIndex = 0;

    public EntityWrapperIterator(T wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public boolean hasNext() {
        return currentIndex < wrapper.size();
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        wrapper.setDataIndex(currentIndex++);
        return wrapper;
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Remove not supported");
    }
}