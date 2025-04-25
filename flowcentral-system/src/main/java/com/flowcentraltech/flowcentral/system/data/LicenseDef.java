/*
 * Copyright 2021-2025 FlowCentral Technologies Limited.
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

package com.flowcentraltech.flowcentral.system.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.common.constants.FlowCentralEditionConstants;
import com.flowcentraltech.flowcentral.common.constants.LicenseStatus;
import com.tcdng.unify.core.util.DataUtils;

/**
 * License definition.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class LicenseDef {

    private Long id;

    private String type;

    private String clientTitle;

    private String clientAccount;

    private Date requestTime;

    private String deploymentId;

    private String deploymentDate;

    private List<LicenseEntryDef> entryList;

    private Map<String, LicenseEntryDef> entryMap;

    private long versionNo;

    private LicenseDef(Long id, String type, String clientTitle, String clientAccount, Date requestTime,
            String deploymentId, String deploymentDate, List<LicenseEntryDef> entryList, long versionNo) {
        this.id = id;
        this.type = type;
        this.clientTitle = clientTitle;
        this.clientAccount = clientAccount;
        this.requestTime = requestTime;
        this.deploymentId = deploymentId;
        this.deploymentDate = deploymentDate;
        this.entryList = DataUtils.unmodifiableList(entryList);
        this.entryMap = new HashMap<String, LicenseEntryDef>();
        for (LicenseEntryDef licenseEntryDef : entryList) {
            this.entryMap.put(licenseEntryDef.getFeatureCode(), licenseEntryDef);
        }

        this.versionNo = versionNo;
    }

    public Long getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getClientTitle() {
        return clientTitle;
    }

    public String getClientAccount() {
        return clientAccount;
    }

    public Date getRequestTime() {
        return requestTime;
    }

    public String getDeploymentId() {
        return deploymentId;
    }

    public String getDeploymentDate() {
        return deploymentDate;
    }

    public List<LicenseEntryDef> getEntryList() {
        return entryList;
    }

    public boolean isStandardLicense() {
        return "Standard".equalsIgnoreCase(type);
    }

    public boolean isEnterpriseLicense() {
        return FlowCentralEditionConstants.ENTERPRISE.equalsIgnoreCase(type);
    }

    public boolean isLicenseEntry(String featureCode) {
        return entryMap.containsKey(featureCode);
    }

    public boolean isLicensed(String featureCode) {
        LicenseEntryDef licenseEntryDef = entryMap.get(featureCode);
        return licenseEntryDef != null && licenseEntryDef.getStatus().isLicensed();
    }

    public LicenseEntryDef getLicenseEntryDef(String featureCode) {
        LicenseEntryDef licenseEntryDef = entryMap.get(featureCode);
        if (licenseEntryDef == null) {
            throw new RuntimeException("License entry with function code [" + featureCode + "] is unknown .");
        }

        return licenseEntryDef;
    }

    public long getVersionNo() {
        return versionNo;
    }

    @Override
    public String toString() {
        return "LicenseDef [id=" + id + ", type=" + type + ", clientTitle=" + clientTitle + ", clientAccount="
                + clientAccount + ", requestTime=" + requestTime + ", deploymentId=" + deploymentId
                + ", deploymentDate=" + deploymentDate + ", entryList=" + entryList + ", versionNo=" + versionNo + "]";
    }

    public static Builder newBuilder(Long id, String type, long versionNo) {
        return new Builder(id, type, versionNo);
    }

    public static class Builder {

        private Long id;

        private String type;

        private String clientTitle;

        private String clientAccount;

        private Date requestTime;

        private String deploymentId;

        private String deploymentDate;

        private List<LicenseEntryDef> entryList;

        private long versionNo;

        public Builder(Long id, String type, long versionNo) {
            this.id = id;
            this.type = type;
            this.entryList = new ArrayList<LicenseEntryDef>();
            this.versionNo = versionNo;
        }

        public Builder clientTitle(String clientTitle) {
            this.clientTitle = clientTitle;
            return this;
        }

        public Builder clientAccount(String clientAccount) {
            this.clientAccount = clientAccount;
            return this;
        }

        public Builder requestTime(Date requestTime) {
            this.requestTime = requestTime;
           return this;
        }

        public Builder deploymentId(String deploymentId) {
            this.deploymentId = deploymentId;
            return this;
        }

        public Builder deploymentDate(String deploymentDate) {
            this.deploymentDate = deploymentDate;
            return this;
        }

        public Builder addEntry(String featureCode, String featureDesc, Date issueDate, Date expiryDate,
                LicenseStatus status, Integer capacity) {
            entryList.add(new LicenseEntryDef(featureCode, featureDesc, issueDate, expiryDate, status, capacity));
            return this;
        }

        public Builder addEntry(LicenseEntryDef licenseEntryDef) {
            entryList.add(licenseEntryDef);
            return this;
        }

        public LicenseDef build() {
            return new LicenseDef(id, type, clientTitle, clientAccount, requestTime, deploymentId, deploymentDate,
                    DataUtils.unmodifiableList(entryList), versionNo);
        }
    }
}
