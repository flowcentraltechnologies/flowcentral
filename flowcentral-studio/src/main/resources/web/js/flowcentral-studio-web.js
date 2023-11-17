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

/**
 * flowCentral studio JavaScript functions.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
const fuxstudio = {};
fuxstudio.dragEvp = null;

const TAB_MINIFORM = "MINIFORM";
const TAB_MINIFORM_CHANGELOG = "MINIFORM_CHANGELOG";

/********************************************************************************/
/************************* STUDIO MENU ******************************************/
/********************************************************************************/

/** Studio Menu */
fuxstudio.rigStudioMenu = function(rgp) {
	const id = rgp.pId;
	const menuCat = rgp.pMenuCat;
	if (menuCat) {
		const categoryId = rgp.pCategoryId;
		for (var i = 0; i < menuCat.length; i++) {
			const _menucat = menuCat[i];
			const evp = ux.newEvPrm(rgp);
			evp.uCmd = id + "->switchState";
			evp.uPanels = [ rgp.pContId ];
			evp.uSelCtrlId = rgp.pCurrSelCtrlId;
			evp.uRef = [ rgp.pCurrSelCtrlId ];
			evp.uType = _menucat.type;

			ux.addHdl(_id(categoryId + _menucat.index), "click",
					fuxstudio.smClickHandler, evp);
		}
	}

	const menuItems = rgp.pMenuItems;
	if (menuItems) {
		for (var i = 0; i < menuItems.length; i++) {
			const mItem = menuItems[i];
			const evp = {uMain:false, uOpenPath:mItem.path, uIsDebounce:true};
			ux.addHdl(_id(mItem.id), "click", ux.menuOpenPath, evp);
		}
	}
}

fuxstudio.smClickHandler = function(uEv) {
	const evp = uEv.evp;
	_id(evp.uSelCtrlId).value = evp.uType;
	ux.post(uEv);
}

/********************************************************************************/
/************************* FORM EDITOR ******************************************/
/********************************************************************************/
/** Form editor */
const formColumnsType = new Map([["1", {columns:1}],
								["2", {columns:2}],
								["2-1", {columns:3}],
								["1-2", {columns:3}],
								["3", {columns:3}],
								["4", {columns:4}]]);

fuxstudio.rigFormEditor = function(rgp) {
	const id = rgp.pId;
	const content = rgp.pContent;

	const editor = {};
	editor.designId = rgp.pDsnBaseId;
	editor.choicesId = rgp.pFieldBaseId;
	editor.editFieldId = rgp.pEditFieldId;
	editor.stateId = rgp.pStateId;
	editor.placeId = "place_" + id;
	editor.tabId = "tab_" + id;
	editor.tabAddBtnId = "add_" + editor.tabId;
	editor.tabEditBtnId = "edit_" + editor.tabId;
	editor.tabDelBtnId = "del_" + editor.tabId;
	editor.tabAddSecBtnId = "addsec_" + editor.tabId;
	editor.tabDragId = "drag_" + editor.tabId;
	editor.tabCount = content.design.tabs.length;
	editor.tabLabels = content.tabLabels;
	editor.downarrow = content.downarrow;
	editor.addtab = content.addtab;
	editor.addsec = content.addsec;
	editor.plus = content.plus;
	editor.cog = content.cog;
	editor.cross = content.cross;
	editor.editable = content.editable;
	editor.placeitem = content.placetab;	
	editor.additem = content.addfield;	
	editor.tabtitle = content.tabtitle;	
	editor.sectiontitle = content.sectiontitle;	
	editor.choice = content.fields;
	editor.tabInfo = [];	
	editor.ctrl = {uEditTabId: rgp.pEditTabId, uEditSecId: rgp.pEditSecId, uEditFieldId: rgp.pEditFieldId,
			uEditColId: rgp.pEditColId, uEditIndexId :rgp.pEditIndexId, uEditModeId: rgp.pEditModeId};
	
	editor.changeState = function() {
		const usedChoice = new Set();
		const tabs = [];
		const _design = _id(this.designId);
		for(var i = 0; i < _design.children.length; i++) {
			const tabdiv = _design.children[i];
			const tab = tabdiv.tabInfo.tab;
			if (tabdiv.tabInfo.designId) {
				tab.sections = [];
				const _secdesign = _id(tabdiv.tabInfo.designId);
				for(var j = 0; j < _secdesign.children.length; j++) {
					const secdiv = _secdesign.children[j];
					const sectionInfo = secdiv.sectionInfo;
					const section = sectionInfo.section;
					section.fields = [];
					for(var k = 0; k < sectionInfo.designId.length; k++) {
						const _flddesign = _id(sectionInfo.designId[k]);
						for(var l = 0; l < _flddesign.children.length; l++) {
							const field = _flddesign.children[l].fieldInfo.field;
							section.fields.push(field);
							usedChoice.add(field.name);
						}
					}
					
					tab.sections.push(section);
				}
			}
			
			tabs.push(tab);
		}

		this.tabs = tabs;
		_id(this.stateId).value = JSON.stringify({tabs:this.tabs});

		for(var i = 0; i < this.choice.length; i++) {
			const fieldInfo = this.choice[i];
			const _field = _id(fieldInfo.fldId);
			if (usedChoice.has(fieldInfo.fldNm)) {
				_field.style.display = "none";
			} else {
				_field.style.display = "block";
			}
		}
	};
	
	const tevp = ux.newEvPrm(rgp);
	tevp.uCmd = id + "->editTab";
	tevp.uRef = [ id, rgp.pEditTabId, rgp.pEditModeId ];
	editor.tabevp = tevp;

	const sevp = ux.newEvPrm(rgp);
	sevp.uCmd = id + "->editSection";
	sevp.uRef = [ id, rgp.pEditTabId, rgp.pEditSecId, rgp.pEditModeId ];
	editor.secevp = sevp;

	const fevp = ux.newEvPrm(rgp);
	fevp.uCmd = id + "->editField";
	fevp.uRef = [ id, rgp.pEditTabId, rgp.pEditSecId, rgp.pEditFieldId, rgp.pEditColId, rgp.pEditIndexId, rgp.pEditModeId ];
	editor.fevp = fevp;
	
	const choiceId = rgp.pChoiceId;
	for(var i = 0; i < content.fields.length; i++) {
		const fieldInfo = content.fields[i];
		fieldInfo.fldId = choiceId + i;

		if (editor.editable) {
			const evp = {};
			evp.editor = editor;
			evp.fieldInfo = fieldInfo;
			ux.addHdl(_id(fieldInfo.fldId), "mousedown", fuxstudio.frmChoiceDragStart, evp);
		}
	}
	
	if (editor.tabCount > 0) {
		const design = _id(editor.designId);
		for (var i = 0; i < editor.tabCount; i++) {			
			const evp = fuxstudio.frmNewTabParam(editor, i);
			evp.tabInfo.tab = content.design.tabs[i];
			const tabdiv = fuxstudio.frmCreateTab(evp);
			design.appendChild(tabdiv);
			fuxstudio.frmWireTab(evp);
		}
	}

	editor.changeState();
}

fuxstudio.frmNewTabParam = function(editor, i) {
	const tabInfo = {};
	tabInfo.index = i;
	tabInfo.tabId = editor.tabId + i;
	tabInfo.addBtnId = editor.tabAddBtnId + i;
	tabInfo.editBtnId = editor.tabEditBtnId + i;
	tabInfo.delBtnId = editor.tabDelBtnId + i;
	tabInfo.addSecBtnId = editor.tabAddSecBtnId + i;
	tabInfo.dragId = editor.tabDragId + i;
	tabInfo.sectionCounter = -1;
	tabInfo.sectionInfo = [];

	const evp = {};
	evp.editor = editor;
	evp.tabInfo = tabInfo;
	editor.tabInfo.push(tabInfo);
	return evp;
}

fuxstudio.frmWireTab = function(evp) {
	const tabInfo = evp.tabInfo;
	const editor = evp.editor;
	_id(tabInfo.tabId).tabInfo = tabInfo;
	if (!tabInfo.stick) {
		if (editor.editable) {
			ux.addHdl(_id(tabInfo.dragId), "mousedown", fuxstudio.frmTabDragStart, evp);
		}
	}

	ux.addHdl(_id(tabInfo.addBtnId), "click", fuxstudio.frmTabAdd, evp);
	if (!tabInfo.frozen) {
		if (tabInfo.allowSec) {
			ux.addHdl(_id(tabInfo.addSecBtnId), "click", fuxstudio.frmTabSecAdd, evp);
		}

		ux.addHdl(_id(tabInfo.editBtnId), "click", fuxstudio.frmTabEdit, evp);
		if (editor.editable) {
			ux.addHdl(_id(tabInfo.delBtnId), "click", fuxstudio.frmTabDel, evp);
		}
	}
	
	for (var i = 0; i < tabInfo.sectionInfo.length; i++) {
		const sevp = {};
		sevp.editor = editor;
		sevp.tabInfo = tabInfo;
		sevp.sectionInfo = tabInfo.sectionInfo[i];
		fuxstudio.frmWireSection(sevp);
	}
}

fuxstudio.frmTabAdd = function(uEv) {
	fuxstudio.frmTabCrud(uEv, "create");
}

fuxstudio.frmTabSecAdd = function(uEv) {
	fuxstudio.frmTabCrud(uEv, "create_sub");
}

fuxstudio.frmTabEdit = function(uEv) {
	fuxstudio.frmTabCrud(uEv, "update");
}

fuxstudio.frmTabDel = function(uEv) {
	fuxstudio.frmTabCrud(uEv, "delete");
}

fuxstudio.frmTabCrud = function(uEv, mode) {
	const evp = uEv.evp;
	const editor = evp.editor;
	_id(editor.ctrl.uEditModeId).value = mode;
	_id(editor.ctrl.uEditTabId).value = evp.tabInfo.tab.name;
	ux.post({evp: editor.tabevp});
}

fuxstudio.frmWireSection = function(evp) {
	const sectionInfo = evp.sectionInfo;
	const editor = evp.editor;
	_id(sectionInfo.secId).sectionInfo = sectionInfo;
	if (!sectionInfo.frozen) {
		if (editor.editable) {
			ux.addHdl(_id(sectionInfo.dragId), "mousedown", fuxstudio.frmSecDragStart, evp);
		}		
		ux.addHdl(_id(sectionInfo.addId), "click", fuxstudio.frmSecAdd, evp);
		ux.addHdl(_id(sectionInfo.editId), "click", fuxstudio.frmSecEdit, evp);
		if (editor.editable) {
			ux.addHdl(_id(sectionInfo.delId), "click", fuxstudio.frmSecDel, evp);
		}
	}

	for (var i = 0; i < sectionInfo.fieldInfo.length; i++) {
		const fevp = {};
		fevp.editor = editor;
		fevp.sectionInfo = sectionInfo;
		fevp.fieldInfo = sectionInfo.fieldInfo[i];
		fuxstudio.frmWireField(fevp);
	}
}

fuxstudio.frmSecAdd = function(uEv) {
	fuxstudio.frmSecCrud(uEv, "create");
}

fuxstudio.frmSecEdit = function(uEv) {
	fuxstudio.frmSecCrud(uEv, "update");
}

fuxstudio.frmSecDel = function(uEv) {
	fuxstudio.frmSecCrud(uEv, "delete");
}

fuxstudio.frmSecCrud = function(uEv, mode) {
	const evp = uEv.evp;
	const editor = evp.editor;
	_id(editor.ctrl.uEditModeId).value = mode;
	_id(editor.ctrl.uEditTabId).value = evp.sectionInfo.tabName;
	_id(editor.ctrl.uEditSecId).value = evp.sectionInfo.section.name;
	ux.post({evp: editor.secevp});
}

fuxstudio.frmWireField = function(evp) {
	const fieldInfo = evp.fieldInfo;
	const editor = evp.editor;
	_id(fieldInfo.fldId).fieldInfo = fieldInfo;
	if (!fieldInfo.frozen) {
		if (editor.editable) {
			ux.addHdl(_id(fieldInfo.dragId), "mousedown", fuxstudio.frmFieldDragStart, evp);
		}
		ux.addHdl(_id(fieldInfo.editId), "click", fuxstudio.frmFieldEdit, evp);
		if (editor.editable) {
			ux.addHdl(_id(fieldInfo.delId), "click", fuxstudio.frmFieldDel, evp);
		}
	}
}

fuxstudio.frmFieldAdd = function(evp, column, index) {
	const editor = evp.editor;
	_id(editor.ctrl.uEditColId).value = column;
	_id(editor.ctrl.uEditIndexId).value = index;
	fuxstudio.frmFieldCrud(evp, "create");
}

fuxstudio.frmFieldEdit = function(uEv) {
	fuxstudio.frmFieldCrud(uEv.evp, "update");
}

fuxstudio.frmFieldDel = function(uEv) {
	fuxstudio.frmFieldCrud(uEv.evp, "delete");
}

fuxstudio.frmFieldCrud = function(evp, mode) {
	const editor = evp.editor;
	_id(editor.ctrl.uEditModeId).value = mode;
	_id(editor.ctrl.uEditTabId).value = evp.sectionInfo.tabName;
	_id(editor.ctrl.uEditSecId).value = evp.sectionInfo.section.name;
	_id(editor.ctrl.uEditFieldId).value = evp.fieldInfo.field.name;
	ux.post({evp: editor.fevp});
}

fuxstudio.frmCreateTab = function(evp) {
	const editor = evp.editor;
	const tabInfo = evp.tabInfo;
	const tab = tabInfo.tab;
	const caption = tab.label ? tab.label:"&nbsp;";
	const labels = editor.tabLabels;
	var html = null;
	tabInfo.frozen = tab.contentType == TAB_MINIFORM_CHANGELOG;
	tabInfo.allowSec = tab.contentType == TAB_MINIFORM;
	tabInfo.stick = tabInfo.index == 0;
	if (tabInfo.stick) {
		html = "<div class=\"tabhdr\">"
			+ "<div class=\"elcap\" id=\"" + tabInfo.dragId + "\"><span>" + editor.tabtitle + " [" + tab.contentType + " - " + caption + "]</span></div>"
			+ "<div class=\"elact\">";
	    if (editor.editable) {
	    	html += fuxstudio.editorTextButton(tabInfo.addBtnId, editor.addtab);
	    }
	    
		if (tabInfo.allowSec) {
		    if (editor.editable) {
		    	html += fuxstudio.editorTextButton(tabInfo.addSecBtnId, editor.addsec)
		    }
		}
			
		html += fuxstudio.editorSymButton(tabInfo.editBtnId, editor.cog, "actbtn")
        html += "</div></div>";
	} else {
		html = "<div class=\"tabhdr\">"
			+ "<div class=\"elcap grab\" id=\"" + tabInfo.dragId + "\"><span>" + editor.tabtitle + " [" + tab.contentType + " - " + caption + "]</span></div>"
			+ "<div class=\"elact\">";
	    if (editor.editable) {
	    	html += fuxstudio.editorTextButton(tabInfo.addBtnId, editor.addtab);
	    }
		if (!tabInfo.frozen) {
			if (tabInfo.allowSec) {
			    if (editor.editable) {
					html += fuxstudio.editorTextButton(tabInfo.addSecBtnId, editor.addsec)
			    }
			}
			
			html += fuxstudio.editorSymButton(tabInfo.editBtnId, editor.cog, "actbtn");
		    if (editor.editable) {
		    	html += fuxstudio.editorSymButton(tabInfo.delBtnId,  editor.cross, "actbtn");
		    }
		}
	    html += "</div></div>";
	}

	if(tab.contentType == TAB_MINIFORM || tab.contentType == TAB_MINIFORM_CHANGELOG) {
		tabInfo.designId = "secdesign_" + tabInfo.tabId;
		if(tabInfo.frozen) {
			html += "<div class=\"secdesign secdesignz\" id=\"" + tabInfo.designId + "\">"
		} else {
			html += "<div class=\"secdesign\" id=\"" + tabInfo.designId + "\">"
		}
		for (var i = 0; i < tab.sections.length; i++) {
			const sectionInfo = fuxstudio.frmCreateSectionInfo(tabInfo, i);
			html += fuxstudio.frmSectionHtml(editor, sectionInfo);
		}
	    html += "</div>";
	} else {
		html += "<div class=\"secdesign secdesignz\"></div>"
	}
    
	const tabdiv = document.createElement("div");
	tabdiv.className = "tab";
	tabdiv.id = tabInfo.tabId;
	tabdiv.innerHTML = html;
	tabdiv.stick = tabInfo.stick;
	return tabdiv;
}

fuxstudio.frmCreateSectionInfo = function(tabInfo, index) {
	const sectionInfo = {};
	sectionInfo.tabName = tabInfo.tab.name;
	sectionInfo.index = index;
	sectionInfo.secId = "sec_" + tabInfo.tabId + (++tabInfo.sectionCounter);
	sectionInfo.dragId = "drag_" + sectionInfo.secId;
	sectionInfo.addId = "add_" + sectionInfo.secId;
	sectionInfo.editId = "edit_" + sectionInfo.secId;
	sectionInfo.delId = "del_" + sectionInfo.secId;
	sectionInfo.frozen = tabInfo.frozen;
	sectionInfo.section = tabInfo.tab.sections[index];
	tabInfo.sectionInfo.push(sectionInfo);
	return sectionInfo;
}

fuxstudio.frmSectionHtml = function(editor, sectionInfo) {
	return "<div class=\"sec\" id=\"" + sectionInfo.secId + "\">"
				+ fuxstudio.frmSectionInnerHtml(editor, sectionInfo)
				+ "</div>";
}

fuxstudio.frmSectionInnerHtml = function(editor, sectionInfo) {
	const caption = sectionInfo.section.label ? sectionInfo.section.label:"&nbsp;";
	var html = null;
	if (sectionInfo.frozen) {
		html = "<div class=\"sechdr\">"
			+ "<div class=\"elcap\"><span>" + editor.sectiontitle + " [" + caption + "]</span></div>"
			+ "<div class=\"elact\">"
	        + "</div></div>";
	} else {
		html = "<div class=\"sechdr\">"
			+ "<div class=\"elcap grab\" id=\"" + sectionInfo.dragId + "\"><span>" + editor.sectiontitle + " [" + caption + "]</span></div>"
			+ "<div class=\"elact\">";
		if (editor.editable) {
			html += fuxstudio.editorTextButton(sectionInfo.addId, editor.addsec);
		}
		html += fuxstudio.editorSymButton(sectionInfo.editId, editor.cog, "actbtn");
		if (editor.editable) {
			html += fuxstudio.editorSymButton(sectionInfo.delId,  editor.cross, "actbtn");
		}
	    html += "</div></div>";
	}
	sectionInfo.fieldCounter = -1;
	sectionInfo.fieldInfo = [];
	sectionInfo.designId = [];
	const section = sectionInfo.section;
	const designRowId = "flddesign_" + sectionInfo.secId;
	html += "<div style=\"display:table;table-layout:fixed;width:100%;\"><div id=\""
			+ designRowId + "\" style=\"display:table-row;\">";
	const colinfo = formColumnsType.get(section.columns);
	for (var i = 0; i < colinfo.columns; i++) {
		const designId = designRowId + i;
		html += "<div style=\"display:table-cell;vertical-align:top;\"><div class=\"flddesign\" id=\"" + designId + "\">";
		for (var j = 0; j < section.fields.length; j++) {
			const field = section.fields[j];
			if (field.column == i) {
				const fieldInfo = fuxstudio.frmCreateFieldInfo(sectionInfo, j);
				html += fuxstudio.frmFieldHtml(editor, fieldInfo);
			}
		}
		html += "</div></div>";
		sectionInfo.designId.push(designId);
	}

	html += "</div></div>";
	return html;
}

fuxstudio.frmCreateFieldInfo = function(sectionInfo, index) {
	const fieldInfo = {};
	fieldInfo.index = index;
	fieldInfo.fldId = "fld_" + sectionInfo.secId + (++sectionInfo.fieldCounter);
	fieldInfo.dragId = "drag_" + fieldInfo.fldId;
	fieldInfo.editId = "edit_" + fieldInfo.fldId;
	fieldInfo.delId = "del_" + fieldInfo.fldId;
	fieldInfo.frozen = sectionInfo.frozen;
	fieldInfo.field = sectionInfo.section.fields[index];
	sectionInfo.fieldInfo.push(fieldInfo);
	return fieldInfo;
}

fuxstudio.frmFieldHtml = function(editor, fieldInfo) {
	return "<div class=\"fld\" id=\"" + fieldInfo.fldId + "\">"
			+ fuxstudio.frmFieldInnerHtml(editor, fieldInfo)
		    + "</div>";
}

fuxstudio.frmFieldInnerHtml = function(editor, fieldInfo) {
	const field = fieldInfo.field;
	const caption = field.label ? field.label: field.fldLabel;
	if (fieldInfo.frozen) {
		return "<div class=\"fldhdr\">"
			+ "<div class=\"fldcap fldcapz\"><span>" + caption + "</span></div>"
		    + "</div>";
	}
	
	var html = "<div class=\"fldhdr\">"
			+ "<div class=\"fldcap grab\" id=\"" + fieldInfo.dragId + "\"><span>" + caption + "</span></div>"
			+ "<div class=\"fldact\">"
			+ fuxstudio.editorSymButton(fieldInfo.editId, editor.cog, "fldbtn");
	if (editor.editable) {
		html += fuxstudio.editorSymButton(fieldInfo.delId,  editor.cross, "fldbtn");
	}
	html += "</div></div>";
	return html;
}

fuxstudio.frmChoiceDragStart = function(uEv) {
	const evp = uEv.evp;
	const editor = evp.editor;
	const sectionInfo = fuxstudio.frmFindFirstSection(editor);
	if (sectionInfo) {
		evp.newSectionInfo = {sectionInfo:sectionInfo, column:0};
		fuxstudio.editorChoiceDragStart(
				evp, uEv.uTrg, sectionInfo.designId[0], uEv.clientX,
				uEv.clientY, "placefield", fuxstudio.frmChoiceDrag,
				fuxstudio.frmChoiceDragEnd);
	}
}

fuxstudio.frmChoiceDrag = function(uEv) {
	const _drag = fuxstudio.editorChoiceDrag(uEv);
	const evp = fuxstudio.dragEvp;
	const editor = evp.editor;
	const _sectionInfo = fuxstudio.editorSectionInfoAt(editor.tabInfo, uEv.clientX, uEv.clientY);
	if (_sectionInfo && fuxstudio.editorEvaluateDrag(editor, _sectionInfo.sectionInfo.designId[_sectionInfo.column],
					_drag.id, uEv.clientX, uEv.clientY)) {
		evp.newSectionInfo = _sectionInfo;
	}
}

fuxstudio.frmChoiceDragEnd = function(uEv) {
	const evp = fuxstudio.dragEvp;
	const _sectionInfo = evp.newSectionInfo.sectionInfo;
	const _column = evp.newSectionInfo.column;
	const _editor = evp.editor;
	const _placeslot = _id(_editor.placeId);
	var _index = -1;
	
	const _design = _id(_sectionInfo.designId[_column]);
	const _drop = fuxstudio.editorDesignSlotAt(_design, null, uEv.clientX, uEv.clientY);
	if (_drop && (_drop.slot.id == _editor.placeId)) {
		_index = _drop.index;
	}
	
	_design.removeChild(_placeslot);
	const _fields = _id(_editor.choicesId);
	const _field = _id(evp.dragOriginId);
	_fields.removeChild(_field);
	
	ux.remDirectHdl(document, "mousemove", fuxstudio.frmChoiceDrag);
	ux.remDirectHdl(document, "mouseup", fuxstudio.frmChoiceDragEnd);
	fuxstudio.dragEvp = null;
	
	if (_index >= 0) {
		const fevp = {};
		fevp.editor = _editor;
		fevp.sectionInfo = _sectionInfo;
		fevp.fieldInfo = {field:{name: evp.fieldInfo.fldNm}};
		fuxstudio.frmFieldAdd(fevp, _column, _index);
	}
}

fuxstudio.frmFindFirstSection = function(editor) {
	const _design = _id(editor.designId);
	for(var i = 0; i < _design.children.length; i++) {
		const tabdiv = _design.children[i];
		if (tabdiv.tabInfo.designId) {
			const _secdesign = _id(tabdiv.tabInfo.designId);
			for(var j = 0; j < _secdesign.children.length; j++) {
				const secdiv = _secdesign.children[j];
				if (secdiv.sectionInfo) {
					return secdiv.sectionInfo;
				}
			}
		}
	}
}

fuxstudio.frmTabDragStart = function(uEv) {
	const evp = uEv.evp;
	fuxstudio.editorDragStart(evp, "tab", evp.editor.designId, evp.tabInfo.tabId,
			uEv.clientX, uEv.clientY, "placetab", fuxstudio.frmTabDrag, fuxstudio.frmTabDragEnd);
}

fuxstudio.frmTabDrag = function(uEv) {
	const _drag = fuxstudio.editorChoiceDrag(uEv);
	const evp = fuxstudio.dragEvp;
	const editor = evp.editor;

	const _design = _id(editor.designId);
	const _drop = fuxstudio.editorDesignSlotAt(_design, _drag.id, uEv.clientX, uEv.clientY);
	if (_drop && !_drop.slot.stick && _drop.slot.id != editor.placeId) {
		const _placeslot = _id(editor.placeId);
		const dbox = _drop.slot.getBoundingClientRect();
		const pbox = _placeslot.getBoundingClientRect();
		if (pbox.top > dbox.top) {
			if (uEv.clientY < (dbox.top + dbox.height/2)) {
				_design.insertBefore(_placeslot, _drop.slot);
			}
		} else {
			if (uEv.clientY > (dbox.top + dbox.height/2)) {
				if (_drop.slot.nextSibling) {
					_design.insertBefore(_placeslot, _drop.slot.nextSibling);
				} else {
					_design.appendChild(_placeslot);
				}
			}
		}			
	}
}

fuxstudio.frmTabDragEnd = function(uEv) {
	const evp = fuxstudio.dragEvp;
	fuxstudio.editorDragEnd(evp, "tab", evp.editor.designId,
			fuxstudio.frmTabDrag, fuxstudio.frmTabDragEnd);
}

fuxstudio.frmSecDragStart = function(uEv) {
	const evp = uEv.evp;
	evp.newTabInfo = evp.tabInfo;
	fuxstudio.editorDragStart(evp, "section", evp.tabInfo.designId, evp.sectionInfo.secId,
			uEv.clientX, uEv.clientY, "placetab", fuxstudio.frmSecDrag, fuxstudio.frmSecDragEnd);
}

fuxstudio.frmSecDrag = function(uEv) {
	const _drag = fuxstudio.editorChoiceDrag(uEv);
	const evp = fuxstudio.dragEvp;
	const editor = evp.editor;

	const _tabInfo = fuxstudio.editorTabInfoAt(editor.tabInfo, uEv.clientX, uEv.clientY);
	if (_tabInfo && fuxstudio.editorEvaluateDrag(editor, _tabInfo.designId, _drag.id, uEv.clientX,
			uEv.clientY)) {
		evp.newTabInfo = _tabInfo;
	}
}

fuxstudio.frmSecDragEnd = function(uEv) {
	const evp = fuxstudio.dragEvp;
	if (evp.newTabInfo != evp.tabInfo) {
		fuxstudio.editorArrayRemove(evp.tabInfo.sectionInfo, evp.sectionInfo);
		evp.newTabInfo.sectionInfo.push(evp.sectionInfo);
		evp.tabInfo = evp.newTabInfo;
	}

	fuxstudio.editorDragEnd(evp, "section", evp.newTabInfo.designId,
			fuxstudio.frmSecDrag, fuxstudio.frmSecDragEnd);
}

fuxstudio.frmFieldDragStart = function(uEv) {
	const evp = uEv.evp;
	const fieldInfo = evp.fieldInfo;
	evp.newSectionInfo = evp.sectionInfo;
	fuxstudio.editorDragStart(
			evp, "field", evp.sectionInfo.designId[fieldInfo.field.column],
			evp.fieldInfo.fldId, uEv.clientX, uEv.clientY, "placefield",
			fuxstudio.frmFieldDrag, fuxstudio.frmFieldDragEnd);
}

fuxstudio.frmFieldDrag = function(uEv) {
	const _drag = fuxstudio.editorChoiceDrag(uEv);
	const evp = fuxstudio.dragEvp;
	const editor = evp.editor;

	const _sectionInfo = fuxstudio.editorSectionInfoAt(editor.tabInfo, uEv.clientX, uEv.clientY);
	if (_sectionInfo && fuxstudio.editorEvaluateDrag(editor, _sectionInfo.sectionInfo.designId[_sectionInfo.column],
					_drag.id, uEv.clientX, uEv.clientY)) {
		evp.newSectionInfo = _sectionInfo.sectionInfo;
		evp.fieldInfo.field.column = _sectionInfo.column;
	}
}

fuxstudio.frmFieldDragEnd = function(uEv) {
	const evp = fuxstudio.dragEvp;
	if (evp.newSectionInfo != evp.sectionInfo) {
		fuxstudio.editorArrayRemove(evp.sectionInfo.fieldInfo, evp.fieldInfo);	
		evp.newSectionInfo.fieldInfo.push(evp.fieldInfo);
		evp.sectionInfo = evp.newSectionInfo;
	}

	fuxstudio.editorDragEnd(evp, "field", evp.sectionInfo.designId[evp.fieldInfo.field.column],
			fuxstudio.frmFieldDrag, fuxstudio.frmFieldDragEnd);
}

/********************************************************************************/
/************************* TABLE EDITOR *****************************************/
/********************************************************************************/

/** Table column editor */
fuxstudio.rigTableColumnEditorPanel = function(rgp) {
	const id = rgp.pId;
	const tce = _id(id);
	tce._field = _id(rgp.pFieldId);
	tce._label = _id(rgp.pLabelId);
	tce._link = _id(rgp.pLinkId);
	tce._symbol = _id(rgp.pSymbolId);
	tce._order = _id(rgp.pOrderId);
	tce._widget = _id(rgp.pWidgetId);
	tce._width = _id(rgp.pWidthId);
	tce._switch = _id(rgp.pSwitchId);
	tce._hiddenOnNull = _id(rgp.pHiddenOnNullId);
	tce._hidden = _id(rgp.pHiddenId);
	tce._editable = _id(rgp.pEditableId);
	tce._disabled = _id(rgp.pDisabledId);
	tce._sortable = _id(rgp.pSortId);
	tce._summary = _id(rgp.pSummaryId);
	
	tce.attach = function(evp) {
		tce._evp = evp;
		const column = evp.column;
		tce._field.setValue(column.fldNm);
		tce._label.setValue(column.label);
		tce._link.setValue(column.link);
		tce._symbol.setValue(column.symbol);
		tce._order.setValue(column.order);
		tce._widget.setValue(column.widget);
		tce._width.setValue(column.width);
		tce._switch.setValue(column.switchOnChange);
		tce._hiddenOnNull.setValue(column.hiddenOnNull);
		tce._hidden.setValue(column.hidden);
		tce._editable.setValue(column.editable);
		tce._disabled.setValue(column.disabled);
		tce._sortable.setValue(column.sort);
		tce._summary.setValue(column.summary);
	};
	
	tce.apply = function() {
		if (tce._evp) {
			const column = tce._evp.column;
			column.label = tce._label.getValue();
			column.link = tce._link.getValue();
			column.symbol = tce._symbol.getValue();
			column.order = tce._order.getValue();
			column.widget = tce._widget.getValue();
			column.width = parseInt(tce._width.getValue());
			column.switchOnChange = tce._switch.getValue();
			column.hiddenOnNull = tce._hiddenOnNull.getValue();
			column.hidden = tce._hidden.getValue();
			column.editable = tce._editable.getValue();
			column.disabled = tce._disabled.getValue();
			column.sort = tce._sortable.getValue();
			column.summary = tce._summary.getValue();
			tce._evp.editor.changeState();
		}
	};
	
	tce.setFocus = function() {
		tce._label.focus();
	}
	
	const evp = {uId:id};
	ux.addHdl(_id(rgp.pApplyId), "click", fuxstudio.tceApplyHandler, evp);
	ux.addHdl(_id(rgp.pCancelId), "click", fuxstudio.tceCancelHandler, evp);
}

fuxstudio.tceApplyHandler = function(uEv) {
	const tce = _id(uEv.evp.uId);
	if (tce._evp) {
		tce.apply();
		tce.hide();
		fuxstudio.tblReplaceSlot(tce._evp);
	}
}

fuxstudio.tceCancelHandler = function(uEv) {
	const tce = _id(uEv.evp.uId);
	tce.hide();
}

/** Table editor */
fuxstudio.rigTableEditor = function(rgp) {
	const id = rgp.pId;
	const content = rgp.pContent;

	const editor = {};
	editor.choicesId = rgp.pFieldBaseId;
	editor.designId = rgp.pDsnBaseId;
	editor.editColId = rgp.pEditColId;
	editor.stateId = rgp.pStateId;
	editor.placeId = "place_" + id;
	editor.slotId = "slot_" + id;
	editor.editBtnId = "edit_" + editor.slotId;
	editor.delBtnId = "del_" + editor.slotId;
	editor.dragId = "drag_" + editor.slotId;
	editor.len = content.design.columns.length;
	editor.labels = content.propLabels;
	editor.downarrow = content.downarrow;
	editor.plus = content.plus;
	editor.cog = content.cog;
	editor.cross = content.cross;
	editor.editable = content.editable;
	editor.none = content.none;
	editor.placeitem = content.placecolumn;
	editor.additem = content.addcolumn;
	
	editor.changeState = function() {
		const columns = [];
		const _design = _id(this.designId);
		for(var i = 0; i < _design.children.length; i++) {
			columns.push(_design.children[i].column); 
		}
		
		const oldcolumns = this.columns;
		this.columns = columns;
		if (oldcolumns) {
			_id(this.stateId).value = JSON.stringify({columns:this.columns});
		}
	};

	editor.editSlotFieldHtml = function(label, val) {
		val = val ? val: this.none;
		return "<div class=\"itm\"><span class=\"itmlabel\">"
			+ label + "</span><span class=\"itmval\">" + val + "</span></div>";
	};
	
	if (editor.editable) {
		const choiceId = rgp.pChoiceId;
		for(var i = 0; i < content.fields.length; i++) {
			const evp = {};
			evp.editor = editor;
			evp.fieldId = choiceId + i;
			evp.fieldInfo = content.fields[i];
			ux.addHdl(_id(evp.fieldId), "mousedown", fuxstudio.tblFieldDragStart, evp);
		}
	}
	
	if (editor.len > 0) {
		const design = _id(editor.designId);
		for (var i = 0; i < editor.len; i++) {			
			const evp = fuxstudio.tblNewSlotParam(editor, i);
			evp.column = content.design.columns[i];
			const slot = fuxstudio.tblCreateSlot(evp);
			design.appendChild(slot);
			fuxstudio.tblWireSlot(evp);
		}
	}

	editor.changeState();
}

fuxstudio.tblNewSlotParam = function(editor, i) {
	const evp = {};
	evp.editor = editor;
	evp.index = i;
	evp.slotId = editor.slotId + i;
	evp.editBtnId = editor.editBtnId + i;
	evp.delBtnId = editor.delBtnId + i;
	evp.dragId = editor.dragId + i;
	return evp;
}

fuxstudio.tblWireSlot = function(evp) {
	const editor = evp.editor;
	if (editor.editable) {
		ux.addHdl(_id(evp.dragId), "mousedown", fuxstudio.tblSlotDragStart, evp);
		ux.addHdl(_id(evp.delBtnId), "click", fuxstudio.tblSlotDel, evp);
	}

	ux.addHdl(_id(evp.editBtnId), "click", fuxstudio.tblSlotEdit, evp);
}

fuxstudio.tblCreateSlot = function(evp) {
	const editor = evp.editor;
	const column = evp.column;
	const caption = column.label ? column.label: column.fldLabel;
	const labels = editor.labels;
	var html = "<div class=\"slothdr\">"
			+ "<div class=\"slotcap\" id=\"" + evp.dragId + "\"><span>" + caption + "</span></div>"
			+ "<div class=\"slotact\">"
			+ fuxstudio.editorSymButton(evp.editBtnId, editor.cog, "actbtn");
	if (editor.editable) {
		html += fuxstudio.editorSymButton(evp.delBtnId, editor.cross, "actbtn");
	}
	
	html += "</div></div>";
	html += editor.editSlotFieldHtml(labels[0], column.fldNm);
	html += editor.editSlotFieldHtml(labels[1], column.label);
	html += editor.editSlotFieldHtml(labels[2], column.link);
	html += editor.editSlotFieldHtml(labels[3], column.symbol);
	html += editor.editSlotFieldHtml(labels[4], column.order);
	html += editor.editSlotFieldHtml(labels[5], column.widget);
	html += editor.editSlotFieldHtml(labels[6], column.width);
	html += editor.editSlotFieldHtml(labels[7], "" + column.switchOnChange);
	html += editor.editSlotFieldHtml(labels[8], "" + column.hiddenOnNull);
	html += editor.editSlotFieldHtml(labels[9], "" + column.hidden);
	html += editor.editSlotFieldHtml(labels[10], "" + column.disabled);
	html += editor.editSlotFieldHtml(labels[11], "" + column.editable);
	html += editor.editSlotFieldHtml(labels[12], "" + column.sort);
	html += editor.editSlotFieldHtml(labels[13], "" + column.summary);

	const slot = document.createElement("div");
	slot.className = "slot";
	slot.id = evp.slotId;
	slot.innerHTML = html;
	slot.column = column;
	return slot;
}

fuxstudio.tblReplaceSlot = function(evp) {
	var nextslot = null;
	const design = _id(evp.editor.designId);
	for(var i = 0; i < design.children.length; i++) {
		const oldslot = design.children[i];
		if (oldslot.id == evp.slotId) {
			if ((++i) < design.children.length) {
				nextslot = design.children[i];
			}
			
			design.removeChild(oldslot);
			break;
		}
	}
	
	const slot = fuxstudio.tblCreateSlot(evp);
	if (nextslot) {
		design.insertBefore(slot, nextslot);
	} else {
		design.appendChild(slot);
	}
	fuxstudio.tblWireSlot(evp);
}


fuxstudio.tblFieldDragStart = function(uEv) {
	const evp = uEv.evp;
	const editor = evp.editor;
	fuxstudio.editorChoiceDragStart(
			evp, uEv.uTrg, editor.designId, uEv.clientX,
			uEv.clientY, "placeslot", fuxstudio.tblSlotDrag,
			fuxstudio.tblSlotDragEnd);
}

fuxstudio.tblSlotDragStart = function(uEv) {
	const evp = uEv.evp;
	fuxstudio.editorDragStart(evp, "slot", evp.editor.designId, evp.slotId,
			uEv.clientX, uEv.clientY, "placeslot", fuxstudio.tblSlotDrag, fuxstudio.tblSlotDragEnd);
}

fuxstudio.tblSlotDrag = function(uEv) {
	const _drag = fuxstudio.editorChoiceDrag(uEv);
	const evp = fuxstudio.dragEvp;
	const editor = evp.editor;

	const _design = _id(editor.designId);
	const _drop = fuxstudio.editorDesignSlotAt(_design, _drag.id, uEv.clientX, uEv.clientY);
	if (_drop && _drop.slot.id != editor.placeId) {
		const _placeslot = _id(editor.placeId);
		const x1 = _drop.slot.getBoundingClientRect().left;
		const x2 = _placeslot.getBoundingClientRect().left;
		if (x2 > x1) {
			_design.insertBefore(_placeslot, _drop.slot);
		} else {
			if (_drop.slot.nextSibling) {
				_design.insertBefore(_placeslot, _drop.slot.nextSibling);
			} else {
				_design.appendChild(_placeslot);
			}
		}			
	}
}

fuxstudio.tblSlotDragEnd = function(uEv) {
	const evp = fuxstudio.dragEvp;
	const _editor = evp.editor;
	const _design = _id(_editor.designId);
	const _placeslot = _id(_editor.placeId);

	if (evp.dragType == "slot") {
		const _slot = _id(evp.dragOriginId);
		_design.insertBefore(_slot, _placeslot);
		_design.removeChild(_placeslot);
		_slot.classList.remove("drag");
	} else {
		const _drop = fuxstudio.editorDesignSlotAt(_design, null, uEv.clientX, uEv.clientY);
		if (_drop && (_drop.slot.id == _editor.placeId)) {
			const column = {};
			column.fldLabel = evp.fieldInfo.fldLabel;
			column.fldNm = evp.fieldInfo.fldNm;
			column.label = null;
			column.link = null;
			column.symbol = null;
			column.widget = evp.fieldInfo.fldWidget?  evp.fieldInfo.fldWidget:"application.text";
			column.width = 1;
			column.sort = false;
			
			const nevp = fuxstudio.tblNewSlotParam(_editor, ++_editor.len);
			nevp.column = column;
			const nslot = fuxstudio.tblCreateSlot(nevp);
			_design.insertBefore(nslot, _drop.slot);
			fuxstudio.tblWireSlot(nevp);
		}
		
		_design.removeChild(_placeslot);
		const _fields = _id(_editor.choicesId);
		const _field = _id(evp.dragOriginId);
		_fields.removeChild(_field);
	}
	
	ux.remDirectHdl(document, "mousemove", fuxstudio.tblSlotDrag);
	ux.remDirectHdl(document, "mouseup", fuxstudio.tblSlotDragEnd);
	fuxstudio.dragEvp = null;
	
	_editor.changeState();
}

fuxstudio.tblSlotEdit = function(uEv) {
	const evp = uEv.evp;
	const slotedit = _id(evp.editor.editColId);
	slotedit.show(uEv.uTrg.id);
	slotedit.attach(evp);
	slotedit.setFocus();
}

fuxstudio.tblSlotDel = function(uEv) {
	const evp = uEv.evp;
	const editor = evp.editor;
	const slotedit = _id(editor.editColId);
	slotedit.hide();
	
	const _design = _id(editor.designId);
	const _slot = _id(evp.slotId);
	_design.removeChild(_slot);
	editor.changeState();
}


/********************************************************************************/
/************************* REPORT EDITOR *****************************************/
/********************************************************************************/

/** Report column editor */
fuxstudio.rigReportColumnEditorPanel = function(rgp) {
	const id = rgp.pId;
	const rce = _id(id);
	rce._field = _id(rgp.pFieldId);
	rce._order = _id(rgp.pOrderId);
	rce._widget = _id(rgp.pWidgetId);
	rce._horiz = _id(rgp.pHorizId);
	rce._vert = _id(rgp.pVertId);
	rce._desc = _id(rgp.pDescId);
	rce._formatter = _id(rgp.pFormatterId);
	rce._width = _id(rgp.pWidthId);
	rce._bold = _id(rgp.pBoldId);
	rce._group = _id(rgp.pGroupId);
	rce._groupnew = _id(rgp.pGroupNewId);
	rce._sum = _id(rgp.pSumId);
	
	rce.attach = function(evp) {
		rce._evp = evp;
		const column = evp.column;
		rce._field.setValue(column.fldNm);
		rce._horiz.setValue(column.horizAlign);
		rce._vert.setValue(column.vertAlign);
		rce._order.setValue(column.order);
		rce._widget.setValue(column.widget);
		rce._desc.setValue(column.description);
		rce._formatter.setValue(column.formatter);
		rce._width.setValue(column.width);
		rce._bold.setValue(column.bold);
		rce._group.setValue(column.group);
		rce._groupnew.setValue(column.groupOnNewPage);
		rce._sum.setValue(column.sum);
	};
	
	rce.apply = function() {
		if (rce._evp) {
			const column = rce._evp.column;
			column.horizAlign = rce._horiz.getValue();
			column.vertAlign = rce._vert.getValue();
			column.order = rce._order.getValue();
			column.widget = rce._widget.getValue();
			column.description = rce._desc.getValue();
			column.formatter = rce._formatter.getValue();
			column.width = parseInt(rce._width.getValue());
			column.bold = rce._bold.getValue();
			column.group = rce._group.getValue();
			column.groupOnNewPage = rce._groupnew.getValue();
			column.sum = rce._sum.getValue();
			rce._evp.editor.changeState();
		}
	};
	
	rce.setFocus = function() {
		rce._label.focus();
	}
	
	const evp = {uId:id};
	ux.addHdl(_id(rgp.pApplyId), "click", fuxstudio.rceApplyHandler, evp);
	ux.addHdl(_id(rgp.pCancelId), "click", fuxstudio.rceCancelHandler, evp);
}

fuxstudio.rceApplyHandler = function(uEv) {
	const rce = _id(uEv.evp.uId);
	if (rce._evp) {
		rce.apply();
		rce.hide();
		fuxstudio.rptReplaceSlot(rce._evp);
	}
}

fuxstudio.rceCancelHandler = function(uEv) {
	const rce = _id(uEv.evp.uId);
	rce.hide();
}

/** Report editor */
fuxstudio.rigReportEditor = function(rgp) {
	const id = rgp.pId;
	const content = rgp.pContent;

	const editor = {};
	editor.choicesId = rgp.pFieldBaseId;
	editor.designId = rgp.pDsnBaseId;
	editor.editColId = rgp.pEditColId;
	editor.stateId = rgp.pStateId;
	editor.placeId = "place_" + id;
	editor.slotId = "slot_" + id;
	editor.editBtnId = "edit_" + editor.slotId;
	editor.delBtnId = "del_" + editor.slotId;
	editor.dragId = "drag_" + editor.slotId;
	editor.len = content.design.columns.length;
	editor.labels = content.propLabels;
	editor.downarrow = content.downarrow;
	editor.plus = content.plus;
	editor.cog = content.cog;
	editor.cross = content.cross;
	editor.editable = content.editable;
	editor.none = content.none;
	editor.placeitem = content.placecolumn;
	editor.additem = content.addcolumn;
	
	editor.changeState = function() {
		const columns = [];
		const _design = _id(this.designId);
		for(var i = 0; i < _design.children.length; i++) {
			columns.push(_design.children[i].column); 
		}
		
		const oldcolumns = this.columns;
		this.columns = columns;
		if (oldcolumns) {
			_id(this.stateId).value = JSON.stringify({columns:this.columns});
		}
	};

	editor.editSlotFieldHtml = function(label, val) {
		val = val ? val: this.none;
		return "<div class=\"itm\"><span class=\"itmlabel\">"
			+ label + "</span><span class=\"itmval\">" + val + "</span></div>";
	};
	
	if (editor.editable) {
		const choiceId = rgp.pChoiceId;
		for(var i = 0; i < content.fields.length; i++) {
			const evp = {};
			evp.editor = editor;
			evp.fieldId = choiceId + i;
			evp.fieldInfo = content.fields[i];
			ux.addHdl(_id(evp.fieldId), "mousedown", fuxstudio.rptFieldDragStart, evp);
		}
	}
	
	if (editor.len > 0) {
		const design = _id(editor.designId);
		for (var i = 0; i < editor.len; i++) {			
			const evp = fuxstudio.rptNewSlotParam(editor, i);
			evp.column = content.design.columns[i];
			const slot = fuxstudio.rptCreateSlot(evp);
			design.appendChild(slot);
			fuxstudio.rptWireSlot(evp);
		}
	}

	editor.changeState();
}

fuxstudio.rptNewSlotParam = function(editor, i) {
	const evp = {};
	evp.editor = editor;
	evp.index = i;
	evp.slotId = editor.slotId + i;
	evp.editBtnId = editor.editBtnId + i;
	evp.delBtnId = editor.delBtnId + i;
	evp.dragId = editor.dragId + i;
	return evp;
}

fuxstudio.rptWireSlot = function(evp) {
	const editor = evp.editor;
	if (editor.editable) {
		ux.addHdl(_id(evp.dragId), "mousedown", fuxstudio.rptSlotDragStart, evp);
		ux.addHdl(_id(evp.delBtnId), "click", fuxstudio.rptSlotDel, evp);
	}

	ux.addHdl(_id(evp.editBtnId), "click", fuxstudio.rptSlotEdit, evp);
}

fuxstudio.rptCreateSlot = function(evp) {
	const editor = evp.editor;
	const column = evp.column;
	const caption = column.description ? column.description : column.fldLabel;
	const labels = editor.labels;
	var html = "<div class=\"slothdr\">"
			+ "<div class=\"slotcap\" id=\"" + evp.dragId + "\"><span>" + caption + "</span></div>"
			+ "<div class=\"slotact\">"
			+ fuxstudio.editorSymButton(evp.editBtnId, editor.cog, "actbtn");
	if (editor.editable) {
		html += fuxstudio.editorSymButton(evp.delBtnId, editor.cross, "actbtn");
	}
	
	html += "</div></div>";
	html += editor.editSlotFieldHtml(labels[0], column.fldNm);
	html += editor.editSlotFieldHtml(labels[1], column.order);
	html += editor.editSlotFieldHtml(labels[2], column.widget);
	html += editor.editSlotFieldHtml(labels[3], column.horizAlign);
	html += editor.editSlotFieldHtml(labels[4], column.vertAlign);
	html += editor.editSlotFieldHtml(labels[5], column.description);
	html += editor.editSlotFieldHtml(labels[6], column.formatter);
	html += editor.editSlotFieldHtml(labels[7], column.width);
	html += editor.editSlotFieldHtml(labels[8], "" + column.bold);
	html += editor.editSlotFieldHtml(labels[9], "" + column.group);
	html += editor.editSlotFieldHtml(labels[10], "" + column.groupOnNewPage);
	html += editor.editSlotFieldHtml(labels[11], "" + column.sum);

	const slot = document.createElement("div");
	slot.className = "slot";
	slot.id = evp.slotId;
	slot.innerHTML = html;
	slot.column = column;
	return slot;
}

fuxstudio.rptReplaceSlot = function(evp) {
	var nextslot = null;
	const design = _id(evp.editor.designId);
	for(var i = 0; i < design.children.length; i++) {
		const oldslot = design.children[i];
		if (oldslot.id == evp.slotId) {
			if ((++i) < design.children.length) {
				nextslot = design.children[i];
			}
			
			design.removeChild(oldslot);
			break;
		}
	}
	
	const slot = fuxstudio.rptCreateSlot(evp);
	if (nextslot) {
		design.insertBefore(slot, nextslot);
	} else {
		design.appendChild(slot);
	}
	fuxstudio.rptWireSlot(evp);
}


fuxstudio.rptFieldDragStart = function(uEv) {
	const evp = uEv.evp;
	const editor = evp.editor;
	fuxstudio.editorChoiceDragStart(
			evp, uEv.uTrg, editor.designId, uEv.clientX,
			uEv.clientY, "placeslot", fuxstudio.rptSlotDrag,
			fuxstudio.rptSlotDragEnd);
}

fuxstudio.rptSlotDragStart = function(uEv) {
	const evp = uEv.evp;
	fuxstudio.editorDragStart(evp, "slot", evp.editor.designId, evp.slotId,
			uEv.clientX, uEv.clientY, "placeslot", fuxstudio.rptSlotDrag, fuxstudio.rptSlotDragEnd);
}

fuxstudio.rptSlotDrag = function(uEv) {
	const _drag = fuxstudio.editorChoiceDrag(uEv);
	const evp = fuxstudio.dragEvp;
	const editor = evp.editor;

	const _design = _id(editor.designId);
	const _drop = fuxstudio.editorDesignSlotAt(_design, _drag.id, uEv.clientX, uEv.clientY);
	if (_drop && _drop.slot.id != editor.placeId) {
		const _placeslot = _id(editor.placeId);
		const x1 = _drop.slot.getBoundingClientRect().left;
		const x2 = _placeslot.getBoundingClientRect().left;
		if (x2 > x1) {
			_design.insertBefore(_placeslot, _drop.slot);
		} else {
			if (_drop.slot.nextSibling) {
				_design.insertBefore(_placeslot, _drop.slot.nextSibling);
			} else {
				_design.appendChild(_placeslot);
			}
		}			
	}
}

fuxstudio.rptSlotDragEnd = function(uEv) {
	const evp = fuxstudio.dragEvp;
	const _editor = evp.editor;
	const _design = _id(_editor.designId);
	const _placeslot = _id(_editor.placeId);

	if (evp.dragType == "slot") {
		const _slot = _id(evp.dragOriginId);
		_design.insertBefore(_slot, _placeslot);
		_design.removeChild(_placeslot);
		_slot.classList.remove("drag");
	} else {
		const _drop = fuxstudio.editorDesignSlotAt(_design, null, uEv.clientX, uEv.clientY);
		if (_drop && (_drop.slot.id == _editor.placeId)) {
			const column = {};
			column.fldLabel = evp.fieldInfo.fldLabel;
			column.fldNm = evp.fieldInfo.fldNm;
			column.order = null;
			column.horizAlign = null;
			column.vertAlign = null;
			column.widget = evp.fieldInfo.fldWidget?  evp.fieldInfo.fldWidget:"application.text";
			column.description = evp.fieldInfo.fldLabel;
			column.formatter = null;
			column.width = 0;
			column.bold = false;
			column.group = false;
			column.groupOnNewPage = false;
			column.sum = false;
			
			const nevp = fuxstudio.rptNewSlotParam(_editor, ++_editor.len);
			nevp.column = column;
			const nslot = fuxstudio.rptCreateSlot(nevp);
			_design.insertBefore(nslot, _drop.slot);
			fuxstudio.rptWireSlot(nevp);
		}
		
		_design.removeChild(_placeslot);
		const _fields = _id(_editor.choicesId);
		const _field = _id(evp.dragOriginId);
		_fields.removeChild(_field);
	}
	
	ux.remDirectHdl(document, "mousemove", fuxstudio.rptSlotDrag);
	ux.remDirectHdl(document, "mouseup", fuxstudio.rptSlotDragEnd);
	fuxstudio.dragEvp = null;
	
	_editor.changeState();
}

fuxstudio.rptSlotEdit = function(uEv) {
	const evp = uEv.evp;
	const slotedit = _id(evp.editor.editColId);
	slotedit.show(uEv.uTrg.id);
	slotedit.attach(evp);
	slotedit.setFocus();
}

fuxstudio.rptSlotDel = function(uEv) {
	const evp = uEv.evp;
	const editor = evp.editor;
	const slotedit = _id(editor.editColId);
	slotedit.hide();
	
	const _design = _id(editor.designId);
	const _slot = _id(evp.slotId);
	_design.removeChild(_slot);
	editor.changeState();
}

/********************************************************************************/
/************************* DASHBOARD EDITOR *************************************/
/********************************************************************************/
/** Dashboard editor */
const dashColumnsType = new Map([["1", {columns:1}],
								["1_1", {columns:2}],
								["1_2", {columns:2}],
								["2_1", {columns:2}],
								["1_1_1", {columns:3}],
								["1_3", {columns:2}],
								["1_1_2", {columns:3}],
								["1_2_1", {columns:3}],
								["2_1_1", {columns:3}],
								["3_1", {columns:2}],
								["1_1_1_1", {columns:4}]]);

fuxstudio.rigDashboardEditor = function(rgp) {
	const id = rgp.pId;
	const content = rgp.pContent;

	const editor = {};
	editor.designId = rgp.pDsnBaseId;
	editor.choicesId = rgp.pChartBaseId;
	editor.editFieldId = rgp.pEditFieldId;
	editor.stateId = rgp.pStateId;
	editor.placeId = "place_" + id;
	editor.secId = "sec_" + id;
	editor.secAddBtnId = "add_" + editor.secId;
	editor.secEditBtnId = "edit_" + editor.secId;
	editor.secDelBtnId = "del_" + editor.secId;
	editor.secDragId = "drag_" + editor.secId;
	editor.secCount = content.design.sections.length;
	editor.secLabels = content.sectionLabels;
	editor.downarrow = content.downarrow;
	editor.addsec = content.addsec;
	editor.plus = content.plus;
	editor.cog = content.cog;
	editor.cross = content.cross;
	editor.editable = content.editable;
	editor.placeitem = content.placetab;	
	editor.additem = content.addtile;	
	editor.sectiontitle = content.sectiontitle;	
	editor.choice = content.charts;
	editor.secInfo = [];	
	editor.ctrl = {uEditSecId: rgp.pEditSecId, uEditTileId: rgp.pEditTileId,
			uEditIndexId :rgp.pEditIndexId, uEditTileIndexId :rgp.pEditTileIndexId,
			uEditModeId: rgp.pEditModeId, uEditMoveId: rgp.pEditMoveId};
	
	editor.changeState = function() {
		const usedChoice = new Set();
		const sections = [];
		const _design = _id(this.designId);
		for(var i = 0; i < _design.children.length; i++) {
			const secdiv = _design.children[i];
			const secInfo = secdiv.secInfo;
			const section = secInfo.section;
			if (secInfo.designId) {
				section.tiles = [];
				for(var k = 0; k < secInfo.designId.length; k++) {
					const _tledesign = _id(secInfo.designId[k]);
					for(var l = 0; l < _tledesign.children.length; l++) {
						if (_tledesign.children[l].tileInfo) {
							const tile = _tledesign.children[l].tileInfo.tile;
							section.tiles.push(tile);
							usedChoice.add(tile.name);
						}
					}
				}
			}
			
			sections.push(section);
		}

		this.sections = sections;
		_id(this.stateId).value = JSON.stringify({sections:this.sections});

		for(var i = 0; i < this.choice.length; i++) {
			const tileInfo = this.choice[i];
			const _tile = _id(tileInfo.tleId);
			if (usedChoice.has(tileInfo.tleNm)) {
				_tile.style.display = "none";
			} else {
				_tile.style.display = "block";
			}
		}
	};
	
	const sevp = ux.newEvPrm(rgp);
	sevp.uCmd = id + "->editSection";
	sevp.uRef = [ id, rgp.pEditSecId, rgp.pEditModeId ];
	editor.secevp = sevp;

	const tevp = ux.newEvPrm(rgp);
	tevp.uCmd = id + "->editTile";
	tevp.uRef = [ id, rgp.pEditSecId, rgp.pEditModeId, rgp.pEditTileId, rgp.pEditTileIndexId ];
	editor.tileevp = tevp;

	const mevp = ux.newEvPrm(rgp);
	mevp.uCmd = id + "->editTile";
	mevp.uRef = [rgp.pEditModeId, rgp.pEditMoveId ];
	editor.moveevp = mevp;
	
	const choiceId = rgp.pChoiceId;
	for(var i = 0; i < content.charts.length; i++) {
		const tileInfo = content.charts[i];
		tileInfo.tleId = choiceId + i;

		if (editor.editable) {
			const evp = {};
			evp.editor = editor;
			evp.tileInfo = tileInfo;
			ux.addHdl(_id(tileInfo.tleId), "mousedown", fuxstudio.dshChoiceDragStart, evp);
		}
	}
	
	if (editor.secCount > 0) {
		const design = _id(editor.designId);
		for (var i = 0; i < editor.secCount; i++) {			
			const evp = fuxstudio.dshCreateSectionPrm(editor, i);
			evp.secInfo.section = content.design.sections[i];
			const secdiv = fuxstudio.dshCreateSection(evp);
			design.appendChild(secdiv);
			fuxstudio.dshWireSection(evp);
		}
	}

	editor.changeState();
}

fuxstudio.dshCreateSectionPrm = function(editor, i) {
	const secInfo = {};
	secInfo.index = i;
	secInfo.secId = editor.secId + i;
	secInfo.dragId = "drag_" + secInfo.secId;
	secInfo.addId = "add_" + secInfo.secId;
	secInfo.editId = "edit_" + secInfo.secId;
	secInfo.delId = "del_" + secInfo.secId;
	secInfo.tileCounter = -1;
	secInfo.tileInfo = [];

	const evp = {};
	evp.editor = editor;
	evp.secInfo = secInfo;
	editor.secInfo.push(secInfo);
	return evp;
}

fuxstudio.dshCreateSection = function(evp) {
	const editor = evp.editor;
	const secInfo = evp.secInfo;
	const secdiv = document.createElement("div");
	const html = fuxstudio.dshSectionInnerHtml(editor, secInfo);
	secdiv.className = "sec";
	secdiv.id = secInfo.secId;
	secdiv.innerHTML = html;
	secdiv.stick = secInfo.stick;
	return secdiv;
}

fuxstudio.dshSectionInnerHtml = function(editor, secInfo) {
	const caption = secInfo.section.label ? secInfo.section.label:"&nbsp;";
	var html = null;
	secInfo.movable = secInfo.index != 0;
	html = "<div class=\"sechdr\">"
		+ "<div class=\"elcap grab\" id=\"" + secInfo.dragId + "\"><span>" + editor.sectiontitle + " [" + caption + "]</span></div>"
		+ "<div class=\"elact\">";
	if (editor.editable) {
		html += fuxstudio.editorTextButton(secInfo.addId, editor.addsec);
	}
	html += fuxstudio.editorSymButton(secInfo.editId, editor.cog, "actbtn");
	if (secInfo.movable && editor.editable) {
		html += fuxstudio.editorSymButton(secInfo.delId,  editor.cross, "actbtn");
	}
    html += "</div></div>";
	    
	secInfo.tileCounter = -1;
	secInfo.tileInfo = [];
	secInfo.designId = [];
	const section = secInfo.section;
	const designRowId = "tledesign_" + secInfo.secId;
	html += "<div style=\"display:table;table-layout:fixed;width:100%;\"><div id=\""
			+ designRowId + "\" style=\"display:table-row;\">";
	const colinfo = dashColumnsType.get(section.columns);
	for (var i = 0; i < colinfo.columns; i++) {
		const designId = designRowId + i;
		html += "<div style=\"display:table-cell;vertical-align:top;\"><div class=\"tledesign\" id=\"" + designId + "\">";
		var filled = false;
		for (var j = 0; j < section.tiles.length; j++) {
			const tile = section.tiles[j];
			if (tile.index == i) {
				const tileInfo = fuxstudio.dshCreateTileInfo(secInfo, j);
				html += fuxstudio.dshTileHtml(editor, tileInfo);
				filled = true;
				break;
			}
		}
		
		if(!filled) {
			html += "<div class=\"placetile\">";
			html += fuxstudio.editorPlacement(editor.plus, editor.additem);
			html += "</div>";
		}
		
		html += "</div></div>";
		secInfo.designId.push(designId);
	}

	html += "</div></div>";
	return html;
}

fuxstudio.dshWireSection = function(evp) {
	const secInfo = evp.secInfo;
	const editor = evp.editor;
	_id(secInfo.secId).secInfo = secInfo;
	if (editor.editable) {
		ux.addHdl(_id(secInfo.dragId), "mousedown", fuxstudio.dshSectionDragStart, evp);
	}

	ux.addHdl(_id(secInfo.addId), "click", fuxstudio.dshSectionAdd, evp);
	ux.addHdl(_id(secInfo.editId), "click", fuxstudio.dshSectionEdit, evp);
	if (editor.editable) {
		ux.addHdl(_id(secInfo.delId), "click", fuxstudio.dshSectionDel, evp);
	}	

	for (var i = 0; i < secInfo.tileInfo.length; i++) {
		const tevp = {};
		tevp.editor = editor;
		tevp.secInfo = secInfo;
		tevp.tileInfo = secInfo.tileInfo[i];
		fuxstudio.dshWireTile(tevp);
	}
}

fuxstudio.dshSectionAdd = function(uEv) {
	fuxstudio.dshSectionCrud(uEv, "create");
}

fuxstudio.dshSectionEdit = function(uEv) {
	fuxstudio.dshSectionCrud(uEv, "update");
}

fuxstudio.dshSectionDel = function(uEv) {
	fuxstudio.dshSectionCrud(uEv, "delete");
}

fuxstudio.dshSectionCrud = function(uEv, mode) {
	const evp = uEv.evp;
	const editor = evp.editor;
	_id(editor.ctrl.uEditModeId).value = mode;
	_id(editor.ctrl.uEditSecId).value = evp.secInfo.section.index;
	ux.post({evp: editor.secevp});
}

fuxstudio.dshCreateTileInfo = function(secInfo, index) {
	const tileInfo = {};
	tileInfo.index = index;
	tileInfo.tleId = "tle_" + secInfo.secId + (++secInfo.tileCounter);
	tileInfo.dragId = "drag_" + tileInfo.tleId;
	tileInfo.editId = "edit_" + tileInfo.tleId;
	tileInfo.delId = "del_" + tileInfo.tleId;
	tileInfo.tile = secInfo.section.tiles[index];
	secInfo.tileInfo.push(tileInfo);
	return tileInfo;
}

fuxstudio.dshTileHtml = function(editor, tileInfo) {
	return "<div class=\"tlebdy\" id=\"" + tileInfo.tleId + "\">"
			+ fuxstudio.dshTileInnerHtml(editor, tileInfo)
		    + "</div>";
}

fuxstudio.dshTileInnerHtml = function(editor, tileInfo) {
	const tile = tileInfo.tile;
	const caption = tile.description;
	var html = "<div class=\"tlehdr\">"
			+ "<div class=\"tlecap grab\" id=\"" + tileInfo.dragId + "\"><span>" + caption + "</span></div>"
			+ "<div class=\"tleact\">"
			+ fuxstudio.editorSymButton(tileInfo.editId, editor.cog, "tlebtn");
	if (editor.editable) {
		html += fuxstudio.editorSymButton(tileInfo.delId,  editor.cross, "tlebtn");
	}
	html += "</div></div>";
	return html;
}

fuxstudio.dshWireTile = function(evp) {
	const tileInfo = evp.tileInfo;
	const editor = evp.editor;
	_id(tileInfo.tleId).tileInfo = tileInfo;
	if (editor.editable) {
		ux.addHdl(_id(tileInfo.dragId), "mousedown", fuxstudio.dshTileDragStart, evp);
	}
	
	ux.addHdl(_id(tileInfo.editId), "click", fuxstudio.dshTileEdit, evp);
	if (editor.editable) {
		ux.addHdl(_id(tileInfo.delId), "click", fuxstudio.dshTileDel, evp);
	}
}

fuxstudio.dshTileAdd = function(evp, tileIndex) {
	const editor = evp.editor;
	_id(editor.ctrl.uEditTileIndexId).value = tileIndex;
	fuxstudio.dshTileCrud(evp, "create");
}

fuxstudio.dshTileEdit = function(uEv) {
	const evp = uEv.evp;
	_id(evp.editor.ctrl.uEditTileIndexId).value = evp.tileInfo.tile.index;
	fuxstudio.dshTileCrud(uEv.evp, "update");
}

fuxstudio.dshTileDel = function(uEv) {
	const evp = uEv.evp;
	_id(evp.editor.ctrl.uEditTileIndexId).value = evp.tileInfo.tile.index;
	fuxstudio.dshTileCrud(uEv.evp, "delete");
}

fuxstudio.dshTileCrud = function(evp, mode) {
	const editor = evp.editor;
	_id(editor.ctrl.uEditModeId).value = mode;
	_id(editor.ctrl.uEditSecId).value = evp.secInfo.index;
	_id(editor.ctrl.uEditTileId).value = evp.tileInfo.tile.name;
	ux.post({evp: editor.tileevp});
}

fuxstudio.dshTileMove = function(evp, srcSecIndex, srcTileIndex, destSecIndex, destTileIndex) {
	const editor = evp.editor;
	_id(editor.ctrl.uEditModeId).value = "move";
	_id(editor.ctrl.uEditMoveId).value = srcSecIndex + "," + srcTileIndex + "," + destSecIndex + "," + destTileIndex;
	ux.post({ evp: editor.moveevp });
}

fuxstudio.dshChoiceDragStart = function(uEv) {
	const evp = uEv.evp;
	const editor = evp.editor;
	const secInfo = fuxstudio.dshFindFirstSection(editor);
	if (secInfo) {
		evp.newSecInfo = {secInfo:secInfo, column:0};
		fuxstudio.editorFixedChoiceDragStart(
				evp, uEv.uTrg, secInfo.designId[0], uEv.clientX,
				uEv.clientY, "placetile", fuxstudio.dshChoiceDrag,
				fuxstudio.dshChoiceDragEnd);
	}
}

fuxstudio.dshChoiceDrag = function(uEv) {
	const _drag = fuxstudio.editorChoiceDrag(uEv);
	const evp = fuxstudio.dragEvp;
	const editor = evp.editor;
	const _secInfo = fuxstudio.editorTablessSectionInfoAt(editor.secInfo, uEv.clientX, uEv.clientY);
	if (_secInfo && _secInfo.empty) {
		evp.newSecInfo = _secInfo;
	}
}

fuxstudio.dshChoiceDragEnd = function(uEv) {
	const evp = fuxstudio.dragEvp;
	const _editor = evp.editor;
	const _secInfo = evp.newSecInfo ? evp.newSecInfo.secInfo : null;
	const _tileIndex = evp.newSecInfo ? evp.newSecInfo.column : -1;
	var _secIndex = _secInfo ? _secInfo.index : -1;
	
	const _charts = _id(_editor.choicesId);
	const _chart = _id(evp.dragOriginId);
	_charts.removeChild(_chart);
	
	ux.remDirectHdl(document, "mousemove", fuxstudio.dshChoiceDrag);
	ux.remDirectHdl(document, "mouseup", fuxstudio.dshChoiceDragEnd);
	fuxstudio.dragEvp = null;
	
	if (_secIndex >= 0) {
		const tevp = {};
		tevp.editor = _editor;
		tevp.secInfo = _secInfo;
		tevp.tileInfo = {tile:{name: evp.tileInfo.tleNm}};
		fuxstudio.dshTileAdd(tevp, _tileIndex);
	}
}

fuxstudio.dshFindFirstSection = function(editor) {
	const _design = _id(editor.designId);
	for(var i = 0; i < _design.children.length; i++) {
		const secdiv = _design.children[i];
		if (secdiv.secInfo) {
			return secdiv.secInfo;
		}
	}
}

fuxstudio.dshTabDragStart = function(uEv) {
	const evp = uEv.evp;
	fuxstudio.editorDragStart(evp, "tab", evp.editor.designId, evp.tabInfo.tabId,
			uEv.clientX, uEv.clientY, "placetab", fuxstudio.dshTabDrag, fuxstudio.dshTabDragEnd);
}

fuxstudio.dshTabDrag = function(uEv) {
	const _drag = fuxstudio.editorChoiceDrag(uEv);
	const evp = fuxstudio.dragEvp;
	const editor = evp.editor;

	const _design = _id(editor.designId);
	const _drop = fuxstudio.editorDesignSlotAt(_design, _drag.id, uEv.clientX, uEv.clientY);
	if (_drop && !_drop.slot.stick && _drop.slot.id != editor.placeId) {
		const _placeslot = _id(editor.placeId);
		const dbox = _drop.slot.getBoundingClientRect();
		const pbox = _placeslot.getBoundingClientRect();
		if (pbox.top > dbox.top) {
			if (uEv.clientY < (dbox.top + dbox.height/2)) {
				_design.insertBefore(_placeslot, _drop.slot);
			}
		} else {
			if (uEv.clientY > (dbox.top + dbox.height/2)) {
				if (_drop.slot.nextSibling) {
					_design.insertBefore(_placeslot, _drop.slot.nextSibling);
				} else {
					_design.appendChild(_placeslot);
				}
			}
		}			
	}
}

fuxstudio.dshTabDragEnd = function(uEv) {
	const evp = fuxstudio.dragEvp;
	fuxstudio.editorDragEnd(evp, "tab", evp.editor.designId,
			fuxstudio.dshTabDrag, fuxstudio.dshTabDragEnd);
}

fuxstudio.dshSecDragStart = function(uEv) {
	const evp = uEv.evp;
	evp.newTabInfo = evp.tabInfo;
	fuxstudio.editorDragStart(evp, "section", evp.tabInfo.designId, evp.sectionInfo.secId,
			uEv.clientX, uEv.clientY, "placetab", fuxstudio.dshSecDrag, fuxstudio.dshSecDragEnd);
}

fuxstudio.dshSecDrag = function(uEv) {
	const _drag = fuxstudio.editorChoiceDrag(uEv);
	const evp = fuxstudio.dragEvp;
	const editor = evp.editor;

	const _tabInfo = fuxstudio.editorTabInfoAt(editor.secInfo, uEv.clientX, uEv.clientY);
	if (_tabInfo && fuxstudio.editorEvaluateDrag(editor, _tabInfo.designId, _drag.id, uEv.clientX,
			uEv.clientY)) {
		evp.newTabInfo = _tabInfo;
	}
}

fuxstudio.dshSecDragEnd = function(uEv) {
	const evp = fuxstudio.dragEvp;
	if (evp.newTabInfo != evp.tabInfo) {
		fuxstudio.editorArrayRemove(evp.tabInfo.sectionInfo, evp.sectionInfo);
		evp.newTabInfo.sectionInfo.push(evp.sectionInfo);
		evp.tabInfo = evp.newTabInfo;
	}

	fuxstudio.editorDragEnd(evp, "section", evp.newTabInfo.designId,
			fuxstudio.dshSecDrag, fuxstudio.dshSecDragEnd);
}

fuxstudio.dshTileDragStart = function(uEv) {
	const evp = uEv.evp;
	const tileInfo = evp.tileInfo;
	evp.newSectionInfo = evp.secInfo;
	fuxstudio.editorDragStart(
			evp, "tile", evp.secInfo.designId[tileInfo.tile.index],
			evp.tileInfo.tleId, uEv.clientX, uEv.clientY, "placetile",
			fuxstudio.dshTileDrag, fuxstudio.dshTileDragEnd);
}

fuxstudio.dshTileDrag = function(uEv) {
	const _drag = fuxstudio.editorChoiceDrag(uEv);
	const evp = fuxstudio.dragEvp;
	const editor = evp.editor;

	const _secInfo = fuxstudio.editorTablessSectionInfoAt(editor.secInfo, uEv.clientX, uEv.clientY);
	if (_secInfo && _secInfo.empty) {
		evp.newSectionInfo =_secInfo;
	}
}

fuxstudio.dshTileDragEnd = function(uEv) {
	const evp = fuxstudio.dragEvp;
	const srcSecIndex = evp.secInfo.section.index;
	const srcTileIndex = evp.tileInfo.tile.index;
	if (evp.newSectionInfo.secInfo) {
		fuxstudio.editorDragEndDel(evp, "tile", evp.secInfo.designId[srcTileIndex],
			fuxstudio.dshTileDrag, fuxstudio.dshTileDragEnd);

		const destSecIndex = evp.newSectionInfo.secInfo.section.index;
		const destTileIndex = evp.newSectionInfo.column;
		fuxstudio.dshTileMove(evp, srcSecIndex, srcTileIndex, destSecIndex, destTileIndex);
	} else {
		fuxstudio.editorDragEnd(evp, "tile", evp.secInfo.designId[srcTileIndex],
			fuxstudio.dshTileDrag, fuxstudio.dshTileDragEnd);		
	}
}


/********************************************************************************/
/************************* WORKFLOW EDITOR **************************************/
/********************************************************************************/
/** Fabric extension */
const FAB_EXTENSION_HALF_PI = Math.PI/2;
const FAB_EXTENSION_NEG_HALF_PI = -Math.PI/2;
fabric.LineArrow = fabric.util.createClass(fabric.Line, {
	  type: 'lineArrow',

	  initialize: function(element, options) {
	    options || (options = {});
	    this.callSuper('initialize', element, options);
	  },

	  toObject: function() {
	    return fabric.util.object.extend(this.callSuper('toObject'));
	  },

	  _render: function(ctx) {
	    this.callSuper('_render', ctx);
	    if (this.visible) {
			ctx.save();
		
			const dx = this.x2 - this.x1;
			const dy = this.y2 - this.y1;
			ctx.translate(dx / 2, dy / 2);
			if (dy == 0) {
				ctx.rotate((dx >= 0 ? 0:Math.PI));
			} else if (dx == 0) {
				ctx.rotate((dy >= 0 ? FAB_EXTENSION_HALF_PI:FAB_EXTENSION_NEG_HALF_PI));
			} else {
				ctx.rotate(Math.atan2(dy, dx));
			}
			ctx.beginPath();
			ctx.moveTo(4, 0);
			ctx.lineTo(-8, 4);
			ctx.lineTo(-8, -4);
			ctx.closePath();
			ctx.fillStyle = this.stroke;
			ctx.fill();
			ctx.restore();
		}
	  }
	});

fabric.LineArrow.fromObject = function(obj, callback) {
  callback && callback(new fabric.LineArrow([obj.x1, obj.y1, obj.x2, obj.y2], obj));
};

fabric.LineArrow.async = true;

fabric.Text.prototype.set({
	  _getNonTransformedDimensions() {
	    return new fabric.Point(this.width, this.height).scalarAdd(this.padding);
	  },
	  _calculateCurrentDimensions() {
	    return fabric.util.transformPoint(this._getTransformedDimensions(), this.getViewportTransform(), true);
	  }
	});

/** Workflow designer */
const WD_CONNECT_START_GAP = 8;
const WD_CONNECT_END_GAP = 8;
const WD_PATH_ALLOWANCE = 5;
const WD_ARROW_GAP = 8;
const WD_ARROW_GAP_HALF = Math.floor(WD_ARROW_GAP/2);
const WD_ARROW_OFFSET = -2;
const WD_NORMAL_COLOR = "#444";
const WD_SELECT_COLOR = "#3498db";
const WD_ROUTE_FILL_COLOR = "#eee";
const WD_ROUTE_CONN_COLOR1 = "#dd0";
const WD_ROUTE_CONN_COLOR2 = "#ff0";
const WD_STEP_BORDER_COLOR = "#ccc";
const WD_STEP_EDIT_COLOR = "#34495e"; //"#6699cc"
const WD_STEP_WIDTH = 164;
const WD_STEP_FONT =  "Arial";
const WD_STEP_ICON_COLOR =  "#888";
const WD_STEP_ICON_FONT = "FontSymbolMngr0";
const WD_CANVAS_WIDTH = 1920;
const WD_CANVAS_HEIGHT= 1080;

const WD_STEP_COLORS = [
	  "#943126",
	  "#633974",
	  "#1a5276",
	  "#21618c",
	  "#117864",
	  "#9a7d0a",
	  "#935116",  
	  "#797d7f",
	  "#212f3c",
	  "#000000"
];

fuxstudio.wfdesign = {
	editor: null,
	edittypes: null,
	steptypes: null,
	action: null,
	grip: null,
	header: null,
	connectStepInfo:null,
	movingStepInfo:null,
	select: null,
	canvas: null,
	manualConnect: null,
	editevp: null,
	sInfo: null,
	
	init: function(editor) {
		this.editor = editor;
		this.steptypes = editor.stepTypes;
		this.edittypes = editor.editTypes;
		this.editevp = editor.editevp;
		const _self = this;
		if (this.canvas) {
			this.canvas.clear();
			this.canvas.dispose();
		}
		this.canvas = new fabric.Canvas(editor.canvasId, {selection:false});
		this.canvas.setWidth(WD_CANVAS_WIDTH);
		this.canvas.setHeight(WD_CANVAS_HEIGHT);
		this.canvas.on({
			'mouse:move': _self._cMouseMove,
			'mouse:up': _self._cMouseUp,
			'selection:created': _self._cObjectSelected,
			'selection:updated': _self._cObjectSelectUpdated,
			'selection:cleared': _self._cObjectSelectCleared
		});
		
		this.manualConnect = new fabric.LineArrow([0,0,0,0], {
			stroke: '#000',
			strokeWidth: 1,
			hasBorders: false
		});
		
		this.sInfo = [];
	},
	
	refresh: function() {
		this.canvas.renderAll();
	},
	
	resize:	function() {
		const _editor = _id(this.editor.panelId);
		this.canvas.setWidth(WD_CANVAS_WIDTH);
		this.canvas.setHeight(WD_CANVAS_HEIGHT);
		this.refresh();
	},
	
	wire:	function() {
			this._connectAll();
			this.refresh();
	},
	
	addStep: function(step) {
				const typeInfo = this.steptypes[step.typeIndex];
				const totalheadheight = typeInfo.edits.length > 0 ? 40 + 1 * 16: 40;
				const info = {stepId:step.name, acceptInflow:typeInfo.acceptInflow, origin:step};
				const layout = [];
				const _self = this;
				const cat_color = WD_STEP_COLORS[typeInfo.color];
				layout.push(new fabric.Rect({
				  top: 0,
				  left: 0,
				  width: WD_STEP_WIDTH,
				  height: totalheadheight,
				  fill: "#fff",
				  stroke: WD_STEP_BORDER_COLOR,
				  strokeWidth: 1
				}));
				info.header = layout[layout.length - 1];
				info.header._stepId = step.name;
				

				layout.push(new fabric.Text(JSON.parse("\"" + typeInfo.icon + "\""), {
				  fontSize: 12,
				  fontWeight: "bold",
				  fontFamily: WD_STEP_ICON_FONT,
				  top: 4,
				  left: 4,
				  width: 16,
				  fill: cat_color,
				  hoverCursor: "pointer"
				}));
				const icon = layout[layout.length - 1];
				icon._stepId = step.name;
				icon._type = "step";
				icon.on({
					"mousedown": _self._callEditStep,
				});
				
				layout.push(new fabric.Text(typeInfo.typeDesc.toUpperCase(), {
				  fontSize: 10,
				  fontFamily: WD_STEP_FONT,
				  top: 4,
				  left: 24,
				  width: WD_STEP_WIDTH - 24 - 4
				}));

				layout.push(new fabric.Text(step.description, {
				  fontSize: 10,
				  fontFamily: WD_STEP_FONT,
				  top: 24,
				  left: 4,
				  width: WD_STEP_WIDTH - 4 - 4
				}));

				var acleft = 4;
				for(var i = 0; i < typeInfo.edits.length; i++) {
					const etype = this.edittypes[typeInfo.edits[i]];
					layout.push(new fabric.Text(etype.caption.toUpperCase() + " (" + step.census[i] + ")", {
						  fontSize: 7,
						  fontFamily: WD_STEP_FONT,
						  top: 40,
						  left: acleft,
						  hoverCursor: "pointer",
				          fill:"#fff",
			        	  backgroundColor: cat_color,
			        	  padding: 4
						}));
					const editbtn = layout[layout.length - 1];
					editbtn._stepId = step.name;
					editbtn._type = etype.name;
					editbtn.on({
						"mousedown": _self._callEditStep,
					});
					
					acleft += editbtn.getBoundingRect().width - 2;
				}
				
				info.inLeft = [];
				info.inTop =  [];
				info.inBottom = [];
				info.outGrip = [];
				for(var i = 0; i < step.routings.length; i++) {
					const routing = step.routings[i];
					const top_offset = i * 20;
					layout.push(new fabric.Rect({
					  top: totalheadheight + top_offset,
					  left: 0,
					  width: WD_STEP_WIDTH,
					  height: 20,
					  fill: WD_ROUTE_FILL_COLOR,
					  stroke: WD_STEP_BORDER_COLOR,
					  strokeWidth: 1
					}));

					layout.push(new fabric.Text(routing.label, {
					  top: totalheadheight + top_offset + 5,
					  left: (WD_STEP_WIDTH - 4 - 10)/2,
					  fontSize: 10,
					  fontFamily: WD_STEP_FONT,
					  textAlign: "center",
					  originX: "center",
					  width: WD_STEP_WIDTH - 4 - 10
					}));
					
					layout.push(new fabric.Rect({
					  top: totalheadheight + top_offset + 5,
					  left: WD_STEP_WIDTH - 10,
					  width: 10,
					  height: 10,
					  fill: WD_ROUTE_CONN_COLOR1,
					  stroke: WD_STEP_BORDER_COLOR,
					  strokeWidth: 1,
					  hoverCursor: "default"
					}));
					
					const grip = layout[layout.length - 1];
					grip._type = "rgrip";
					grip._stepId = step.name;
					grip._targetStepId = routing.nextStepName;
					grip._origin = routing;
					grip._connected = false;
					grip._index = info.outGrip.length;
					grip.on({
						"mouseover": _self._routGripMouseOver,
						"mouseout": _self._routGripMouseOut,
						"mousedown": _self._routStartConnect
					});
					info.outGrip.push(grip);
				}
				
				info.widget = new fabric.Group(layout, {
				  left: step.x,
				  top: step.y,
				  subTargetCheck:true,
				  hasBorders: true
				});
				info.widget._type = "step";
				info.widget._stepId = step.name;
				info.widget._deletable = typeInfo.deletable;
				info.widget.on({
					'moving': _self._stepMoving,
				});
				
				this.canvas.add(info.widget);
				this.sInfo.push(info);
				this.refresh();
	},
	
	_connectAll: function() {
		this._disconnectAll();
		for (var info of this.sInfo) {
			for (var grip of info.outGrip) {
				this._connect(grip, WD_NORMAL_COLOR);
			}
		}

		this.refresh();
	},
	
	_connect: function(grip, color) {
		if (grip._targetStepId) {
			this._disconnect(grip);
			const conn = {stepId:grip._targetStepId, grip:grip, link:null};
			this._placeConnection(conn);
			this._link(conn, color);
			grip._connected = true;
			grip._origin.nextStepName = grip._targetStepId;
			return conn;
		}
		return null;
	},
	
	_placeConnection: function (conn) {
			const tinfo = this._getStep(conn.stepId);
			const grect = this._gripRect(conn.grip);
			if ((grect.left + grect.width) < (tinfo.widget.left - WD_CONNECT_END_GAP)) {
				conn.index = tinfo.inLeft.length;
				tinfo.inLeft.push(conn);
				conn.grip._tin = "left";
			} else if (grect.top < (tinfo.widget.top - WD_CONNECT_END_GAP)) {
				conn.index = tinfo.inTop.length;
				tinfo.inTop.push(conn);
				conn.grip._tin = "top";
			} else {
				conn.index = tinfo.inBottom.length;
				tinfo.inBottom.push(conn);
				conn.grip._tin = "bottom";
			}
	},
	
	_disconnectAll: function() {
		for (var info of this.sInfo) {
			for (var grip of info.outGrip) {
				this._disconnect(grip);
			}
		}
		this.refresh();
	},
	
	_disconnect: function(grip) {
		if (grip._connected && grip._targetStepId) {
			const tinfo = this._getStep(grip._targetStepId);
			var arr = tinfo.inLeft;
			if (grip._tin == "top") {
				arr = tinfo.inTop;
			} else if (grip._tin == "bottom") {
				arr = tinfo.inBottom;
			}
			
			for (var i = 0; i < arr.length; i++) {
				const conn = arr[i];
				if (conn.grip == grip) {
					this._unlink(conn);
					arr.splice(i, 1);
					for (;i < arr.length; i++) {
						arr[i].index = i;
					}
					break;
				}
			}

			grip._tin = null;
			grip._connected = false;
			grip._origin.nextStepName = null;
		}
	},
	
	_rewire: function(info, color) {
		const oldIn = [info.inTop, info.inLeft, info.inBottom];
		info.inLeft = [];
		info.inTop =  [];
		info.inBottom = [];
		for (var arr of oldIn) {
			for (var conn of arr) {
				this._placeConnection(conn);
				this._link(conn, color);
			}
		}
		
		for (var grip of info.outGrip) {
			if (grip._connected) {
				this._connect(grip, color);
			}
		}
	},
	
	_link: function(conn, color) {
		this._unlink(conn);
		const startgap = WD_CONNECT_START_GAP + WD_CONNECT_START_GAP * conn.grip._index;
		const rect = this._gripRect(conn.grip);
		var x1 = rect.left + rect.width;
		var y1 = rect.top + rect.height/2;
		const start = {x1:x1, y1:y1, x2:x1 + startgap, y2:y1};
		
		const info = this._getStep(conn.stepId);
		var x2 = info.widget.left;
		var y2 = info.widget.top;
		const end = {x2:x2, y2:y2};

		if (conn.grip._tin == "top") {
			end.x2 += WD_ARROW_GAP_HALF + WD_ARROW_GAP * conn.index;
			end.y2 += WD_ARROW_OFFSET;
			end.x1 = end.x2;
			end.y1 = end.y2 - WD_CONNECT_END_GAP;
		} else if (conn.grip._tin == "bottom") {
			end.y2 += info.widget.height - WD_ARROW_OFFSET;
			end.x2 += WD_ARROW_GAP_HALF + WD_ARROW_GAP * conn.index;
			end.x1 = end.x2;
			end.y1 = end.y2 + WD_CONNECT_END_GAP;
		} else {
			end.x2 += WD_ARROW_OFFSET;
			end.y2 += WD_ARROW_GAP_HALF + WD_ARROW_GAP * conn.index;
			end.x1 = end.x2 - WD_CONNECT_END_GAP;
			end.y1 = end.y2;
		}

		const gp = {x:start.x1, y:start.y1};
		this._minPoint(gp, start);
		this._minPoint(gp, end);
		this._relLine(gp, start);
		this._relLine(gp, end);
		
		const points = [];
		points.push({x:start.x1, y:start.y1});
		points.push({x:start.x2, y:start.y2});
		const dx = end.x1 - start.x2;
		const dy = end.y1 - start.y2;
		if (conn.grip._tin == "top" || conn.grip._tin == "bottom") {
			const midy = start.y2 + dy / 2;
			points.push({x:start.x2, y:midy});
			points.push({x:end.x1, y:midy});
			points.push({x:end.x1, y:end.y1});
		} else {
			const midx = start.x2 + dx / 2;
			points.push({x:midx, y:start.y2});
			points.push({x:midx, y:end.y1});
			points.push({x:end.x1, y:end.y1});
		}

		// TODO Implement collision detection
		var pathDef = "M " + points[0].x + "," + points[0].y;
		for (var i = 1; i < points.length; i++) {
			// TODO Implement curved corners
			pathDef += " L" + points[i].x + "," + points[i].y;
		}
		
		const estub = new fabric.LineArrow([end.x1, end.y1, end.x2, end.y2], {
			fill: "",
			stroke: color,
			hasBorders: false,
			perPixelTargetFind:true,
			targetFindTolerance:4
		});
		const path = new fabric.Path(pathDef, {
			fill: "",
			stroke: color,
			hasBorders: false,
			perPixelTargetFind:true,
			targetFindTolerance:4
		});

		conn.link = new fabric.Group([path, estub], {
			left: gp.x,
			top: gp.y,
			lockMovementX: true,
			lockMovementY: true,
			subTargetCheck:false,
			hasBorders: false,
			perPixelTargetFind:true,
			targetFindTolerance:4
		});
		conn.link._type = "link";
		conn.link._grip = conn.grip;

		this.canvas.add(conn.link);
	},
	
	_minPoint: function(p, line) {
		p.x = p.x > line.x1 ? line.x1 : p.x;
		p.y = p.y > line.y1 ? line.y1 : p.y;
		
		p.x = p.x > line.x2 ? line.x2 : p.x;
		p.y = p.y > line.y2 ? line.y2 : p.y;
	},
	
	_relLine: function(p, line) {
		line.x1 -= p.x;
		line.y1 -= p.y;
		
		line.x2 -= p.x;
		line.y2 -= p.y;
	},
	
	_unlink: function(conn) {
		if (conn.link) {
			this.canvas.remove(conn.link);
			conn.link = null;
		}
	},
	
	_getStep: function(stepId) {
		for (var info of this.sInfo) {
			if (info.stepId === stepId) {
				return info;
			}
		}
		return null;
	},
	
	_deleteStep: function(stepId) {
		const info = this._getStep(stepId);
		if (info) {
			const conns = [];
			for (var conn of info.inLeft) {conns.push(conn);}
			for (var conn of info.inTop) {conns.push(conn);}
			for (var conn of info.inBottom)  {conns.push(conn);}
			for (var conn of conns) {this._disconnect(conn.grip);}
			for (var grip of info.outGrip) {this._disconnect(grip);}
			
			this.canvas.remove(info.widget);
			this.editor.steps = this.editor.steps.filter(function(step) {
			    return step.name !== info.stepId;
			});
			fuxstudio.editorArrayRemove(this.sInfo, info);
		}
	},
	
	_getStepWithHeaderAt: function(p) {
		for (var info of this.sInfo) {
			const widget = info.widget;
			if (p.x >= widget.left && p.x < (widget.left + widget.width) 
				&& p.y >= widget.top && p.y < (widget.top + 40)) {
				return info;
			}
		}
		return null;
	},

	_setGroupStroke: function(group, stroke) {
		const len = group.size();
		for(var i = 0; i < len; i++) {
			group.item(i).set({stroke:stroke});
		}
	},
	
	// Event handlers
	_cKeyDown: function(e) {
		const wfdesign = fuxstudio.wfdesign;
		if (wfdesign.select && e.uKeyCode == UNIFY_KEY_DELETE) {
			if (wfdesign.select._type == "link") {
				wfdesign._disconnect(wfdesign.select._grip);
				wfdesign.editor.changeState();
				wfdesign.refresh();
			} else if (wfdesign.select._type == "step" && wfdesign.select._deletable){
				wfdesign._deleteStep(wfdesign.select._stepId);
				wfdesign.editor.changeState();
				wfdesign.refresh();
			}
		}
	},
	
	_cMouseMove: function(e) {
		const wfdesign = fuxstudio.wfdesign;
		if (wfdesign.action == "link") {
			const p = wfdesign.canvas.getPointer(e.target);
			wfdesign.manualConnect.set({"x2":p.x, "y2":p.y});
			const info = wfdesign._getStepWithHeaderAt(p);
			const oldheader = wfdesign.header;
			wfdesign.header = null;
			wfdesign.connectStepInfo = null;
			if (info && info.acceptInflow && info.stepId != wfdesign.grip._stepId) {
				info.header.set({fill:"#d6eaf8", stroke:WD_SELECT_COLOR});
				wfdesign.header = info.header;
				wfdesign.connectStepInfo = info;
			}
			
			if (oldheader && oldheader != wfdesign.header) {
				oldheader.set({fill:"#fff", stroke:"#aaa"});
			}			
			wfdesign.refresh();
		}
	},
	
	_cMouseUp: function(e) {
		const wfdesign = fuxstudio.wfdesign;
		if (wfdesign.action == "link") {
			wfdesign.canvas.remove(wfdesign.manualConnect);
			wfdesign._routGripMouseOut({target:wfdesign.grip});
			if (wfdesign.header) {
				wfdesign.header.set({fill:"#fff", stroke:"#aaa"});
				wfdesign.header = null;
			}

			if (wfdesign.connectStepInfo) {
				wfdesign.grip._targetStepId = wfdesign.connectStepInfo.stepId;
				wfdesign._connect(wfdesign.grip, WD_NORMAL_COLOR);
				wfdesign.connectStepInfo = null;
			}
			
			wfdesign.action = null;
			wfdesign.grip = null;
			wfdesign.editor.changeState();
		} else if (wfdesign.action == "movingStep") {
			const info = wfdesign.movingStepInfo;
			wfdesign._rewire(info, WD_NORMAL_COLOR);
			wfdesign.refresh();
			info.origin.x = info.widget.left;
			info.origin.y = info.widget.top;
			wfdesign.action = null;
			wfdesign.movingStepInfo = null;
			wfdesign.editor.changeState();
		}
	},
	
	_cObjectSelected: function(e) {
		const wfdesign = fuxstudio.wfdesign;
		wfdesign.select = e.target;
		if (wfdesign.select._type == "link") {
			wfdesign._setGroupStroke(wfdesign.select, WD_SELECT_COLOR);
			wfdesign.refresh();
		}
	},
	
	_cObjectSelectUpdated: function(e) {
		const wfdesign = fuxstudio.wfdesign;
		if(wfdesign.select != e.target) {
			if (wfdesign.select._type == "link") {
				wfdesign._setGroupStroke(wfdesign.select, WD_NORMAL_COLOR);
			}
			wfdesign.select = e.target;
			if (wfdesign.select._type == "link") {
				wfdesign._setGroupStroke(wfdesign.select, WD_SELECT_COLOR);
			}
			wfdesign.refresh();
		}
	},
	
	_cObjectSelectCleared: function(e) {
		const wfdesign = fuxstudio.wfdesign;
		if (wfdesign.select && wfdesign.select._type == "link") {
			wfdesign._setGroupStroke(wfdesign.select, WD_NORMAL_COLOR);
			wfdesign.refresh();
		}
		
		wfdesign.select = null;
	},
	
	_callEditStep: function(e) {
		const editbtn = e.subTargets[0];
		const evp = fuxstudio.wfdesign.editevp;
		_id(evp.uEditTypeId).value = editbtn._type;
		_id(evp.uEditStepId).value = editbtn._stepId;
		ux.post({evp: evp});
	},
	
	_routGripMouseOver: function(e) {
		const grip = e.target;
		if (!grip._connected) {
			const widget = fuxstudio.wfdesign._getStep(grip._stepId).widget;
			grip.set("fill", WD_ROUTE_CONN_COLOR2);
			widget.lockMovementX = true;
			widget.lockMovementY = true;
			fuxstudio.wfdesign.refresh();
		}
	},
	
	_stepMoving: function(e) {
		const wfdesign = fuxstudio.wfdesign;
		const widget = wfdesign.canvas.getActiveObject();
		if (widget._stepId) {
			wfdesign.action = "movingStep";
			wfdesign.movingStepInfo = wfdesign._getStep(widget._stepId);
			wfdesign._rewire(wfdesign.movingStepInfo, WD_SELECT_COLOR);
		}
	},
	
	_routGripMouseOut: function(e) {
		const grip = e.target;
		if (!grip._connected) {
			const widget = fuxstudio.wfdesign._getStep(grip._stepId).widget;
			grip.set("fill", WD_ROUTE_CONN_COLOR1);
			widget.lockMovementX = false;
			widget.lockMovementY = false;
			fuxstudio.wfdesign.refresh();
		}
	},
	
	_routStartConnect: function(e) {
		const grip = e.subTargets[0];
		if (!grip._connected) {
			const wfdesign = fuxstudio.wfdesign;
			const rect = wfdesign._gripRect(grip);
			const x = rect.left + rect.width/2;
			const y = rect.top + rect.height/2;
			wfdesign.action = "link";
			wfdesign.grip = grip;
			wfdesign.manualConnect.set({"x1":x, "y1":y, "x2":x, "y2":y});
			wfdesign.canvas.add(wfdesign.manualConnect);
			wfdesign.canvas.setActiveObject(wfdesign.manualConnect);
			wfdesign.refresh();
		}
	},
	
	_gripRect: function(grip) {
		const widget = fuxstudio.wfdesign._getStep(grip._stepId).widget;
		const p = fabric.util.transformPoint({"x":grip.left, "y": grip.top},
			widget.calcTransformMatrix());
		return {left:p.x, top:p.y, width:grip.width, height:grip.height};
	}
};

/** Workflow editor */
fuxstudio.rigWorkflowEditor = function(rgp) {
	const id = rgp.pId;
	const content = rgp.pContent;

	const editor = {};
	editor.choicesId = rgp.pToolBaseId;
	editor.panelId = id;
	editor.designId = rgp.pDsnBaseId;
	editor.canvasId = rgp.pDsnCanvasId;
	editor.stateId = rgp.pStateId;
	editor.steps = content.design.steps;
	editor.editable = content.editable;
	editor.editTypes = content.editTypes;
	editor.stepTypes = content.stepTypes;
	editor.changeState = function() {
		var csteps = [];
		for (var step of this.steps) {
			csteps.push({
				name:step.name,
				x:Math.round(step.x),
				y:Math.round(step.y),
				routings:step.routings
			});
		}
		
		_id(this.stateId).value = JSON.stringify({steps:csteps});
	};
	
	const evp = ux.newEvPrm(rgp);
	evp.uCmd = id + "->edit";
	evp.uEditTypeId = rgp.pEditTypeId;
	evp.uEditStepId = rgp.pEditStepId;
	evp.uRef = [ id, rgp.pEditTypeId, rgp.pEditStepId ];
	editor.editevp = evp;

	const wfdesign = fuxstudio.wfdesign;
	wfdesign.init(editor);
	wfdesign.resize();
	for (var step of editor.steps) {
		wfdesign.addStep(step);
	}
	wfdesign.wire();
	
	if (editor.editable) {
		const choiceId = rgp.pChoiceId;
		for(var i = 0; i < content.tools.length; i++) {
			const evp = ux.newEvPrm(rgp);
			evp.uCmd = id + "->create";
			evp.uNewTypeId = rgp.pNewTypeId;
			evp.uNewXId = rgp.pNewXId;
			evp.uNewYId = rgp.pNewYId;
			evp.uRef = [id, rgp.pNewTypeId, rgp.pNewXId, rgp.pNewYId];
			evp.editor = editor;
			evp.componentId = choiceId + i;
			evp.componentInfo = content.tools[i];
			ux.addHdl(_id(evp.componentId), "mousedown", fuxstudio.wfeToolDragStart, evp);
		}
	}
	
	editor.changeState();
}

fuxstudio.wfeToolDragStart = function(uEv) {
	const evp = uEv.evp;
	fuxstudio.editorChoiceDragClone(evp, uEv.uTrg, uEv.clientX, uEv.clientY);
	ux.addDirectHdl(document, "mousemove", fuxstudio.wfeChoiceDrag);
	ux.addDirectHdl(document, "mouseup", fuxstudio.wfeChoiceDragEnd);
}

fuxstudio.wfeChoiceDrag = function(uEv) {
	fuxstudio.editorChoiceDrag(uEv);
}

fuxstudio.wfeChoiceDragEnd = function(uEv) {
	const evp = fuxstudio.dragEvp;
	const editor = evp.editor;
	const tools = _id(editor.choicesId);
	const _drag = _id(evp.dragOriginId);
	const cbox = _id(editor.canvasId).getBoundingClientRect();
	const dbox = _drag.getBoundingClientRect();
	tools.removeChild(_drag);
	
	ux.remDirectHdl(document, "mousemove", fuxstudio.wfeChoiceDrag);
	ux.remDirectHdl(document, "mouseup", fuxstudio.wfeChoiceDragEnd);
	fuxstudio.dragEvp = null;
	
	const x = dbox.left - cbox.left;
	const y = dbox.top - cbox.top;
	if (cbox.x >= 0 && cbox.y >= 0) {
		_id(evp.uNewTypeId).value = evp.componentInfo.typeNm;
		_id(evp.uNewXId).value = x;
		_id(evp.uNewYId).value = y;
		ux.post({evp: evp});
	}
}

/********************************************************************************/
/************************* COMMON ***********************************************/
/********************************************************************************/

fuxstudio.editorNullifyBlank = function(val) {
	if (val && val.trim().length > 0) {
		return val;
	}	
	return null;
}

fuxstudio.editorArrayRemove = function(arr, obj) {
	const i = arr.indexOf(obj);
	if (i >= 0) {
		arr.splice(i, 1);
	}
}

fuxstudio.editorInsertAfter = function(parent, newNode, target) {
	if (target && target.nextSibling) {
		parent.insertBefore(newNode, target.nextSibling);
	} else {
		parent.appendChild(newNode);
	}
}

fuxstudio.editorEvaluateDrag = function(editor, designId, dragId, x, y) {
	const _design = _id(designId);
	const _placeslot = _id(editor.placeId);
	const pbox = _placeslot.getBoundingClientRect();
	var _drop = fuxstudio.editorDesignSlotAt(_design, dragId, x, y);
	if (_drop) {
		if (_drop.slot.id != editor.placeId) {
			const dbox = _drop.slot.getBoundingClientRect();
			if (pbox.top > dbox.top) {
				if (y < (dbox.top + dbox.height/2)) {
					_design.insertBefore(_placeslot, _drop.slot);						
					return true;
				}
			} else {
				if (y > (dbox.top + dbox.height/2)) {
					fuxstudio.editorInsertAfter(_design, _placeslot, _drop.slot);
					return true;
				}
			}
		}
	} else {
		if (_design.children.length == 0
				|| (_design.children.length == 1 && _design.children[0].id == dragId)) {
			_design.appendChild(_placeslot);
			return true;
		} else {
			var _dropslot = _design.children[0];
			if (_dropslot.id != editor.placeId) {
				const dbox = _dropslot.getBoundingClientRect();
				if (pbox.top < dbox.top) {
					if (y < (dbox.top + dbox.height/2)) {
						_design.insertBefore(_placeslot, _dropslot);						
						return true;
					}
				}
			}
			
			_dropslot = _design.children[_design.children.length - 1];
			if (_dropslot.id != editor.placeId) {
				const dbox = _dropslot.getBoundingClientRect();
				if (y > (dbox.top + dbox.height / 2)) {
					_design.appendChild(_placeslot);
					return true;
				}
			}
		}
	}
	
	return false;
}

fuxstudio.editorTabInfoAt = function(tabInfos, x, y) {
	for (var i = 0; i < tabInfos.length; i++) {
		const _tabInfo = tabInfos[i];
		if (!_tabInfo.frozen && _tabInfo.designId) {
			const _design = _id(_tabInfo.designId);
			if (_design) {
				const box = _design.getBoundingClientRect();
				if (x > box.left && x < box.right && y > box.top && y < box.bottom) {
					return _tabInfo;
				}
			}
		}
	}

	return null;
}

fuxstudio.editorSectionInfoAt = function(tabInfos, x, y) {
	for (var i = 0; i < tabInfos.length; i++) {
		const _tabInfo = tabInfos[i];
		if (!_tabInfo.frozen && _tabInfo.designId) {
			var _design = _id(_tabInfo.designId);
			if (_design) {
				var box = _design.getBoundingClientRect();
				if (x > box.left && x < box.right && y > box.top && y < box.bottom) {
					for (var j = 0; j < _tabInfo.sectionInfo.length; j++) {
						const _sectionInfo = _tabInfo.sectionInfo[j];
						for(var k = 0; k < _sectionInfo.designId.length; k++) {
							const designId = _sectionInfo.designId[k];
							_design = _id(designId);
							if (_design) {
								var box = _design.getBoundingClientRect();
								if (x > box.left && x < box.right && y > box.top && y < box.bottom) {
									return {sectionInfo:_sectionInfo, column:k};
								}
							}
						}
					}
				}
			}
		}
	}

	return null;
}

fuxstudio.editorTablessSectionInfoAt = function(secInfos, x, y) {
	for (var j = 0; j < secInfos.length; j++) {
		const _secInfo = secInfos[j];
		for(var k = 0; k < _secInfo.designId.length; k++) {
			const designId = _secInfo.designId[k];
			_design = _id(designId);
			if (_design) {
				var box = _design.getBoundingClientRect();
				if (x > box.left && x < box.right && y > box.top && y < box.bottom) {
					const section = _secInfo.section;
					var empty = true;
					for (var n = 0; n < section.tiles.length; n++) {
						const tile = section.tiles[n];
						if (tile.index == k) {
							empty = false;
							break;
						}
					}
					
					return {secInfo:_secInfo, column:k, empty:empty};
				}
			}
		}
	}

	return null;
}

fuxstudio.editorDesignSlotAt = function(design, dragId, x, y) {
	for (var i = 0; i < design.children.length; i++) {
		const slot = design.children[i];
		if (slot.id != dragId) {
			const box = slot.getBoundingClientRect();
			if (x > box.left && x < box.right && y > box.top && y < box.bottom) {
				return {slot:slot, index:i};
			}
		}
	}

	return null;
}

fuxstudio.editorChoiceDragStart = function(evp, target, designId, clientX,
		clientY, placeClass, dragFunc, dragEndFunc) {
	fuxstudio.editorChoiceDragClone(evp, target, clientX, clientY);
	const editor = evp.editor;
	const placeslot = document.createElement("div");
	placeslot.className = placeClass;
	placeslot.id = editor.placeId;
	placeslot.innerHTML = fuxstudio.editorPlacement(editor.plus, editor.additem);
	const design = _id(designId);
	if (design.children.length > 0) {
		design.insertBefore(placeslot, design.children[0]);
	} else {
		design.appendChild(placeslot);
	}
	
	ux.addDirectHdl(document, "mousemove", dragFunc);
	ux.addDirectHdl(document, "mouseup", dragEndFunc);
}

fuxstudio.editorFixedChoiceDragStart = function(evp, target, designId, clientX,
		clientY, placeClass, dragFunc, dragEndFunc) {
	fuxstudio.editorChoiceDragClone(evp, target, clientX, clientY);
	ux.addDirectHdl(document, "mousemove", dragFunc);
	ux.addDirectHdl(document, "mouseup", dragEndFunc);
}

fuxstudio.editorChoiceDragClone = function(evp, target, clientX, clientY) {
	const editor = evp.editor;
	const _choices = _id(editor.choicesId);
	const box = target.getBoundingClientRect();

	const _nchoice = target.cloneNode(true);
	_nchoice.id = "drag_" + target.id;
	_choices.insertBefore(_nchoice, target);

	evp.dragType = "choice";
	evp.dragOriginId = _nchoice.id;
	evp.dragOffsetX = clientX - box.x;
	evp.dragOffsetY = clientY - box.y;
	_nchoice.style.left = box.x + "px";
	_nchoice.style.top = box.y + "px";
	_nchoice.style.width = box.width.toFixed() + "px";
	_nchoice.style.height = box.height.toFixed() + "px";    
	_nchoice.classList.add("drag");           
	
	fuxstudio.dragEvp = evp;
}

fuxstudio.editorChoiceDrag = function(uEv) {
	const evp = fuxstudio.dragEvp;
	const _drag = _id(evp.dragOriginId);
	_drag.style.left = (uEv.clientX - evp.dragOffsetX) + "px";
	_drag.style.top = (uEv.clientY - evp.dragOffsetY) + "px";
	return _drag;
}

fuxstudio.editorDragStart = function(evp, type, designId, elemId, clientX,
		clientY, slotClass, dragFunc, dragEndFunc) {
	const editor = evp.editor;
	const _elem = _id(elemId);
	const box = _elem.getBoundingClientRect();
	evp.dragType = type;
	evp.dragOriginId = elemId;
	evp.dragOffsetX = clientX - box.x;
	evp.dragOffsetY = clientY - box.y;
	_elem.style.left = box.x + "px";
	_elem.style.top = box.y + "px";
	_elem.style.width =  box.width.toFixed() + "px";
	_elem.style.height =  box.height.toFixed() + "px";
	_elem.classList.add("drag");           

	const placeslot = document.createElement("div");
	placeslot.className = slotClass;
	placeslot.id = editor.placeId;
	placeslot.innerHTML = fuxstudio.editorPlacement(editor.downarrow, editor.placeitem);
	const design = _id(designId);
	design.insertBefore(placeslot, _elem);
	
	fuxstudio.dragEvp = evp;
	ux.addDirectHdl(document, "mousemove", dragFunc);
	ux.addDirectHdl(document, "mouseup", dragEndFunc);
}

fuxstudio.editorDragEnd = function(evp, type, designId, dragFunc, dragEndFunc) {
	const _editor = evp.editor;
	const _design = _id(designId);
	const _placeslot = _id(_editor.placeId);

	if (evp.dragType == type) {
		const _elem = _id(evp.dragOriginId);
		_design.insertBefore(_elem, _placeslot);
		_design.removeChild(_placeslot);
		_elem.style.left = null;
		_elem.style.top = null;
		_elem.style.width = null;
		_elem.style.height = null;
		_elem.classList.remove("drag");
	}
	
	ux.remDirectHdl(document, "mousemove", dragFunc);
	ux.remDirectHdl(document, "mouseup", dragEndFunc);
	fuxstudio.dragEvp = null;
	
	_editor.changeState();
}

fuxstudio.editorDragEndDel = function(evp, type, designId, dragFunc, dragEndFunc) {
	const _editor = evp.editor;
	const _design = _id(designId);
	const _placeslot = _id(_editor.placeId);

	if (evp.dragType == type) {
		const _elem = _id(evp.dragOriginId);
		_design.removeChild(_elem);
	}
	
	ux.remDirectHdl(document, "mousemove", dragFunc);
	ux.remDirectHdl(document, "mouseup", dragEndFunc);
	fuxstudio.dragEvp = null;
}

fuxstudio.editorPlacement = function(sym, txt) {
	return "<div><span class=\"plus g_fsm\">" + sym + "</span></div>"
		 + "<div><span class=\"label\">" + txt + "</span></div>";
}

fuxstudio.editorTextButton = function(id, text) {
	return "<button type=\"button\" class=\"txtbtn\" id=\"" + id + "\">" + text + "</button>";
};

fuxstudio.editorSymButton = function(id, symbol, styleClass) {
	return "<button type=\"button\" class=\"ui-symbol " + styleClass
			+ " g_fsm\" id=\"" + id + "\">" + symbol + "</button>";
};

/** Initialization */
fuxstudio.init = function() {
	ux.registerExtension("fuxstudio", fuxstudio);
	ux.addDirectHdl(document, "keydown",
			fuxstudio.wfdesign._cKeyDown, {});
	// Perform
	ux.setfn(fuxstudio.rigFormEditor, "fuxstudio01");  
	ux.setfn(fuxstudio.rigStudioMenu, "fuxstudio02");  
	ux.setfn(fuxstudio.rigTableColumnEditorPanel, "fuxstudio03");  
	ux.setfn(fuxstudio.rigTableEditor, "fuxstudio04");  
	ux.setfn(fuxstudio.rigWorkflowEditor, "fuxstudio05"); 
	ux.setfn(fuxstudio.rigReportColumnEditorPanel, "fuxstudio06");  
	ux.setfn(fuxstudio.rigReportEditor, "fuxstudio07");  
	ux.setfn(fuxstudio.rigDashboardEditor, "fuxstudio08");  
}

fuxstudio.init();
