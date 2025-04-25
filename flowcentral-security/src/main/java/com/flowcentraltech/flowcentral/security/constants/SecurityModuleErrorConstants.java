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
package com.flowcentraltech.flowcentral.security.constants;

/**
 * Security module error constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public interface SecurityModuleErrorConstants {

    /** Invalid login ID or password */
    String INVALID_LOGIN_ID_PASSWORD = "SECURITY_0001";

    /** Invalid old password */
    String INVALID_OLD_PASSWORD = "SECURITY_0002";

    /** New password is stale */
    String NEW_PASSWORD_IS_STALE = "SECURITY_0003";

    /** User has no role or role(s) not active at current time */
    String USER_ROLE_NOT_ACTIVE_AT_CURRENTTIME = "SECURITY_0004";

    /** User account is locked */
    String USER_ACCOUNT_IS_LOCKED = "SECURITY_0005";

    /** Login as anonymous is not allowed. */
    String LOGIN_AS_ANONYMOUS_NOT_ALLOWED = "SECURITY_0006";

    /** User account is not active */
    String USER_ACCOUNT_NOT_ACTIVE = "SECURITY_0007";

    /** Invalid one-time password */
    String INVALID_ONETIME_PASSWORD = "SECURITY_0008";

    /** User role {0} has no workspace */
    String USER_ROLE_HAS_NO_WORKSPACE = "SECURITY_0009";

    /** User account is not approved */
    String USER_ACCOUNT_NOT_APPROVED = "SECURITY_000A";

    /** Tenancy is required */
    String TENANCY_IS_REQUIRED = "SECURITY_000B";

    /** Tenant with ID not found */
    String TENANT_WITH_ID_NOT_FOUND = "SECURITY_000C";
}
