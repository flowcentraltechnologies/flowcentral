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
package com.flowcentraltech.flowcentral.configuration.constants;

import java.util.Arrays;
import java.util.List;

import com.tcdng.unify.common.annotation.StaticList;
import com.tcdng.unify.common.annotation.Table;
import com.tcdng.unify.common.constants.EnumConst;
import com.tcdng.unify.core.util.EnumUtils;

/**
 * Workflow step type constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Table("FC_WFSTEPTYPE")
@StaticList(name = "wfsteptypelist", description = "$m{staticlist.wfsteptypelist}")
public enum WorkflowStepType implements EnumConst {

    START(
            ProcessingStatus.NORMAL,
            "STR",
            "play",
            WorkflowStepColor.GREEN,
            true,
            false,
            false,
            0),
    ENRICHMENT(
            ProcessingStatus.NORMAL,
            "ENR",
            "outdent",
            WorkflowStepColor.CYAN,
            true,
            true,
            false,
            1),
    PROCEDURE(
            ProcessingStatus.NORMAL,
            "PRC",
            "stream",
            WorkflowStepColor.CYAN,
            true,
            true,
            false,
            2),
    RECORD_ACTION(
            ProcessingStatus.NORMAL,
            "REC",
            "database",
            WorkflowStepColor.NAVY,
            true,
            true,
            false,
            3),
    BINARY_ROUTING(
            ProcessingStatus.NORMAL,
            "BRT",
            "directions",
            WorkflowStepColor.BLUE,
            false,
            true,
            false,
            4),
    POLICY_ROUTING(
            ProcessingStatus.NORMAL,
            "PRT",
            "directions",
            WorkflowStepColor.BLUE,
            false,
            true,
            false,
            5),
    MULTI_ROUTING(
            ProcessingStatus.NORMAL,
            "MRT",
            "directions",
            WorkflowStepColor.BLUE,
            false,
            true,
            false,
            6),
    USER_ACTION(
            ProcessingStatus.NORMAL,
            "USR",
            "user-check",
            WorkflowStepColor.ORANGE,
            true,
            true,
            true,
            7),
    SET_VALUES(
            ProcessingStatus.NORMAL,
            "SET",
            "equals",
            WorkflowStepColor.PURPLE,
            true,
            true,
            false,
            8),
    NOTIFICATION(
            ProcessingStatus.NORMAL,
            "NTF",
            "mail",
            WorkflowStepColor.GREEN,
            true,
            true,
            true,
            9),
    DELAY(
            ProcessingStatus.NORMAL,
            "DLY",
            "circle-pause",
            WorkflowStepColor.CYAN,
            true,
            true,
            false,
            10),
    ERROR(
            ProcessingStatus.ERROR,
            "ERR",
            "exclamation-circle",
            WorkflowStepColor.RED,
            true,
            true,
            true,
            11),
    END(
            ProcessingStatus.NORMAL,
            "END",
            "flag",
            WorkflowStepColor.BLACK,
            true,
            false,
            false,
            12);

    private final ProcessingStatus processingStatus;

    private final String code;

    private final String icon;

    private final WorkflowStepColor color;

    private final boolean supportSetValues;

    private final boolean sendPassThroughAlert;

    private final boolean sendUserActionAlert;

    private final int index;

    private static final List<WorkflowStepType> list = Arrays.asList(values());

    private WorkflowStepType(ProcessingStatus processingStatus, String code, String icon, WorkflowStepColor color,
            boolean supportSetValues, boolean sendPassThrough, boolean sendUserActionAlert, int index) {
        this.processingStatus = processingStatus;
        this.code = code;
        this.icon = icon;
        this.color = color;
        this.supportSetValues = supportSetValues;
        this.sendPassThroughAlert = sendPassThrough;
        this.sendUserActionAlert = sendUserActionAlert;
        this.index = index;
    }

    @Override
    public String code() {
        return code;
    }

    public String icon() {
        return icon;
    }

    public ProcessingStatus processingStatus() {
        return processingStatus;
    }

    public WorkflowStepColor color() {
        return color;
    }

    public boolean supportSetValues() {
        return supportSetValues;
    }

    public boolean sendPassThroughAlert() {
        return sendPassThroughAlert;
    }

    public boolean sendUserActionAlert() {
        return sendUserActionAlert;
    }

    public int index() {
        return index;
    }

    @Override
    public String defaultCode() {
        return START.code;
    }

    public boolean isStart() {
        return START.equals(this);
    }

    public boolean isError() {
        return ERROR.equals(this);
    }

    public boolean isEnd() {
        return END.equals(this);
    }

    public boolean isAcceptInflow() {
        return !START.equals(this) && !ERROR.equals(this);
    }

    public boolean isBinaryRouting() {
        return BINARY_ROUTING.equals(this);
    }

    public boolean isNotification() {
        return NOTIFICATION.equals(this);
    }

    public boolean isPolicy() {
        return ENRICHMENT.equals(this) || PROCEDURE.equals(this) || POLICY_ROUTING.equals(this);
    }

    public boolean isOptional() {
        return !START.equals(this) && !END.equals(this) && !ERROR.equals(this);
    }

    public boolean isAutomatic() {
        return !isSettling();
    }

    public boolean isSetValues() {
        return SET_VALUES.equals(this);
    }

    public boolean isInteractive() {
        return USER_ACTION.equals(this) || ERROR.equals(this) || DELAY.equals(this);
    }

    public boolean isUserAction() {
        return USER_ACTION.equals(this);
    }

    public boolean isRecordAction() {
        return RECORD_ACTION.equals(this);
    }

    public boolean isDelay() {
        return DELAY.equals(this);
    }

    public boolean isSettling() {
        return USER_ACTION.equals(this) || ERROR.equals(this) || DELAY.equals(this);
    }

    public boolean isFlowing() {
        return !isTerminal();
    }

    public boolean isTerminal() {
        return isSettling() || END.equals(this);
    }

    public static List<WorkflowStepType> asList() {
        return list;
    }

    public static WorkflowStepType fromIndex(int index) {
        return list.get(index);
    }

    public static int length() {
        return list.size();
    }

    public static WorkflowStepType fromCode(String code) {
        return EnumUtils.fromCode(WorkflowStepType.class, code);
    }

    public static WorkflowStepType fromName(String name) {
        return EnumUtils.fromName(WorkflowStepType.class, name);
    }
}
