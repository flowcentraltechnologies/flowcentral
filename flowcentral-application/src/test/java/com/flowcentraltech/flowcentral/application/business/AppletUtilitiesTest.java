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
package com.flowcentraltech.flowcentral.application.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;

import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.common.AbstractFlowCentralTest;
import com.flowcentraltech.flowcentral.common.constants.FlowCentralSessionAttributeConstants;
import com.tcdng.unify.core.criterion.Restriction;

/**
 * Applet utilities tests.
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class AppletUtilitiesTest extends AbstractFlowCentralTest {

    private AppletUtilities au;

    @Test
    public void testSessionBranchScopeRestrictionNull() throws Exception {
        EntityDef entityDef = au.getEntityDef("manageProduct.product");
        assertNotNull(entityDef);

        au.setSessionAttribute(FlowCentralSessionAttributeConstants.BRANCH_SCOPING, null);
        Restriction restriction = au.getSessionBranchScopeRestriction(entityDef);
        assertNull(restriction);
        
        entityDef = au.getEntityDef("manageProduct.locationProduct");
        assertNotNull(entityDef);
        restriction = au.getSessionBranchScopeRestriction(entityDef);
        assertNull(restriction);
    }

    @Test
    public void testSessionBranchScopeRestrictionEmpty() throws Exception {
        EntityDef entityDef = au.getEntityDef("manageProduct.product");
        assertNotNull(entityDef);

        au.setSessionAttribute(FlowCentralSessionAttributeConstants.BRANCH_SCOPING, Collections.emptyList());
        Restriction restriction = au.getSessionBranchScopeRestriction(entityDef);
        assertNull(restriction);
        
        entityDef = au.getEntityDef("manageProduct.locationProduct");
        assertNotNull(entityDef);
        restriction = au.getSessionBranchScopeRestriction(entityDef);
        assertNull(restriction);
    }

    @Test
    public void testSessionBranchScopeRestrictionSingleBranch() throws Exception {
        EntityDef entityDef = au.getEntityDef("manageProduct.product");
        assertNotNull(entityDef);

        au.setSessionAttribute(FlowCentralSessionAttributeConstants.BRANCH_SCOPING, Arrays.asList(1L));
        Restriction restriction = au.getSessionBranchScopeRestriction(entityDef);
        assertNotNull(restriction);
        String filterTxt = au.translate(restriction, entityDef);
        assertEquals("Branch == 1", filterTxt);    
        
        entityDef = au.getEntityDef("manageProduct.locationProduct");
        assertNotNull(entityDef);
        restriction = au.getSessionBranchScopeRestriction(entityDef);
        assertNotNull(restriction);
        filterTxt = au.translate(restriction, entityDef);
        assertEquals("Branch == 1 AND Location == 1", filterTxt);    
    }

    @Test
    public void testSessionBranchScopeRestrictionMultipleBranch() throws Exception {
        EntityDef entityDef = au.getEntityDef("manageProduct.product");
        assertNotNull(entityDef);

        au.setSessionAttribute(FlowCentralSessionAttributeConstants.BRANCH_SCOPING, Arrays.asList(1L, 5L, 22L));
        Restriction restriction = au.getSessionBranchScopeRestriction(entityDef);
        assertNotNull(restriction);
        String filterTxt = au.translate(restriction, entityDef);
        assertEquals("Branch in (1, 5, 22)", filterTxt);    
        
        entityDef = au.getEntityDef("manageProduct.locationProduct");
        assertNotNull(entityDef);
        restriction = au.getSessionBranchScopeRestriction(entityDef);
        assertNotNull(restriction);
        filterTxt = au.translate(restriction, entityDef);
        assertEquals("Branch in (1, 5, 22) AND Location in (1, 5, 22)", filterTxt);    
    }

    @Override
    protected void onSetup() throws Exception {
        au = (AppletUtilities) getComponent(ApplicationModuleNameConstants.APPLET_UTILITIES);
    }

    @Override
    protected void onTearDown() throws Exception {

    }

}
