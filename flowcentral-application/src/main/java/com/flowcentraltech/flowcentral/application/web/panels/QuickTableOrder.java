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
package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.web.data.AppletContext;
import com.tcdng.unify.common.database.Entity;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Update;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;

/**
 * Quick table order object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class QuickTableOrder {

    private final AppletContext ctx;

    private final AppletDef appletDef;

    private final EntityClassDef entityClassDef;

    private final String baseField;

    private final Object baseId;

    private final String orderField;

    private String orderCaption;

    private List<OrderItem> orderItems;

    public QuickTableOrder(AppletContext ctx, AppletDef appletDef, EntityClassDef entityClassDef, String baseField,
            Object baseId, String orderField) {
        this.ctx = ctx;
        this.appletDef = appletDef;
        this.entityClassDef = entityClassDef;
        this.baseField = baseField;
        this.baseId = baseId;
        this.orderField = orderField;
        this.orderItems = Collections.emptyList();
    }

    public String getOrderCaption() {
        return orderCaption;
    }

    public void setOrderCaption(String orderCaption) {
        this.orderCaption = orderCaption;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    @SuppressWarnings("unchecked")
    public void loadOrderItems() throws UnifyException {
        orderItems = new ArrayList<OrderItem>();
        Query<? extends Entity> query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass())
                .addEquals(baseField, baseId).addOrder(orderField);
        List<Entity> resultList = (List<Entity>) ctx.environment().listAll(query);
        for (Entity inst : resultList) {
            final ValueStoreReader reader = new BeanValueStore(inst).getReader();
            final String description = appletDef.isWithTitleFormat()
                    ? ctx.au().specialParamProvider().getStringGenerator(reader, reader, appletDef.getTitleFormat())
                            .generate()
                    : ctx.au().getEntityDescription(entityClassDef, inst, null);
            orderItems.add(new OrderItem((Long) inst.getId(), description));
        }
    }

    @SuppressWarnings("unchecked")
    public boolean commitOrderItems() throws UnifyException {
        if (!DataUtils.isBlank(orderItems)) {
            int orderIndex = 0;
            for (OrderItem item : orderItems) {
                ctx.au().environment().updateById((Class<? extends Entity>) entityClassDef.getEntityClass(),
                        item.getId(), new Update().add(orderField, orderIndex++));
            }

            EntityFieldDef baseFieldDef = entityClassDef.getEntityDef().getFieldDef(baseField);
            EntityDef parentEntityDef = ctx.au()
                    .getEntityDef(ctx.au().getRefDef(baseFieldDef.getRefLongName()).getEntity());
            ctx.au().bumpVersion(parentEntityDef, (Long) baseId);
        }

        return true;
    }

    public static class OrderItem {

        private final Long id;

        private final String description;

        public OrderItem(Long id, String description) {
            this.id = id;
            this.description = description;
        }

        public Long getId() {
            return id;
        }

        public String getDescription() {
            return description;
        }

    }
}
