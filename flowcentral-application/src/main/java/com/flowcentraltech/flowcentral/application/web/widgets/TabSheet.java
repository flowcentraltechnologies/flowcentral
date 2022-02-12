/*
 * Copyright (c) 2019, 2021, FlowCentral Technologies.
 * All rights reserved.
 * 
 * PROPRIETARY AND CONFIDENTIAL. USE IS SUBJECT TO LICENSE TERMS.
 */
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.Collections;
import java.util.List;

import com.flowcentraltech.flowcentral.application.data.TabDef;
import com.flowcentraltech.flowcentral.application.data.TabSheetDef;
import com.flowcentraltech.flowcentral.common.data.FormMessage;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.web.ui.constant.MessageType;

/**
 * Tab sheet.
 * 
 * @author Lateef Ojulari
 * @since 1.0
 */
public class TabSheet {

    private TabSheetDef tabSheetDef;

    private List<TabSheetItem> tabSheetItemList;

    private TabSheetEventHandler eventHandler;

    private int currentTabIndex;

    private boolean active;

    public TabSheet(TabSheetDef tabSheetDef, List<TabSheetItem> tabSheetItemList) {
        this.tabSheetDef = tabSheetDef;
        this.tabSheetItemList = Collections.unmodifiableList(tabSheetItemList);
        this.active = true;
        pushTabIndexToActiveItem();
    }

    public TabSheet(TabSheetDef tabSheetDef, List<TabSheetItem> tabSheetItemList, boolean active) {
        this.tabSheetDef = tabSheetDef;
        this.tabSheetItemList = Collections.unmodifiableList(tabSheetItemList);
        this.active = active;
        pushTabIndexToActiveItem();
    }

    public TabSheetDef getTabSheetDef() {
        return tabSheetDef;
    }

    public List<TabDef> getTabDefList() {
        return tabSheetDef.getTabDefList();
    }

    public TabDef getTabDef(int tabIndex) {
        return tabSheetDef.getTabDef(tabIndex);
    }

    public List<TabSheetItem> getTabSheetItemList() {
        return tabSheetItemList;
    }

    public TabSheetItem getTabSheetItem(int tabIndex) {
        return tabSheetItemList.get(tabIndex);
    }

    public int getCurrentTabIndex() {
        return currentTabIndex;
    }

    public void setCurrentTabIndex(int currentTabIndex) {
        this.currentTabIndex = currentTabIndex;
    }

    public TabSheetItem getCurrentItem() {
        if (currentTabIndex >= 0) {
            return tabSheetItemList.get(currentTabIndex);
        }

        return null;
    }

    public MessageType getReviewMessageType(String tabName) throws UnifyException {
        MessageType type = null;
        if (tabName != null && tabSheetDef.isWithFormContext()) {
            List<FormMessage> msgList = tabSheetDef.getFormContext().getReviewMessages(tabName);
            if (msgList != null) {
                for (FormMessage msg : msgList) {
                    if (type == null || type.compareSeverity(msg.getType()) < 0) {
                        type = msg.getType();
                    }
                }
            }
        }

        return type;
    }

    public void setEventHandler(TabSheetEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    public TabSheetEventHandler getEventHandler() {
        return eventHandler;
    }

    public boolean isWithEventHandler() {
        return eventHandler != null;
    }

    public int getTabCount() {
        return tabSheetItemList.size();
    }

    public boolean setTabItemVisible(int tabIndex, boolean visible) {
        if (tabIndex >= 0 && tabIndex < tabSheetItemList.size()) {
            tabSheetItemList.get(tabIndex).setVisible(visible);
            pushTabIndexToActiveItem();
            return true;
        }

        return false;
    }

    public void pushTabIndexToActiveItem() {
        if (currentTabIndex >= 0 && tabSheetItemList.get(currentTabIndex).isVisible()) {
            return;
        }

        for (int i = 0; i < tabSheetItemList.size(); i++) {
            if (tabSheetItemList.get(i).isVisible()) {
                currentTabIndex = i;
                return;
            }
        }

        currentTabIndex = -1;
    }

    public boolean isWithCurrentItem() {
        return currentTabIndex >= 0;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isInStateForDisplay() {
        return active && currentTabIndex >= 0;
    }

    public static class TabSheetItem {

        private final String name;

        private final String appletName;

        private final Object valObject;

        private final int index;

        private boolean visible;

        public TabSheetItem(String name, String appletName, Object valObject, int index, boolean visible) {
            this.name = name;
            this.appletName = appletName;
            this.valObject = valObject;
            this.index = index;
            this.visible = visible;
        }

        public boolean isVisible() {
            return visible;
        }

        public void setVisible(boolean visible) {
            this.visible = visible;
        }

        public String getName() {
            return name;
        }

        public String getAppletName() {
            return appletName;
        }

        public Object getValObject() {
            return valObject;
        }

        public int getIndex() {
            return index;
        }
    }
}
