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

/**
 * FlowCentral JavaScript functions.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
const fux = {};
const FC_USER_HINT_TIMEOUT = 3000; // 3 seconds.

/** Applet Menu */
fux.rigMenu = function(rgp) {
	const id = rgp.pId;
	fux.menuWire(rgp);

	const evp = {menuId:id};
	ux.addHdl(_id("exp_" + id), "click", fux.menuExpand, evp);
	ux.addHdl(_id("col_" + id), "click", fux.menuCollapse, evp);
}

fux.menuWire = function(rgp) {
	const id = rgp.pId;
	const _menu = _id(id);
	const initVisible = !rgp.pCollInit;

	// Menus
	if (rgp.pMenuIds) {
		var mevps = [];
		for(var i = 0; i < rgp.pMenuIds.length; i++) {
			var baseId = rgp.pMenuIds[i];
			var menuId = "menu_" + baseId;
			var submenuId = "submenu_" + baseId;
			var evp = {};
			evp.visible = initVisible;
			evp.submenuId = submenuId;
			evp.mevps = mevps;
			mevps.push(evp);
			ux.addHdl(_id(menuId), "click", fux.menuToggle, evp);
		}
		_menu.mevps = mevps;
	}
	
	// Menu items
	if (rgp.pMenuItems) {
		for (var mItem of rgp.pMenuItems) {
			var evp = {};
			if (mItem.isOpenWin) {
				evp.uMain = false;
				evp.uURL = mItem.path;
				evp.uWinName = mItem.winName;
				ux.addHdl(_id(mItem.id), "click", ux.openWindow, evp);
			} else {
				evp.uMain = false;
				evp.uOpenPath = mItem.path;
				ux.addHdl(_id(mItem.id), "click", ux.menuOpenPath, evp);
			}
		}
	}
}

fux.menuToggle = function(uEv) {
	const evp = uEv.evp;
	_id(evp.submenuId).style.display = evp.visible ? "none": "block";
	evp.visible = !evp.visible;
	for (var mevp of evp.mevps) {
		if (mevp.submenuId != evp.submenuId) {
			_id(mevp.submenuId).style.display = "none";
			mevp.visible = false;
		}
	}
}

fux.menuExpand = function(uEv) {
	const evp = uEv.evp;
	const _menu = _id(evp.menuId);
	for (var mevp of _menu.mevps) {
		_id(mevp.submenuId).style.display = "block";
		mevp.visible = true;
	}
}

fux.menuCollapse = function(uEv) {
	const evp = uEv.evp;
	const _menu = _id(evp.menuId);
	for (var mevp of _menu.mevps) {
		_id(mevp.submenuId).style.display = "none";
		mevp.visible = false;
	}
}

fux.rigMenuSearch = function(rgp) {
	const id = rgp.pId;
	var evp = ux.newEvPrm(rgp);
	evp.uId = id;
	evp.uCmd = id + "->search";
	evp.uIsReqTrg = true;
	ux.addHdl(_id(rgp.pInpId), "input", ux.post, evp);

	var evp = ux.newEvPrm(rgp);
	evp.uId = id;
	evp.uInpId = rgp.pInpId;
	evp.uCmd = id + "->clear";
	evp.uIsReqTrg = true;
	ux.addHdl(_id(rgp.pClrId), "click", fux.menuClear, evp);	
}

fux.menuClear = function (uEv) {
	const evp = uEv.evp;
	_id(evp.uInpId).value = "";
	ux.post(uEv);
}

fux.rigMenuSectionResult = function(rgp) {
	fux.menuWire(rgp);
}

/** Chart */
fux.chartList = [];
fux.rigChart = function(rgp) {
	const id = rgp.pId;
	var chart = new ApexCharts(_id(id), rgp.pOptions);
    chart.render();
    fux.chartList.push(chart);
}

fux.clearCharts = function() {
	if (fux.chartList.length > 0) {
		for(var i = 0; i < fux.chartList.length; i++) {
			fux.chartList[i].destroy();
		}

		fux.chartList = [];
	}
}

/** Entity Search */
fux.rigEntitySearch = function(rgp) {
	const id = rgp.pId;
	const sel = _id(rgp.pFilId);
	if (sel) {
		const evp = ux.newEvPrm(rgp);
		evp.uId = id;
		evp.facId = rgp.pFacId;
		evp.popupId = rgp.pPopId;
		evp.frameId = rgp.pBrdId;
		evp.stayOpenForMillSec = 0;
		evp.uRltId = rgp.pRltId;
		evp.uCmd = id + "->search";
		evp.uIsReqTrg = true;
		evp.altered = false;
		evp.manual = false;
		evp.withlabels=!rgp.pText;
		ux.addHdl(sel, "focusout", fux.entityFocusOut, evp);
		ux.addHdl(sel, "keydown", fux.entityListSwitch, evp);
		ux.addHdl(sel, "input", fux.entityListInput, evp);
	}

	const fac = _id(rgp.pFacId);
	fac.value = rgp.pDesc != undefined? rgp.pDesc:"";
}

fux.rigEntitySearchResult = function(rgp) {
	const id = rgp.pId;
	const sel = _id(id);
	if (rgp.pKeys && rgp.pKeys.length > 0) {
		const frm = _id(rgp.pRltId);
		if (frm && !frm.wired) {
			const evp = {};
			evp.uId = id;
			evp.withlabels=!rgp.pText;
			ux.addHdl(frm, "keydown", fux.entityListManual, evp);
			frm.wired = true;
		}
		
		for (var i = 0; i < rgp.pKeys.length; i++) {
			const evp = {};
			evp.uId = id;
			evp.uFacId = rgp.pFacId;
			evp.uKey = rgp.pKeys[i];
			evp.withlabels = !rgp.pText;
			const label = _id(rgp.pSelectIds[i]);
			if (evp.withlabels) {
				label.innerHTML = rgp.pLabels[i];
			} else {
				label.innerHTML = rgp.pKeys[i];
			}
			evp.uLabel = label;
			ux.addHdl(label, "click", fux.entityListSelect, evp);
		}

		sel._sel = -1;
		sel._oldSel = -1;
		sel._facId = rgp.pFacId;
		sel._key = rgp.pKeys;
		sel._label = rgp.pSelectIds;
		sel._clsNorm = rgp.pNormCls;
		sel._clsSel = rgp.pSelCls;
		sel._result = true;
	} else {
		sel._result = false;
	}
}

fux.esOnShow = function(rgp) {
	const res = _id(rgp.pRltId);
	if (res) {
		res.innerHTML = "";
	}
	
	ux.setFocus(rgp.pFilId);
}

fux.entityListSelect = function(uEv) {
	const evp = uEv.evp;
	const fac = _id(evp.uFacId);
	fac.value = evp.uLabel.innerHTML;

	const sel = _id(evp.uId);
	sel.value = evp.uKey;
	sel._sel = -1;
	sel._oldSel = -1;
	ux.setFocus(evp.uFacId);
	ux.hidePopup(null);
	ux.fireEvent(sel, "change", true);
}

fux.entityListSwitch = function(uEv) {
	const evp = uEv.evp;
	const sel = _id(evp.uId);
	if (sel._result && (uEv.uKeyCode == UNIFY_KEY_UP || uEv.uKeyCode == UNIFY_KEY_DOWN)) {
		evp.manual = true;
		fux.entityListManual(uEv);
		ux.setFocus(evp.uRltId);
		return;
	}
	
	fux.entityFocusOut(uEv);
}

fux.entityFocusOut = function(uEv) {
	const evp = uEv.evp;
	const sel = _id(evp.uId);
	
	if (evp.altered && (uEv.uKeyCode == UNIFY_KEY_TAB)) {
		ux.fireEvent(sel, "change", true);
	}
}

fux.entityListManual = function(uEv) {
	const evp = uEv.evp;
	const sel = _id(evp.uId);
	if (sel._result) {
		if (uEv.uKeyCode == UNIFY_KEY_ESCAPE) {
			evp.manual = false;
			ux.setFocus(sel._facId);
			ux.hidePopup(null);
			uEv.uStop();
			return;
		}
		
		if (uEv.uKeyCode == UNIFY_KEY_ENTER) {
			evp.manual = false;
			evp.uKey = sel._key[sel._sel]
			evp.uLabel = _id(sel._label[sel._sel]);
			evp.uFacId = sel._facId;
			fux.entityListSelect(uEv);
			uEv.uStop();
			return;
		}

		if (uEv.uKeyCode == UNIFY_KEY_UP || uEv.uKeyCode == UNIFY_KEY_DOWN) {
			if (uEv.uKeyCode == UNIFY_KEY_UP) {
				sel._sel--;
			} else {
				sel._sel++;
			}

			if (sel._sel < 0) {
				sel._sel = sel._label.length - 1;
			} else if (sel._sel >= sel._label.length) {
				sel._sel = 0;
			}

			if (sel._oldSel >= 0) {
				var label = _id(sel._label[sel._oldSel]);
				label.className = sel._clsNorm;
			}
			
			var label = _id(sel._label[sel._sel]);
			label.className = sel._clsSel;
			sel._oldSel = sel._sel;
			uEv.uStop();
		}
	}
}

fux.entityListInput = function(uEv) {
	const evp = uEv.evp;
	if(!ux.isPopupVisible(uEv)) {
		ux.doOpenPopup(evp);
	}
	
	const hid = _id(evp.uId);
	if (evp.withlabels) {
		hid.value = "";
	} else {
		const fac = _id(evp.facId);
		hid.value = fac.value;
	}
	evp.altered = true;
	ux.post(uEv);
}


/** Entity Select */
fux.rigEntitySelect = function(rgp) {
	const id = rgp.pId;
	const sel = _id(rgp.pFilId);
	if (sel) {
		const evp = ux.newEvPrm(rgp);
		evp.uId = id;
		evp.uFacId = rgp.pFacId;
		evp.uCmd = id + "->search";
		evp.uIsReqTrg = true;
		evp.altered = false;
		evp.selectOnly = rgp.pSelOnly;
		if (rgp.pText) {
			_id(evp.uFacId).readOnly = evp.selectOnly;
			ux.addHdl(sel, "input", fux.entitySelectText, evp);
			ux.addHdl(sel, "enter", fux.entitySelectInput, evp);
			const btn = _id(rgp.pBtnId);
			ux.addHdl(btn, "click", fux.entitySelectClick, evp);
		} else {
			ux.addHdl(sel, "input", fux.entitySelectInput, evp);
		}
	}

	const fac = _id(rgp.pFacId);
	fac.value = rgp.pDesc != undefined? rgp.pDesc:"";
}

fux.entitySelectText = function(uEv) {
	const evp = uEv.evp;
	_id(evp.uId).value = _id(evp.uFacId).value;
	evp.altered = true;
}

fux.entitySelectInput = function(uEv) {
	const evp = uEv.evp;
	_id(evp.uId).value = "";
	evp.uTrg =  uEv.uTrg;
	evp.altered = true;
	ux.post(uEv);
}

fux.entitySelectClick = function(uEv) {
	const evp = uEv.evp;
	evp.uTrg =  uEv.uTrg;
	evp.altered = true;
	ux.post(uEv);
}


/** Filter */
fux.rigFilter = function(rgp) {
	var id = rgp.pId;

	// Handle on change
	var chgId = rgp.pOnChgId;
	if (chgId && chgId.length) {
		var swapId = rgp.pSwpId;
		var andId = rgp.pAndId;
		var orId = rgp.pOrId;
		var addId = rgp.pAddId;
		var delId = rgp.pDelId;

		var evpNorm = ux.newEvPrm(rgp);
		evpNorm.uCmd = id + "->normalize";
		evpNorm.uRef = [ id ];
		evpNorm.uPanels = [ rgp.pContId ];

		var evpSwp = ux.newEvPrm(rgp);
		evpSwp.uCmd = id + "->swap";
		evpSwp.uRef = [ id ];
		evpSwp.uPanels = [ rgp.pContId ];

		var evpAnd = ux.newEvPrm(rgp);
		evpAnd.uCmd = id + "->and";
		evpAnd.uRef = [ id ];
		evpAnd.uPanels = [ rgp.pContId ];

		var evpOr = ux.newEvPrm(rgp);
		evpOr.uCmd = id + "->or";
		evpOr.uRef = [ id ];
		evpOr.uPanels = [ rgp.pContId ];

		var evpAdd = ux.newEvPrm(rgp);
		evpAdd.uCmd = id + "->add";
		evpAdd.uRef = [ id ];
		evpAdd.uPanels = [ rgp.pContId ];

		var evpDel = ux.newEvPrm(rgp);
		evpDel.uCmd = id + "->delete";
		evpDel.uRef = [ id ];
		evpDel.uPanels = [ rgp.pContId ];

		for (var i = 0; i < chgId.length; i++) {
			var idx = "d" + i;
			ux.addHdl(_id(chgId[i]), "change", ux.post, evpNorm);

			ux.addHdl(_id(swapId + idx), "click", ux.post, evpSwp);
			ux.addHdl(_id(andId + idx), "click", ux.post, evpAnd);
			ux.addHdl(_id(orId + idx), "click", ux.post, evpOr);
			ux.addHdl(_id(addId + idx), "click", ux.post, evpAdd);
			ux.addHdl(_id(delId + idx), "click", ux.post, evpDel);
		}
	}
}

/** Field sequence */
fux.rigFieldSequence = function(rgp) {
	var id = rgp.pId;

	// Handle on change
	var chgId = rgp.pOnChgId;
	if (chgId && chgId.length) {
		const moveUpId = rgp.pMoveUpId;
		const moveDownId = rgp.pMoveDownId;
		const delId = rgp.pDelId;

		const evpNorm = ux.newEvPrm(rgp);
		evpNorm.uCmd = id + "->normalize";
		evpNorm.uRef = [ id ];
		evpNorm.uPanels = [ rgp.pContId ];

		const evpMoveUp = ux.newEvPrm(rgp);
		evpMoveUp.uCmd = id + "->moveUp";
		evpMoveUp.uRef = [ id ];
		evpMoveUp.uPanels = [ rgp.pContId ];

		const evpMoveDown = ux.newEvPrm(rgp);
		evpMoveDown.uCmd = id + "->moveDown";
		evpMoveDown.uRef = [ id ];
		evpMoveDown.uPanels = [ rgp.pContId ];

		const evpDel = ux.newEvPrm(rgp);
		evpDel.uCmd = id + "->delete";
		evpDel.uRef = [ id ];
		evpDel.uPanels = [ rgp.pContId ];

		for (var i = 0; i < chgId.length; i++) {
			var idx = "d" + i;
			ux.addHdl(_id(chgId[i]), "change", ux.post, evpNorm);
			if (i > 0) {
				ux.addHdl(_id(moveUpId + idx), "click", ux.post, evpMoveUp);
			}
			if (i < (chgId.length - 2)) {
				ux.addHdl(_id(moveDownId + idx), "click", ux.post, evpMoveDown);
			}
			ux.addHdl(_id(delId + idx), "click", ux.post, evpDel);
		}
	}
}

/** Mini Form */
fux.rigMiniForm = function(rgp) {
	const tabWidgetIds = rgp.pTabWidId;
	if (tabWidgetIds && tabWidgetIds.length) {
		const focusMemId = rgp.pFocusMemId;
		const tabMemId = rgp.pTabMemId;
		for (var i = 0; i < tabWidgetIds.length;) {
			var evp = ux.newEvPrm(rgp);
			evp.uFocusMemId = focusMemId;
			evp.uTabMemId = tabMemId;
			evp.uTabId = tabWidgetIds[i];
			i++;
			evp.uNextTabId = i < tabWidgetIds.length ? tabWidgetIds[i] : "";			
			ux.addHdl(_id(evp.uTabId), "focus", fux.miniFormFocusMemory, evp);
			ux.addHdl(_id(evp.uTabId), "keydown", fux.miniFormTabout, evp);
		}
	}    
}

fux.preferredFocusId=null;
fux.miniFormFocusMemory = function(uEv) {
	const evp = uEv.evp;
	const focusMem = _id(evp.uFocusMemId);
	const tabMem = _id(evp.uTabMemId);
	if (focusMem) {
		focusMem.value = fux.preferredFocusId ? fux.preferredFocusId : evp.uTabId;
	}

	if (tabMem) {
		tabMem.value = evp.uTabId;
	}
	
	fux.preferredFocusId = null;
}

fux.miniFormTabout = function(uEv) {
	if (uEv.uKeyCode == UNIFY_KEY_TAB) {
		const evp = uEv.evp;
		const tabMem = _id(evp.uTabMemId);
		if (tabMem) {
			tabMem.value = evp.uNextTabId;
		}
		
		fux.preferredFocusId = evp.uTabId;
	}
}

/** Set values */
fux.rigSetValues = function(rgp) {
	var id = rgp.pId;

	// Handle on change
	var chgId = rgp.pOnChgId;
	if (chgId && chgId.length) {
		var delId = rgp.pDelId;

		var evpNorm = ux.newEvPrm(rgp);
		evpNorm.uCmd = id + "->normalize";
		evpNorm.uRef = [ id ];
		evpNorm.uPanels = [ rgp.pContId ];

		var evpDel = ux.newEvPrm(rgp);
		evpDel.uCmd = id + "->delete";
		evpDel.uRef = [ id ];
		evpDel.uPanels = [ rgp.pContId ];

		for (var i = 0; i < chgId.length; i++) {
			var idx = "d" + i;
			ux.addHdl(_id(chgId[i]), "change", ux.post, evpNorm);
			ux.addHdl(_id(delId + idx), "click", ux.post, evpDel);
		}
	}
}

/** Search */
fux.rigSearch = function(rgp) {
	var id = rgp.pId;
}

/** Tab sheet */
fux.rigTabSheet = function(rgp) {
	var id = rgp.pId;
	var pref = rgp.pTabId;
	var currSel = rgp.pCurrSel;
	var len = rgp.pTabCount;
	for (var i = 0; i < len; i++) {
		if (i != currSel) {
			var elem = _id(pref + i);
			if (elem) {
				var evp = ux.newEvPrm(rgp);
				evp.uCmd = id + "->choose";
				evp.uRef = [ id ];
				evp.uPanels = [ rgp.pContId ];
				evp.uReqTrg = i;
				ux.addHdl(elem, "click", ux.post, evp);
			}
		}
	}
}

/** Table */
fux.rigTable = function(rgp) {
	var id = rgp.pId;
	var tblToRig = _id(id);
	if (!tblToRig) {
		// TODO Show some error
		return;
	}

	if (rgp.pRowId) {
		var rows = _name(rgp.pRowId);
		for(var i = 0; i < rows.length; i++) {
			rows[i].dispIdx = i;
		}
	}
	
	if (rgp.pMultiSel) {
		tblToRig.uSelAllId = rgp.pSelAllId;
		tblToRig.uSelCtrlId = rgp.pSelCtrlId;
		tblToRig.uMultiSelDepList = rgp.pMultiSelDepList;
		tblToRig.uVisibleSel = 0;

		// Rig select
		const selAll = _id(rgp.pSelAllId);
		selAll._active = true;
		ux.cbWire(selAll);

		const selBoxes = _name(rgp.pSelCtrlId);
		tblToRig.uSelBoxes = selBoxes;

		const evp = {uRigTbl:tblToRig};
		const selAllFac = _id("fac_" + rgp.pSelAllId);
		selAllFac.selAll = selAll;
		ux.addHdl(selAllFac, "change", fux.tableSelAllClick, evp);

		for (var selBox of selBoxes) {
			selBox._active = true;
			ux.cbWire(selBox);
			
			const selBoxFac = _id("fac_" + selBox.id);
			selBoxFac.selBox = selBox;
			ux.addHdl(selBoxFac, "change", fux.tableMultiSelClick, evp);
		}
	}

	if (rgp.pColHeaderId) {
		for (var i = 0; i < rgp.pColCount; i++) {
			const evp = ux.newEvPrm(rgp);
			evp.uCmd = id + "->sortColumn";
			evp.uRef = [rgp.pSortIndexId];
			evp.uPanels = [ rgp.pContId ];
			if (rgp.pRefPanels) {
				for (var panelId of rgp.pRefPanels) {
					evp.uPanels.push(panelId);
				}
			}
			evp.uSortIndexId = rgp.pSortIndexId;
			evp.uColIndex = i;
			ux.addHdl(_id(rgp.pColHeaderId + i), "click", fux.tableHeaderClick, evp);
		}
	}
	
	const tabWidgetIds = rgp.pTabWidId;
	if (tabWidgetIds && tabWidgetIds.length) {
		const tabMemId = rgp.pTabMemId;
		for (var i = 0; i < tabWidgetIds.length;) {
			var evp = ux.newEvPrm(rgp);
			evp.uTabMemId = tabMemId;
			evp.uTabId = tabWidgetIds[i];
			i++;
			evp.uNextTabId = i < tabWidgetIds.length ? tabWidgetIds[i] : "";			
			ux.addHdl(_id(evp.uTabId), "focus", fux.tableFocusTabMem, evp);
			ux.addHdl(_id(evp.uTabId), "keydown", fux.tableTaboutTabMem, evp);
		}
	}
	
	if (rgp.pConDepList) {
		ux.setDisabledById(rgp.pConDepList, true);
	}
	
	if (rgp.pFocusId) {
		ux.setFocus({wdgid: rgp.pFocusId});
	}
}

fux.tableFocusTabMem = function(uEv) {
	const evp = uEv.evp;
	const tabMem = _id(evp.uTabMemId);
	if (tabMem) {
		tabMem.value = evp.uTabId;
	}
}

fux.tableTaboutTabMem = function(uEv) {
	if (uEv.uKeyCode == UNIFY_KEY_TAB) {
		const evp = uEv.evp;
		const tabMem = _id(evp.uTabMemId);
		if (tabMem) {
			tabMem.value = evp.uNextTabId;
		}
	}
}

fux.tableHeaderClick = function(uEv) {
	const evp = uEv.evp;
	_id(evp.uSortIndexId).value = evp.uColIndex;
	ux.post(uEv);
}

fux.tableMultiSelClick = function(uEv) {
	var selBoxFac = uEv.uTrg;
	if (selBoxFac) {
		var selBox = selBoxFac.selBox;
		var rigTbl = uEv.evp.uRigTbl;
		rigTbl.uLastSelClick = null;
		const checked = selBox.checked == true;
		const inc = checked ? 1 : -1;
		rigTbl.uVisibleSel += inc;

		if (rigTbl.uChain) {
			var chain = rigTbl.uChain;
			var i = selBoxFac.index;
			var level = chain[i];
			var len = chain.length;
			var selBoxes = rigTbl.uSelBoxes;
			
			while((++i) < len && level < chain[i]) {
				selBoxes[i].checked = checked;
				ux.cbSwitchImg(selBoxes[i]);
				rigTbl.uVisibleSel += inc;
			}
			
			if (checked) {
				i = selBoxFac.index;
				while ((--i) >= 0) {
					if (level > chain[i]) {
						selBoxes[i].checked = true;
						ux.cbSwitchImg(selBoxes[i]);
						rigTbl.uVisibleSel++;
						level--;
					}
				}
			}
		}
		
		fux.tableDisableMultiSelElements(rigTbl);
	}
}

fux.tableSelAllClick = function(uEv) {
	var rigTbl = uEv.evp.uRigTbl;
	var selAllFac = uEv.uTrg;
	var selBoxes = rigTbl.uSelBoxes;
	var selAllBox = selAllFac.selAll;
	if (selAllBox.checked == true) {
		rigTbl.uVisibleSel = selBoxes.length;
		for (var selBox of selBoxes) {
			selBox.checked = selAllBox.checked;
			ux.cbSwitchImg(selBox);
		}
	} else {
		rigTbl.uVisibleSel = 0;
		for (var selBox of selBoxes) {
			selBox.checked = selAllBox.checked;
			ux.cbSwitchImg(selBox);
		}
	}

	// Update dependencies
	fux.tableDisableMultiSelElements(rigTbl);
}

fux.tableDisableMultiSelElements = function(rigTbl) {
	ux.setDisabledById(rigTbl.uMultiSelDepList, rigTbl.uVisibleSel <= 0);
	var selAllBox = _id(rigTbl.uSelAllId);
	if (selAllBox) {
		if (selAllBox.checked && rigTbl.uVisibleSel < rigTbl.uSelBoxes.length) {
			selAllBox.checked = false;
			ux.cbSwitchImg(selAllBox);
		}
	}
}

/* Tree Table*/
fux.rigEntityTreeTable = function(rgp) {
	var id = rgp.pId;
	var tblToRig = _id(id);
	if (!tblToRig) {
		// TODO Show some error
		return;
	}

	if (rgp.pMultiSel) {
		tblToRig.uSelAllId = rgp.pSelAllId;
		tblToRig.uSelCtrlId = rgp.pSelCtrlId;
		tblToRig.uMultiSelDepList = rgp.pMultiSelDepList;
		tblToRig.uChain = rgp.pLvlChain;
		tblToRig.uVisibleSel = 0;

		// Rig select
		const selAll = _id(rgp.pSelAllId);
		selAll._active = true;
		ux.cbWire(selAll);

		const selBoxes = _name(rgp.pSelCtrlId);
		tblToRig.uSelBoxes = selBoxes;

		const evp = {uRigTbl:tblToRig};
		const selAllFac = _id("fac_" + rgp.pSelAllId);
		selAllFac.selAll = selAll;
		ux.addHdl(selAllFac, "change", fux.tableSelAllClick, evp);

		
		for (var i = 0; i < selBoxes.length; i++) {
			var selBox = selBoxes[i];
			selBox._active = true;
			ux.cbWire(selBox);
			
			const selBoxFac = _id("fac_" + selBox.id);
			selBoxFac.selBox = selBox;
			selBoxFac.index = i;
			ux.addHdl(selBoxFac, "change", fux.tableMultiSelClick, evp);
		}
	}

}

/** Widget Rules */
fux.rigWidgetRules = function(rgp) {
	var id = rgp.pId;

	// Handle on change
	var chgId = rgp.pOnChgId;
	if (chgId && chgId.length) {
		const delId = rgp.pDelId;

		const evpNorm = ux.newEvPrm(rgp);
		evpNorm.uCmd = id + "->normalize";
		evpNorm.uRef = [ id ];
		evpNorm.uPanels = [ rgp.pContId ];

		const evpDel = ux.newEvPrm(rgp);
		evpDel.uCmd = id + "->delete";
		evpDel.uRef = [ id ];
		evpDel.uPanels = [ rgp.pContId ];

		for (var i = 0; i < chgId.length; i++) {
			var idx = "d" + i;
			ux.addHdl(_id(chgId[i]), "change", ux.post, evpNorm);
			ux.addHdl(_id(delId + idx), "click", ux.post, evpDel);
		}
	}
}

/** Initialization */
fux.init = function() {
	ux.registerExtension("fux", fux);
	ux.registerPageReset("clearFuxChart", fux.clearCharts);
	ux.setHintTimeout(FC_USER_HINT_TIMEOUT);
	// Perform
	ux.setfn(fux.rigMenu,"fux01");  
	ux.setfn(fux.rigMenuSearch,"fux02");  
	ux.setfn(fux.rigMenuSectionResult,"fux03");  
	ux.setfn(fux.rigEntitySearchResult,"fux04");  
	ux.setfn(fux.esOnShow,"fux05");  
	ux.setfn(fux.rigEntitySelect,"fux06");  
	ux.setfn(fux.rigEntitySearch,"fux07");     
	ux.setfn(fux.rigEntityTreeTable,"fux08");  
	ux.setfn(fux.rigFieldSequence,"fux09");  
	ux.setfn(fux.rigFilter,"fux0a");  
	ux.setfn(fux.rigMiniForm,"fux0b");  
	ux.setfn(fux.rigSearch,"fux0c");  
	ux.setfn(fux.rigSetValues,"fux0d");  
	ux.setfn(fux.rigTable,"fux0e");  
	ux.setfn(fux.rigTabSheet,"fux0f"); 
	ux.setfn(fux.rigChart,"fux10");
	ux.setfn(fux.rigWidgetRules,"fux11");
}

fux.init();
