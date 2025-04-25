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

package com.flowcentraltech.flowcentral.system.util;

import java.util.Date;

import com.flowcentraltech.flowcentral.common.constants.LicenseStatus;
import com.flowcentraltech.flowcentral.system.data.LicenseEntryDef;

/**
 * License utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public final class LicenseUtils {

    public static long EVALUATION_PERIOD = 30L * 24 * 60 * 60 * 1000;

    private LicenseUtils() {

    }

    public static LicenseEntryDef getLicenseEntryDefFromLineItems(String[] items, String description, Date now) {
        long issue = Long.parseLong(items[1]);
        long expiry = Long.parseLong(items[2]);
        int capacity = Integer.parseInt(items[3]);
        Integer actCapacity = capacity > 0 ? capacity: null;
        Date issueDate = issue > 0L ? new Date(issue) : null;
        Date expiryDate = expiry > 0L ? new Date(expiry) : null;
        LicenseStatus status = LicenseStatus.LICENSED;
        if (expiryDate == null || expiryDate.before(now)) {
            status = LicenseStatus.EXPIRED;
        }

        return new LicenseEntryDef(items[0], description, issueDate, expiryDate, status, actCapacity);
    }

    public static String getLineFromLicenseEntry(LicenseEntryDef licenseEntryDef) {
        return LicenseUtils.getLineFromLineItems(licenseEntryDef.getFeatureCode(), licenseEntryDef.getIssueDate(),
                licenseEntryDef.getExpiryDate(), licenseEntryDef.getCapacity());
    }

    public static String getLineFromLineItems(String code, Date issueDate, Date expiryDate, Integer capacity) {
        long issue = issueDate != null ? issueDate.getTime() : 0L;
        long expiry = expiryDate != null ? expiryDate.getTime() : 0L;
        int cap = capacity != null ? capacity : 0;
        return code + "," + issue + "," + expiry + "," + cap;
    }
}
