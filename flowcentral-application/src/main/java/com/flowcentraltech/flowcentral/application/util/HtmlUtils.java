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
package com.flowcentraltech.flowcentral.application.util;

/**
 * HTML utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class HtmlUtils {

    private HtmlUtils() {

    }

    public static String formatReportHTML(final String html) {
        if (html != null) {
            return html.replaceAll("&nbsp;", "&#160;").replaceAll(" ", "&#160;")
                    .replaceAll("\t", "&#160;&#160;&#160;&#160;").replaceAll("\n", "<br/>");
        }

        return null;
    }

    public static String formatEmailHTML(String html) {
        if (html != null) {
            return html/*.replaceAll("\n", "<br/>")*/.replaceAll("\t", "&nbsp;&nbsp;&nbsp;&nbsp;").replaceAll(" ",
                    "&nbsp;");
        }

        return null;
    }

}
