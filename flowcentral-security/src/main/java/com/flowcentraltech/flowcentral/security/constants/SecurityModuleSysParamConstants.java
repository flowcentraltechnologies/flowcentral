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
package com.flowcentraltech.flowcentral.security.constants;

/**
 * Security module system parameter constants.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public interface SecurityModuleSysParamConstants {

    String ENABLE_PASSWORD_HISTORY = "SEC-0001";

    String PASSWORD_HISTORY_LENGTH = "SEC-0002";

    String ENABLE_PASSWORD_EXPIRY = "SEC-0003";

    String PASSWORD_EXPIRY_DAYS = "SEC-0004";

    String ENABLE_ACCOUNT_LOCKING = "SEC-0005";

    String MAXIMUM_LOGIN_TRIES = "SEC-0006";

    String USER_PASSWORD_GENERATOR = "SEC-0007";

    String USER_PASSWORD_LENGTH = "SEC-0008";

    String USER_PASSWORD_SEND_EMAIL = "SEC-0009";

    String USER_DEFAULT_APPLICATION = "SEC-000A";

    String ENABLE_TWOFACTOR_AUTHENTICATION = "SEC-000B";

    String ENABLE_SYSTEMWIDE_MULTILOGIN_RULE = "SEC-000C";

    String SYSTEMWIDE_MULTILOGIN = "SEC-000D";

    String APPLICATION_SECURITY_KEY = "SEC-000E";

    String ADMINISTRATOR_EMAIL = "SEC-000F";

    String ADMINISTRATOR_NAME = "SEC-0010";

    String USE_LOGIN_LOCALE = "SEC-0011";

    String KEEP_USER_LOGIN_EVENTS = "SEC-0012";

    String ENABLE_THIRDPARTY_PASSWORD_AUTHENTICATION = "SEC-0013";

    String ENABLE_PASSWORD_COMPLEXITY= "SEC-0014";

}
