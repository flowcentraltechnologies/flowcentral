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

package com.flowcentraltech.flowcentral.application.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.flowcentraltech.flowcentral.test.AbstractFlowCentralTest;

/**
 * Validator component tests.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class ValidatorTest extends AbstractFlowCentralTest {

    @Test
    public void testEmailValidator() throws Exception {
        Validator validator = getValidator("fc-emailvalidator");
        assertFalse(validator.validate(null));
        assertFalse(validator.validate("abc"));
        assertFalse(validator.validate("abc.com"));
        assertTrue(validator.validate("info@tcdng.com"));
        assertTrue(validator.validate("lateef.ojulari@tcdng.com"));
    }

    @Test
    public void testMobileValidator() throws Exception {
        Validator validator = getValidator("fc-mobilevalidator");
        assertFalse(validator.validate(null));
        assertFalse(validator.validate(234));
        assertFalse(validator.validate("abc"));
        assertFalse(validator.validate("124"));
        assertTrue(validator.validate("08020948192"));
        assertTrue(validator.validate("720948192"));
        assertTrue(validator.validate("+2348020948192"));
    }

    @Test
    public void testWebsiteValidator() throws Exception {
        Validator validator = getValidator("fc-websitevalidator");
        assertFalse(validator.validate(null));
        assertFalse(validator.validate(234));
        assertFalse(validator.validate("abc"));
        assertFalse(validator.validate("http:/tcdng.com"));
        assertFalse(validator.validate("https:/tcdng.com"));
        assertTrue(validator.validate("tcdng.com"));
        assertTrue(validator.validate("www.tcdng.com"));
        assertTrue(validator.validate("http://tcdng.com"));
        assertTrue(validator.validate("https://tcdng.com"));
        assertTrue(validator.validate("http://www.tcdng.com"));
        assertTrue(validator.validate("https://www.tcdng.com"));
    }

    @Test
    public void testDomainValidator() throws Exception {
        Validator validator = getValidator("fc-domainvalidator");
        assertFalse(validator.validate(null));
        assertFalse(validator.validate(234));
        assertFalse(validator.validate("abc"));
        assertFalse(validator.validate("http:/tcdng.com"));
        assertFalse(validator.validate("https:/tcdng.com"));
        assertTrue(validator.validate("tcdng.com"));
        assertTrue(validator.validate("contact.tcdng.com"));
        assertFalse(validator.validate(".com"));
        assertFalse(validator.validate("http://tcdng.com"));
        assertFalse(validator.validate("https://tcdng.com"));
        assertFalse(validator.validate("http://www.tcdng.com"));
        assertFalse(validator.validate("https://www.tcdng.com"));
    }

    protected Validator getValidator(String name) throws Exception {
        return (Validator) getComponent(name);
    }

    @Override
    protected void onSetup() throws Exception {

    }

    @Override
    protected void onTearDown() throws Exception {

    }
}
