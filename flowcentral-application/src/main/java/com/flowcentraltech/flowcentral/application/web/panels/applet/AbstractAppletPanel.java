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
package com.flowcentraltech.flowcentral.application.web.panels.applet;

import java.util.List;

import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.AppletRequestAttributeConstants;
import com.flowcentraltech.flowcentral.application.constants.ApplicationResultMappingConstants;
import com.flowcentraltech.flowcentral.application.constants.WorkflowDraftType;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.RefDef;
import com.flowcentraltech.flowcentral.application.data.RequestOpenTabInfo;
import com.flowcentraltech.flowcentral.application.data.WorkflowDraftInfo;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractApplicationSwitchPanel;
import com.flowcentraltech.flowcentral.application.web.panels.AbstractForm.FormMode;
import com.flowcentraltech.flowcentral.application.web.panels.EntitySelect;
import com.flowcentraltech.flowcentral.application.web.widgets.EntityTable;
import com.flowcentraltech.flowcentral.common.business.policies.EntityActionResult;
import com.flowcentraltech.flowcentral.common.business.policies.TableActionResult;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralContainerPropertyConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralRequestAttributeConstants;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralResultMappingConstants;
import com.flowcentraltech.flowcentral.common.data.EntitySelectInfo;
import com.flowcentraltech.flowcentral.common.data.ReportOptions;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.IndexedTarget;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.constant.ResultMappingConstants;
import com.tcdng.unify.web.ui.widget.Panel;
import com.tcdng.unify.web.ui.widget.data.Hint.MODE;
import com.tcdng.unify.web.ui.widget.data.MessageBoxCaptions;
import com.tcdng.unify.web.ui.widget.data.MessageIcon;
import com.tcdng.unify.web.ui.widget.data.MessageMode;
import com.tcdng.unify.web.ui.widget.data.Popup;

/**
 * Convenient abstract base panel for applet panels.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractAppletPanel extends AbstractApplicationSwitchPanel {

	private static final int DEFAULT_MAX_TABLE_REPORT_ROWS = 100000;

	private static final int ULTIMATE_MAX_TABLE_REPORT_ROWS = 800000;

	protected void addPanelToPushComponents(String panelName, boolean editable) throws UnifyException {
		if (editable && getApplet().isSaveHeaderFormOnTabAction()) {
			Panel formPanel = getWidgetByShortName(Panel.class, panelName);
			getRequestContextUtil().addListItem(AppletRequestAttributeConstants.MAINFORM_PUSH_COMPONENTS,
					formPanel.getId());
		}
	}

	protected void openInBrowserTab(TableActionResult result, AppletDef formAppletDef, FormMode formMode)
			throws UnifyException {
		final String formName = formAppletDef.getPropValue(String.class,
				formMode.isCreate() ? AppletPropertyConstants.CREATE_FORM : AppletPropertyConstants.MAINTAIN_FORM);
		final List<StringToken> titleFormat = !StringUtils.isBlank(formName)
				? au().getFormDef(formName).getTitleFormat()
				: formAppletDef.getTitleFormat();
		final ValueStoreReader reader = new BeanValueStore(result.getInst()).getReader();
		final String title = !DataUtils.isBlank(titleFormat)
				? au().specialParamProvider().getStringGenerator(reader, reader, titleFormat).generate()
				: null;
		RequestOpenTabInfo requestOpenTabInfo = new RequestOpenTabInfo(title, result.getTabName(),
				(String) result.getResult(), result.isMultiPage());
		setRequestAttribute(AppletRequestAttributeConstants.OPEN_TAB_INFO, requestOpenTabInfo);
		setCommandResultMapping(ApplicationResultMappingConstants.OPEN_IN_NEW_BROWSER_WINDOW);
	}

	protected AbstractApplet getApplet() throws UnifyException {
		return getValue(AbstractApplet.class);
	}

	protected void prepareGenerateReport(EntityTable entityTable) throws UnifyException {
		final int maxReportRows = getContainerSetting(int.class,
				FlowCentralContainerPropertyConstants.FLOWCENTRAL_REPORTING_TABLE_RECORDS_MAXIMUM,
				DEFAULT_MAX_TABLE_REPORT_ROWS);
		final int actMaxReportRows = maxReportRows > ULTIMATE_MAX_TABLE_REPORT_ROWS ? ULTIMATE_MAX_TABLE_REPORT_ROWS
				: maxReportRows;

		if (entityTable.getTotalItemCount() > actMaxReportRows) {
			showMessageBox(resolveSessionMessage("$m{table.records.above.maximum.report.rows}", actMaxReportRows));
			return;
		}

		ReportOptions reportOptions = au().reportProvider().getReportableEntityDynamicReportOptions(
				entityTable.getEntityDef().getLongName(), entityTable.getDefaultReportColumnList());
		reportOptions.setReportResourcePath("/common/resource/report");
		reportOptions.setRestriction(entityTable.getSourceObject());
		reportOptions.setReportEntityList(true);
		showReportOptionsBox(reportOptions);
	}

	protected void handleEntityActionResult(EntityActionResult entityActionResult) throws UnifyException {
		handleEntityActionResult(entityActionResult, null);
	}

	protected void handleEntityActionResult(EntityActionResult entityActionResult, FormContext ctx)
			throws UnifyException {
		if (entityActionResult.isWorkflowCopied()) {
			entityActionResult.setSuccessHint("$m{entityformapplet.update.workflowcopy.success.hint}");
		}

		if (entityActionResult.isRefreshMenu()) {
			refreshApplicationMenu();
		}

		if (ctx != null && ctx.isWithReviewErrors()) {
			onReviewErrors(entityActionResult);
		} else if (entityActionResult.isWithTaskResult()) {
			fireEntityActionResultTask(entityActionResult);
		} else {
			setCommandResultMapping(entityActionResult, false);
		}

		handleHints(entityActionResult, ctx);
	}

	protected void showPromptWorkflowDraft(WorkflowDraftType type, IndexedTarget target) throws UnifyException {
		AbstractApplet applet = getValue(AbstractApplet.class);
		applet.setWorkflowDraftInfo(new WorkflowDraftInfo(type, target));
		final String caption = resolveSessionMessage("$m{formapplet.workflowdraft.caption}");
		final String prompt = type.isDelete() ? resolveSessionMessage("$m{formapplet.workflowdraft.submitdeleteprompt}")
				: resolveSessionMessage("$m{formapplet.workflowdraft.prompt}");
		final String viewMessage = resolveSessionMessage("$m{formapplet.workflowdraft.enterview}");
		final String okMessage = type.isDelete() ? resolveSessionMessage("$m{formapplet.workflowdraft.submitdelete}")
				: resolveSessionMessage("$m{formapplet.workflowdraft.enteredit}");
		final String cancelMessage = resolveSessionMessage("$m{formapplet.workflowdraft.cancel}");
		final String commandPath = type.isDelete() ? getCommandFullPath("deletionToWorkflow")
				: getCommandFullPath("openWorkflowDraft");
		MessageBoxCaptions captions = new MessageBoxCaptions(caption);
		if (type.isNew() || type.isUpdate() || type.isDelete()) {
			captions.setOkStyleClass("fc-orangebutton");
			captions.setCancelStyleClass("fc-bluebutton");
			captions.setOkCaption(okMessage);
			captions.setCancelCaption(cancelMessage);
			showMessageBox(MessageIcon.QUESTION, MessageMode.OK_CANCEL, captions, prompt, commandPath);
			return;
		}

		captions.setYesStyleClass("fc-orangebutton");
		captions.setNoStyleClass("fc-bluebutton");

		captions.setYesCaption(okMessage);
		captions.setNoCaption(viewMessage);
		showMessageBox(MessageIcon.QUESTION, MessageMode.YES_NO_CANCEL, captions, prompt, commandPath);
	}

	protected void setCommandResultMapping(EntityActionResult entityActionResult, boolean refereshPanel)
			throws UnifyException {
		if (entityActionResult != null) {
			if (entityActionResult.isHidePopupOnly()) {
				setCommandResultMapping(ResultMappingConstants.REFRESH_HIDE_POPUP);
				return;
			}

			if (entityActionResult.isWithResultPath()) {
				commandPost(entityActionResult.getResultPath());
				return;
			}

			if (entityActionResult.isWithTaskResult()) {
				fireEntityActionResultTask(entityActionResult);
				return;
			}

			if (entityActionResult.isCloseView()) {
				if (getApplet().navBackToPrevious()) {
					if (refereshPanel) {
						getApplet().au().commandRefreshPanelsAndHidePopup(this);
					}
				} else {
					setCloseResultMapping();
				}

				return;
			}

			if (entityActionResult.isClosePage()) {
				setCloseResultMapping();
				return;
			}

			if (entityActionResult.isDisplayListingReport()) {
				setRequestAttribute(FlowCentralRequestAttributeConstants.REPORT, entityActionResult.getResult());
				setCommandResultMapping(FlowCentralResultMappingConstants.VIEW_LISTING_REPORT);
				return;
			}

			if (entityActionResult.isEntitySelect()) {
				EntitySelect entitySelect = createEntitySelect(entityActionResult);
				commandShowPopup(new Popup(ApplicationResultMappingConstants.SHOW_ENTITY_SELECT, entitySelect));
				return;
			}
		}

		if (refereshPanel) {
			getApplet().au().commandRefreshPanelsAndHidePopup(this);
		} else {
			setCommandResultMapping(ResultMappingConstants.REFRESH_HIDE_POPUP);
		}
	}

	protected EntitySelect createEntitySelect(EntityActionResult entityActionResult) throws UnifyException {
		EntitySelectInfo entitySelectInfo = entityActionResult.getEntitySelect();
		final RefDef refDef = au().getRefDef(entitySelectInfo.getRef());
		final int limit = entitySelectInfo.getLimit();
		final String filter = entitySelectInfo.getDefaultFilter();
		EntitySelect entitySelect = au().constructEntitySelect(refDef,
				new BeanValueStore(entitySelectInfo.getInst()), null, null, filter, limit);
		entitySelect.setEnableFilter(true);
		entitySelect.applyFilterToSearch();
		entitySelect.setTitle(entitySelectInfo.getTitle());
		entitySelect.setEnableFilter(false);
		return entitySelect;
	}
	
	protected void handleHints(EntityActionResult entityActionResult, FormContext ctx) throws UnifyException {
		String errMsg = (String) getRequestAttribute(
				AppletRequestAttributeConstants.SILENT_MULTIRECORD_SEARCH_ERROR_MSG);
		if (!StringUtils.isBlank(errMsg)) {
			hintUser(MODE.ERROR, "$m{entityformapplet.formdelegation.error.hint}");
		} else {
			String successHint = entityActionResult.getSuccessHint();
			if (!StringUtils.isBlank(successHint)) {
				formHintSuccess(successHint, ctx != null ? ctx.getEntityName() : null);
			} else {
			    formHintFailure(entityActionResult.getFailureHint(), ctx != null ? ctx.getEntityName() : null);
			}
		}
	}

	protected void setCloseResultMapping() throws UnifyException {
		if (getApplet().getCtx().isInDetachedWindow()) {
			setCommandResultMapping(ResultMappingConstants.CLOSE_WINDOW);
		} else {
			setCommandResultMapping(ResultMappingConstants.CLOSE);
		}
	}

	protected abstract void onReviewErrors(EntityActionResult entityActionResult) throws UnifyException;

    private void formHintSuccess(String messageKey, String entityName) throws UnifyException {
        if (!StringUtils.isBlank(entityName)) {
            hintUser(messageKey, StringUtils.capitalizeFirstLetter(entityName));
        } else {
            hintUser(messageKey);
        }
    }

    private void formHintFailure(String messageKey, String entityName) throws UnifyException {
        if (!StringUtils.isBlank(messageKey)) {
            if (!StringUtils.isBlank(entityName)) {
                hintUser(MODE.ERROR, messageKey, StringUtils.capitalizeFirstLetter(entityName));
            } else {
                hintUser(MODE.ERROR, messageKey);
            }
        }
    }
}
