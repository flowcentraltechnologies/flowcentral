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

fux.currSubMenuId = null;
 
fux.menuWire = function(rgp) {
	const id = rgp.pId;
	const menuPopId = "mpop_" + id
	const _menu = _id(id);
	const initVisible = !rgp.pCollInit;

	if (rgp.pReg) {
		ux.registerOtherPopup(menuPopId);
	}
	 
	const horiz = rgp.pHoriz;
	// Scroll
	if (horiz) {
		var evp = {};
		evp.secId = rgp.pSecId;
		evp.sldId = rgp.pSldId;
		ux.addHdl(_id(rgp.pLeftId), "click", fux.menuScrollLeft, evp);
		ux.addHdl(_id(rgp.pRightId), "click", fux.menuScrollRight, evp);
	}
	
	// Menus
	if (rgp.pMenuIds) {
		var mevps = [];
		for(var i = 0; i < rgp.pMenuIds.length; i++) {
			var baseId = rgp.pMenuIds[i];
			var menuId = "menu_" + baseId;
			var submenuId = "submenu_" + baseId;
			var melem = _id(menuId);
			var evp = {};
			evp.horiz = horiz;
			evp.uMenuPopId = menuPopId;
			evp.visible = initVisible;
			evp.menuId = menuId;
			evp.secId = rgp.pSecId;
			evp.submenuId = submenuId;
			evp.mevps = mevps;
			mevps.push(evp);
			if (!horiz) {
				ux.addHdl(melem, "mouseover", fux.menuHidePopup, evp);
			}
			
			ux.addHdl(melem, "click", fux.menuToggle, evp);
		}
		_menu.mevps = mevps;
	}
	
	// Menu items
	if (rgp.pMenuItems) {
		for (var mItem of rgp.pMenuItems) { 
			const evp = {};
			const melem = _id(mItem.id);
			evp.uMenuPopId = menuPopId;
			if (mItem.pSubMenuItems) {
				if (rgp.pReg && !horiz) {
					evp.uMenuItems = mItem.pSubMenuItems;
					ux.addHdl(melem, "mouseover", fux.menuShowPopup, evp);
				}
			} else { 
				if (rgp.pReg && !horiz) {
					ux.addHdl(melem, "mouseover", fux.menuHidePopup, evp);
				}
				
				if (mItem.isOpenWin) {
					evp.uMain = false;
					evp.uURL = mItem.path;
					evp.uWinName = mItem.winName;
					ux.addHdl(melem, "click", fux.openWindow, evp);
				} else {
					evp.uMain = false;
					evp.uOpenPath = mItem.path;
					evp.uIsDebounce = true;
					ux.addHdl(melem, "click", fux.menuOpenPath, evp);
				}
			}
		}
	}
}

fux.menuScrollLeft = function(uEv) {
	const evp = uEv.evp;
	const celem = _id(evp.secId);
	const selem = _id(evp.sldId);
	const crect = ux.boundingRect(celem);
	const srect = ux.boundingRect(selem);
}

fux.menuScrollRight = function(uEv) {
	const evp = uEv.evp;
}

fux.menuShowPopup = function(uEv) {
	const id = uEv.uTrg.id;
	if (id) {
		const melem = _id(id);
		const evp = uEv.evp;
		const rect = ux.boundingRect(melem);
		const pelem = _id(evp.uMenuPopId);
		const _baseId = id + "_sb";
		const uMenuItems = evp.uMenuItems;
		var cnt = [];
		for (var i = 0; i < uMenuItems.length;i++) {
			cnt.push("<div class=\"base\" id=\"");
			cnt.push(_baseId + i);
			cnt.push("\">");
            cnt.push("<span class=\"icon\">");
            cnt.push(uMenuItems[i].icon);
            cnt.push("</span>");
			cnt.push("<a>");
			cnt.push(uMenuItems[i].label);
			cnt.push("</a>");
            cnt.push("</div>");
		}
		
		pelem.innerHTML = cnt.join("");	
		
		var viewRect = ux.getWindowRect();
		var y = rect.top;
		var diff = (y + 32 * uMenuItems.length + 20) - viewRect.bottom;
		if (diff > 0) {
			y -= diff;
		}
		 	
		pelem.style.top = y + "px";
		pelem.style.left = rect.right  + "px";
		pelem.style.display = "block";
		
		for (var i = 0; i < uMenuItems.length;i++) { 
			const _evp = {};
			const melem = _id(_baseId + i);
			_evp.uMenuPopId = evp.uMenuPopId ;
			if (uMenuItems[i].isOpenWin) {
				_evp.uMain = false;
				_evp.uURL = uMenuItems[i].path;
				_evp.uWinName = uMenuItems[i].winName;
				ux.addHdl(melem, "click", fux.openWindow, _evp);
			} else {
				_evp.uMain = false;
				_evp.uOpenPath = uMenuItems[i].path;
				_evp.uIsDebounce = true;
				ux.addHdl(melem, "click", fux.menuOpenPath, _evp);
			}
		}
	}
}

fux.openWindow = function(uEv) {
	fux.menuHidePopup(uEv);
	ux.openWindow(uEv);
}

fux.menuOpenPath = function(uEv) {
	fux.menuHidePopup(uEv);
	ux.menuOpenPath(uEv);
}

fux.menuHidePopup = function(uEv) {
	const evp = uEv.evp;
	const elem = _id(evp.uMenuPopId);
	if (elem) {
		elem.style.display = "none";
	} 

	const selem = _id(fux.currSubMenuId);
	if (selem) {
		selem.style.display = "none";
	} 
}

fux.menuToggle = function(uEv) {
	const evp = uEv.evp;
	const selem = _id(evp.submenuId);
	if (evp.horiz) {
		fux.currSubMenuId = evp.submenuId;	
		const srect = ux.boundingRect(_id(evp.secId));
		const mrect = ux.boundingRect(_id(evp.menuId));
		const x = mrect.left - srect.left;
		selem.style.left = x + "px";
	}
	
	selem.style.display = evp.visible ? "none": "block";
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
	evp.uCmd = id + "->search";
	evp.uIsReqTrg = true;
	ux.addHdl(_id(rgp.pInpId), "input", ux.post, evp);

	var evp = ux.newEvPrm(rgp);
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
	const options = rgp.pOptions
	if (options._yformatter) {
		options.yaxis.labels.formatter = function(val, opts) {
				if (options._yintegers) {
					val = parseInt(val.toFixed(0));
				}				
		        return val.toLocaleString();
		      };
	} else if (options._yintegers) {
		options.yaxis.labels.formatter = function(val, opts) {
		        return val.toFixed(0);
		      };
	}
	
	if (options.dataLabels._cformatter) {
		options.dataLabels.formatter = function(val, opts) {
			        return (Math.round(val * 10) / 10).toFixed(1) + "%";
			      };
	} else if (options.dataLabels._dformatter) {
		options.dataLabels.formatter = function(val, opts) {
			        return val.toLocaleString();
			      };
	}
	
	var chart = new ApexCharts(_id(id), options);
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
		evp.facId = rgp.pFacId;
		evp.popupId = rgp.pPopId;
		evp.frameId = rgp.pBrdId;
		evp.stayOpenForMillSec = 0;
		evp.uRltId = rgp.pRltId;
		evp.uCmd = id + "->search";
		evp.uIsReqTrg = true;
		evp.altered = false;
		evp.withlabels=!rgp.pText;
		ux.addHdl(sel, "focusout", fux.entityFocusOut, evp);
		ux.addHdl(sel, "keydown", fux.entityListSwitch, evp);
		ux.addHdl(sel, "input", fux.entityListInput, evp);
	}

	const fac = _id(rgp.pFacId);
	if (fac) {
		fac.value = rgp.pDesc != undefined? rgp.pDesc:"";
	}
}

fux.rigEntitySearchResult = function(rgp) {
	const id = rgp.pId;
	const sel = _id(id);
	if (rgp.pKeys && rgp.pKeys.length > 0) {	
		for (var i = 0; i < rgp.pKeys.length; i++) {
			const evp = {};
			evp.uId = id;
			evp.uFacId = rgp.pFacId;
			evp.uKey = rgp.pKeys[i];
			evp.withlabels = !rgp.pText;
			const _label = _id(rgp.pSelectIds[i]);
			if (evp.withlabels) {
				_label.innerHTML = rgp.pLabels[i];
			} else {
				_label.innerHTML = rgp.pKeys[i];
			}
			evp.uLabel = _label;
			ux.addHdl(_label, "click", fux.entityListSelect, evp);
			ux.addHdl(_label, "mouseenter", fux.entityListEnter, evp);
			ux.addHdl(_label, "mouseleave", fux.entityListLeave, evp);
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

fux.entityListEnter = function(uEv) {
	_id(uEv.evp.uId)._msel = true;
}

fux.entityListLeave = function(uEv) {
	_id(uEv.evp.uId)._msel = false;
}

fux.entityListSelect = function(uEv) {
	const evp = uEv.evp;
	const fac = _id(evp.uFacId);
	fac.value = ux.unescape(evp.uLabel.innerHTML);

	const sel = _id(evp.uId);
	sel._sel = -1;
	sel._oldSel = -1;
	ux.setFocus(evp.uFacId);
	ux.hidePopup(null);
	if (!(sel.value === evp.uKey)) {
		sel.value = evp.uKey;
		ux.fireEvent(sel, "change", true);
	}
}

fux.entityListSwitch = function(uEv) {
	if (fux.entityListManual(uEv)) {
		return;
	}
	
	fux.entityFocusOut(uEv);
}

fux.entityFocusOut = function(uEv) {
	const evp = uEv.evp;
	const sel = _id(evp.uId);
	if (evp.altered &&
		(uEv.uKeyCode == UNIFY_KEY_TAB || (uEv.uKeyCode === undefined && !sel._msel))) {
		ux.fireEvent(sel, "change", true);
	}
}

fux.entityListManual = function(uEv) {
	const evp = uEv.evp;
	const sel = _id(evp.uId);
	if (sel._result) {
		if (uEv.uKeyCode == UNIFY_KEY_ESCAPE) {
			ux.setFocus(sel._facId);
			ux.hidePopup(null);
			uEv.uStop();
			return true;
		}
		
		if (uEv.uKeyCode == UNIFY_KEY_ENTER) {
			evp.uKey = sel._key[sel._sel]
			evp.uLabel = _id(sel._label[sel._sel]);
			evp.uFacId = sel._facId;
			fux.entityListSelect(uEv);
			uEv.uStop();
			return true;
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
			return true;
		}
	}
	
	return false;
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
		evp.uFacId = rgp.pFacId;
		evp.uCmd = id + "->search";
		if (rgp.pFormId) {
			evp.uRef = [ rgp.pFormId ];	
		}
		evp.uIsReqTrg = true;
		evp.altered = false;
		if (rgp.pText) {
			ux.addHdl(sel, "input", fux.entitySelectText, evp);
			ux.addHdl(sel, "enter", fux.entitySelectInput, evp);
			const btn = _id(rgp.pBtnId);
			ux.addHdl(btn, "click", fux.entitySelectClick, evp);
		} else {
			ux.addHdl(sel, "input", fux.entitySelectInput, evp);
		}
	}

	const fac = _id(rgp.pFacId);
	if (fac) {
		fac.value = rgp.pDesc != undefined? rgp.pDesc:"";
	}
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

/** Entity Composition */
fux.rigEntityComposition =  function(rgp) {
	var id = rgp.pId;
	
	// Handle on change
	var chgId = rgp.pOnChgId;
	if (chgId && chgId.length) {
		var addEId = rgp.pAddEId;
		var delEId = rgp.pDelEId;
		var addFId = rgp.pAddFId;
		var delFId = rgp.pDelFId;
	
		var evpNorm = ux.newEvPrm(rgp);
		evpNorm.uCmd = id + "->normalize";
		evpNorm.uRef = [ id ];
		evpNorm.uPanels = [ rgp.pContId ];
	
		var evpAddE = ux.newEvPrm(rgp);
		evpAddE.uCmd = id + "->addEntity";
		evpAddE.uRef = [ id ];
		evpAddE.uPanels = [ rgp.pContId ];
	
		var evpDelE = ux.newEvPrm(rgp);
		evpDelE.uCmd = id + "->delEntity";
		evpDelE.uRef = [ id ];
		evpDelE.uPanels = [ rgp.pContId ];
	
		var evpAddF = ux.newEvPrm(rgp);
		evpAddF.uCmd = id + "->addField";
		evpAddF.uRef = [ id ];
		evpAddF.uPanels = [ rgp.pContId ];
	
		var evpDelF = ux.newEvPrm(rgp);
		evpDelF.uCmd = id + "->delField";
		evpDelF.uRef = [ id ];
		evpDelF.uPanels = [ rgp.pContId ];
	
		for (var i = 0; i < chgId.length; i++) {
			var idx = "d" + i;
			ux.addHdl(_id(chgId[i]), "change", ux.post, evpNorm);
			ux.addHdl(_id(addEId + idx), "click", ux.post, evpAddE);
			ux.addHdl(_id(delEId + idx), "click", ux.post, evpDelE);
			ux.addHdl(_id(addFId + idx), "click", ux.post, evpAddF);
			ux.addHdl(_id(delFId + idx), "click", ux.post, evpDelF);
		}
	}
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

/** Collapsible Table */
fux.rigCollapsibleTable = function(rgp) {
	const id = rgp.pId;
	const rows = rgp.pRows;
	
	const evpe = ux.newEvPrm(rgp);
	evpe.uCmd = id + "->expandAll";
	evpe.uSendTrg = i;
	evpe.uPanels = [ rgp.pContId ];			
	ux.addHdl(_id(id + "_exp"), "click", ux.post, evpe);
	
	const evpc = ux.newEvPrm(rgp);
	evpc.uCmd = id + "->collapseAll";
	evpc.uSendTrg = i;
	evpc.uPanels = [ rgp.pContId ];			
	ux.addHdl(_id(id + "_col"), "click", ux.post, evpc);
	
	for (var i = 0; i < rows; i++) {
		const evp = ux.newEvPrm(rgp);
		evp.uCmd = id + "->toggle";
		evp.uSendTrg = i;
		evp.uPanels = [ rgp.pContId ];				
		ux.addHdl(_id(id + "_cl" + i), "click", ux.post, evp);
	}
}

/** Line entries*/
fux.rigLineEntries = function(rgp) {
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

/** Token entries*/
fux.rigTokenEntries = function(rgp) {
	const fldId = rgp.pOnFldId;
	if (fldId && fldId.length) {
		const evp = ux.newEvPrm(rgp);
		evp.uPreviewId = rgp.pPreviewId;
		for (var i = 0; i < fldId.length; i++) {
			ux.addHdl(_id(fldId[i]), "click", fux.rigTokenClick , evp);
		}
	}
}

fux.rigTokenClick = function(uEv) {
	const evp = uEv.evp;
	const pinput = _id(evp.uPreviewId);
	ux.setTextAtCaret(pinput, "{{" + _id("trg_" + uEv.uTrg.id).value + "}}");
	pinput.focus();
}

/** Form Annotation */
fux.rigFormAnnotation = function(rgp) {
	if (rgp.pAnnot) {
		const nid = rgp.pId + "_an";
		const cid = rgp.pId + "_cl";
		var hideMilliSec = rgp.pHideSec * 1000;

		for (var i = 0; i < rgp.pAnnot.length; i++) {
			var evp = ux.newEvPrm(rgp);
			evp.nId = nid + i;
			if (rgp.pAnnot[i].disp) {
				window.setTimeout("fux.closeAnnotationById(\""+ evp.nId + "\");"
					, hideMilliSec);
			
				hideMilliSec += 1000;
			}
			
			if (rgp.pAnnot[i].closable) {
				ux.addHdl(_id(cid + i), "click", fux.closeAnnotation, evp);			
			}
		}
	}	
}

fux.closeAnnotation = function(uEv) {
	fux.closeAnnotationById(uEv.evp.nId);
}

fux.closeAnnotationById = function(id) {
	const annot = _id(id);
	if (annot) {
		annot.style.display = "none";
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
			ux.addHdl(_id(evp.uTabId), "focus", fux.mFrmFocusMem, evp);
			ux.addHdl(_id(evp.uTabId), "keydown", fux.mFrmTabout, evp);
		}
	}

	const help = rgp.pHelp;
	if (help && help.length) {	
		for (var i = 0; i < help.length; i++) {
			var evp = ux.newEvPrm(rgp);
			evp.uId = rgp.pId;
			evp.uHelp = help[i];
			ux.addHdl(_id("help_" + evp.uHelp.id), "click", fux.mFrmHelp, evp);
		}
	}  

	const preview = rgp.pPreview;
	if (preview && preview.length) {	
		for (var i = 0; i < preview.length; i++) {
			var evp = ux.newEvPrm(rgp);
			evp.uId = rgp.pId;
			evp.uPreview = preview[i];
			ux.addHdl(_id("view_" + evp.uPreview.id), "click", fux.mFrmPreview, evp);
		}
	}  
}

fux.preferredFocusId=null;
fux.mFrmFocusMem = function(uEv) {
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

fux.mFrmTabout = function(uEv) {
	if (uEv.uKeyCode == UNIFY_KEY_TAB) {
		const evp = uEv.evp;
		const tabMem = _id(evp.uTabMemId);
		if (tabMem) {
			tabMem.value = evp.uNextTabId;
		}
		
		fux.preferredFocusId = evp.uTabId;
	}
}

fux.mFrmPreview = function(uEv) {
	const evp = uEv.evp;
	const entId = _id(evp.uPreview.id).value;
	if (entId) {
		evp.uCmd = evp.uId + "->preview";
		evp.uSendTrg = entId + ":" + evp.uPreview.frm;
		evp.uIsDebounce = true;
		ux.post(uEv);
	}
}

fux.mFrmHelp = function(uEv) {
	const evp = uEv.evp;
	evp.uCmd = evp.uId + "->help";
	evp.uSendTrg = evp.uHelp.fld;
	evp.uIsDebounce = true;
	ux.post(uEv);
}

/** Popup window text  */
fux.rigPopupWinText = function(rgp) {
	const elem = _id(rgp.pTrgId);
	if (elem) {
		const id = rgp.pId;
		const evp = ux.newEvPrm(rgp);
		evp.uCmd = id + "->popup";
		evp.uRef = rgp.pRef;
		evp.uIsDebounce = true;
		ux.addHdl(elem, rgp.pEvt, ux.post, evp);
	}
}

/** Input array */
fux.rigInputArray = function(rgp) {

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
				var evp = fux.newCmdEvPrm(rgp, "choose");
				evp.uReqTrg = i;
				evp.uIsDebounce=true;
				ux.addHdl(elem, "click", ux.post, evp);
			}
		}
	}
}

/** Table */
fux.rigTable = function(rgp) {
	var id = rgp.pId;
	var tbl = _id(id);
	if (!tbl) {
		// TODO Show some error
		return;
	}

	if (rgp.pRowId) {
		var rows = _name(rgp.pRowId);
		for(var i = 0; i < rows.length; i++) {
			rows[i].dispIdx = i;
		}
	}
	
    if (rgp.pFixedRows) {
    	fux.wireGroupClick(rgp, rgp.pfExcCtrlId, "exclude");
    	fux.wireGroupClick(rgp, rgp.pfIncCtrlId, "include");
    	fux.wireGroupClick(rgp, rgp.pfDelCtrlId, "delete");
    	
		if (rgp.pMultiSel) {
    		fux.tableFixedMultiClick(rgp, rgp.pAthCtrlId, "includeSelect")
    		fux.tableFixedMultiClick(rgp, rgp.pRemCtrlId, "excludeSelect")
    		fux.tableFixedMultiClick(rgp, rgp.pAthAllCtrlId, "includeAll")
    		fux.tableFixedMultiClick(rgp, rgp.pRemAllCtrlId, "excludeAll")
    		
    		rgp.pMultiSelDepList.push(rgp.pAthCtrlId);
    		rgp.pMultiSelDepList.push(rgp.pRemCtrlId);
		}
    }
    
	if (rgp.pMultiSel) {
		tbl.uSelAllId = rgp.pSelAllId;
		tbl.uSelCtrlId = rgp.pSelCtrlId;
		tbl.uMultiSelDepList = rgp.pMultiSelDepList;
		tbl.uMultiSelLink = rgp.pMultiSelLink;
		tbl.uVisibleSel = 0;

		// Rig select
		if (!rgp.pDisabled) {
			const selAll = _id(rgp.pSelAllId);
			selAll._active = true;
			ux.cbWire(selAll);
	
			const selBoxes = _name(rgp.pSelCtrlId);
			tbl.uSelBoxes = selBoxes;
	
			const evp = {uRigTbl:tbl};
			const selAllFac = _id("fac_" + rgp.pSelAllId);
			selAllFac.selAll = selAll;
			ux.addHdl(selAllFac, "change", fux.tableSelAllClick, evp);
	
			for (var i = 0; i < selBoxes.length; i++) {
				var selBox = selBoxes[i];
				selBox._active = true;
				ux.cbWire(selBox);
				
				const selBoxFac = _id("fac_" + selBox.id);
				var _evp = evp;
				if (rgp.pMultiSelLink) {
					_evp = fux.newCmdEvPrm(rgp, "multiSelect");
					_evp.uSendTrg = i;
					_evp.uRigTbl = evp.uRigTbl;
				}
				
				selBoxFac.selBox = selBox;
				ux.addHdl(selBoxFac, "change", fux.tableMultiSelClick, _evp);
			}
		}
	}

	if (rgp.pColHeaderId) {
		for (var i = 0; i < rgp.pColCount; i++) {
			const evp = fux.newCmdEvPrm(rgp, "sortColumn");
			evp.uRef = [rgp.pSortIndexId];
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
	
	const swhCmdList = rgp.pSwhCmdList;
	if (swhCmdList) {
		for (var i = 0; i < swhCmdList.length; i++) {
			var info = swhCmdList[i];
			const evp = fux.newCmdEvPrm(rgp, "switchOnChange");
			evp.uCmdTag = info.cmdTag;
			ux.addHdl(_id(info.cId), "change", ux.post, evp);
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
	
	if (rgp.pMultiSelDepList) {
		ux.setDisabledById(rgp.pMultiSelDepList, rgp.pSelCount == 0);
	}
	
	if (rgp.pConDepList) {
		ux.setDisabledById(rgp.pConDepList, true);
	}
	
	if (rgp.pFocusId) {
		ux.setFocus({wdgid: rgp.pFocusId});
	}
}

fux.wireGroupClick = function(rgp, groupId, action) {
	const evp = fux.newCmdEvPrm(rgp, action);
	var elems = _name(groupId);
	for (var _elem of elems) {
		ux.addHdl(_elem, "click", ux.post, evp);
	}
}

fux.tableFixedMultiClick = function(rgp, id, action) {
	const evp = fux.newCmdEvPrm(rgp, action);
	ux.addHdl(_id(id), "click", ux.post, evp);
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
		
		if(checked && rigTbl.uMultiSelLink) {
			ux.post(uEv);
		} else {
			fux.tableDisableMultiSelElements(rigTbl);
		}
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
	var tbl = _id(id);
	if (!tbl) {
		// TODO Show some error
		return;
	}

	if (rgp.pMultiSel) {
		tbl.uSelAllId = rgp.pSelAllId;
		tbl.uSelCtrlId = rgp.pSelCtrlId;
		tbl.uMultiSelDepList = rgp.pMultiSelDepList;
		tbl.uChain = rgp.pLvlChain;
		tbl.uVisibleSel = 0;
		const active = !rgp.pDisabled;

		// Rig select
		const selAll = _id(rgp.pSelAllId);
		selAll._active = active;
		ux.cbWire(selAll);

		const selBoxes = _name(rgp.pSelCtrlId);
		tbl.uSelBoxes = selBoxes;

		const evp = {uRigTbl:tbl};
		const selAllFac = _id("fac_" + rgp.pSelAllId);
		selAllFac.selAll = selAll;
		ux.addHdl(selAllFac, "change", fux.tableSelAllClick, evp);

		
		for (var i = 0; i < selBoxes.length; i++) {
			var selBox = selBoxes[i];
			selBox._active = active;
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
		const evpNorm = fux.newCmdEvPrm(rgp, "normalize");
		const evpDel = fux.newCmdEvPrm(rgp, "delete");
		for (var i = 0; i < chgId.length; i++) {
			var idx = "d" + i;
			ux.addHdl(_id(chgId[i]), "change", ux.post, evpNorm);
			ux.addHdl(_id(delId + idx), "click", ux.post, evpDel);
		}
	}
}

fux.newCmdEvPrm = function(rgp, cmd) {
	var id = rgp.pId;
	var evp = ux.newEvPrm(rgp);
	evp.uCmd = id + "->" + cmd;
	evp.uRef = [ id ];
	evp.uPanels = [ rgp.pContId ];
	return evp;
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
	ux.setfn(fux.rigLineEntries,"fux09");  
	ux.setfn(fux.rigFilter,"fux0a");  
	ux.setfn(fux.rigMiniForm,"fux0b");  
	ux.setfn(fux.rigSearch,"fux0c");  
	ux.setfn(fux.rigSetValues,"fux0d");  
	ux.setfn(fux.rigTable,"fux0e");  
	ux.setfn(fux.rigTabSheet,"fux0f"); 
	ux.setfn(fux.rigChart,"fux10");
	ux.setfn(fux.rigWidgetRules,"fux11");
	ux.setfn(fux.rigInputArray,"fux12");  
	ux.setfn(fux.rigPopupWinText,"fux13"); 
	ux.setfn(fux.rigTokenEntries,"fux14");  
	ux.setfn(fux.rigFormAnnotation,"fux15"); 
	ux.setfn(fux.rigCollapsibleTable,"fux16");  
	ux.setfn(fux.rigEntityComposition,"fux17");  
}

fux.init();
