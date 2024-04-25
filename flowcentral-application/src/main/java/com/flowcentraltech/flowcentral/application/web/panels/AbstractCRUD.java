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

package com.flowcentraltech.flowcentral.application.web.panels;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef.FilterType;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.widgets.AbstractTable;
import com.flowcentraltech.flowcentral.application.web.widgets.MiniForm;
import com.flowcentraltech.flowcentral.common.business.policies.FormValidationContext;
import com.flowcentraltech.flowcentral.common.business.policies.SweepingCommitPolicy;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.tcdng.unify.core.UnifyException;

/**
 * CRUD object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractCRUD<T extends AbstractTable<?, ?>> {

	private final AppletUtilities au;

	private final SweepingCommitPolicy scp;

	private final T table;

	private final MiniForm createForm;

	private final MiniForm maintainForm;

	private final String baseField;

	private final Object baseId;

	private final String addCaption;

    private final boolean allowCreate;

    private int maintainIndex;

    private boolean allowUpdate;

    private boolean allowDelete;

	private boolean create;

	public AbstractCRUD(AppletUtilities au, SweepingCommitPolicy scp, String baseField, Object baseId, T table,
			MiniForm createForm, MiniForm maintainForm, String addCaption, boolean allowCreate) {
		this.au = au;
		this.scp = scp;
		this.baseField = baseField;
		this.baseId = baseId;
		this.table = table;
		this.createForm = createForm;
		this.maintainForm = maintainForm;
		this.addCaption = addCaption;
		this.allowCreate = allowCreate;
	}

	public AppletUtilities au() {
		return au;
	}

	public T getTable() {
		return table;
	}

	public MiniForm getForm() {
		return create ? createForm : maintainForm;
	}

	public String getAddCaption() throws UnifyException {
		return au.resolveSessionMessage(addCaption);
	}

	public boolean isWithFormErrors() {
		return isEditable() && getForm().getCtx().isWithFormErrors();
	}

	public boolean isWithDisplayItems() {
		return table.isWithDisplayItems();
	}

	public List<FormMessage> getValidationErrors() {
        if (isEditable()) {
            return Collections.emptyList();
        }

        return getForm().getCtx().getValidationErrors();
    }

	public void evaluateTabStates() throws UnifyException {
        if (isEditable()) {
            getForm().getCtx().evaluateTabStates();
        }
    }

	public boolean isCreate() {
		return create;
	}

	public boolean isMaintain() {
		return !create;
	}

	public boolean isEditable() {
		return !table.isViewOnly();
	}

	public boolean isAllowCreate() {
        return allowCreate;
    }

    public boolean isAllowUpdate() {
        return allowUpdate;
    }

    public boolean isAllowDelete() {
        return allowDelete;
    }

    public void enterCreate() throws UnifyException {
		create = true;
		if (isEditable()) {
			Object _inst = createObject();
			FormContext formContext = getForm().getCtx();
			formContext.clearValidationErrors();
            formContext.setReadOnly(!allowCreate);
			formContext.setInst(_inst);
			prepareCreate(formContext);
			table.setHighlightedRow(-1);
		}
	}

	public void enterMaintain(int index) throws UnifyException {
		create = false;
		if (table.isWithDisplayItems()) {
			Object inst = table.getDisplayItem(index);
			Object _inst = reload(inst);
			final Date now = table.au().getNow();
            allowUpdate = table.match(FilterType.MAINTAIN_UPDATE, _inst, now);
            allowDelete = table.match(FilterType.MAINTAIN_DELETE, _inst, now);
            
			FormContext formContext = getForm().getCtx();
			formContext.setReadOnly(table.isViewOnly());
			formContext.setInst(_inst);
			prepareMaintain(formContext);
			maintainIndex = index;
			table.setHighlightedRow(maintainIndex);
		}
	}

	public void save() throws UnifyException {
		EvaluationMode evaluationMode = create ? EvaluationMode.CREATE : EvaluationMode.UPDATE;
		FormContext formContext = getForm().getCtx();
		evaluateFormContext(formContext, new FormValidationContext(evaluationMode));
		if (!isWithFormErrors()) {
			if (create) {
				create(formContext, scp);
				table.reset();
				enterCreate();
			} else {
				update(formContext, scp);
				table.reset();
				enterMaintain(maintainIndex);
			}
		}
	}

	public void delete() throws UnifyException {
		FormContext formContext = getForm().getCtx();
		delete(formContext, scp);
		table.reset();
		if (table.isWithDisplayItems()) {
			enterMaintain(0);
		} else {
			enterCreate();
		}
	}

	protected String getBaseField() {
		return baseField;
	}

	protected Object getBaseId() {
		return baseId;
	}

	protected FormTabDef getCreateFormTabDef() {
		return createForm != null ? createForm.getFormTabDef() : null;
	}

	protected FormDef getCreateFormDef() {
		return createForm != null ? createForm.getCtx().getFormDef() : null;
	}

	protected FormDef getMaintainFormDef() {
		return maintainForm != null ? maintainForm.getCtx().getFormDef() : null;
	}

	protected abstract void evaluateFormContext(FormContext formContext, FormValidationContext ctx)
			throws UnifyException;

	protected abstract Object createObject() throws UnifyException;

	protected abstract void prepareCreate(FormContext formContext) throws UnifyException;

	protected abstract void create(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException;

	protected abstract Object reload(Object inst) throws UnifyException;

	protected abstract void prepareMaintain(FormContext formContext) throws UnifyException;

	protected abstract void update(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException;

	protected abstract void delete(FormContext formContext, SweepingCommitPolicy scp) throws UnifyException;
}
