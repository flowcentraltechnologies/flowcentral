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

package com.flowcentraltech.flowcentral.common.business;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.junit.Test;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralTest;
import com.flowcentraltech.flowcentral.common.constants.SequencePartType;
import com.flowcentraltech.flowcentral.common.data.SequenceDef;
import com.flowcentraltech.flowcentral.common.data.SequencePartDef;

/**
 * Sequence code generator tests
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SequenceCodeGeneratorTest extends AbstractFlowCentralTest {

    private SequenceCodeGenerator scg;

    @Test
    public void testGetSequenceDefinition() throws Exception {
        SequenceDef sequenceDef = scg.getSequenceDefinition("LOT{n:8}{p:S0001}");
        assertNotNull(sequenceDef);
        
        List<SequencePartDef> partList = sequenceDef.getPartList();
        assertNotNull(partList);
        assertEquals(3, partList.size());
        
        SequencePartDef partDef = partList.get(0);
        assertNotNull(partDef);
        assertEquals(SequencePartType.TEXT, partDef.getType());
        assertEquals("LOT", partDef.getText());
        assertEquals(0, partDef.getNumLen());
        
        partDef = partList.get(1);
        assertNotNull(partDef);
        assertEquals(SequencePartType.SEQUENCE_NUMBER, partDef.getType());
        assertNull(partDef.getText());
        assertEquals(8, partDef.getNumLen());
        
        partDef = partList.get(2);
        assertNotNull(partDef);
        assertEquals(SequencePartType.SYSTEM_PARAMETER, partDef.getType());
        assertEquals("S0001", partDef.getText());
        assertEquals("S0001", partDef.getCode());
        
        assertFalse(sequenceDef.isWithDatePart());
        
        sequenceDef = scg.getSequenceDefinition("{v:code-generator}");
        assertNotNull(sequenceDef);
        
        partList = sequenceDef.getPartList();
        assertNotNull(partList);
        assertEquals(1, partList.size());
        
        partDef = partList.get(0);
        assertNotNull(partDef);
        assertEquals(SequencePartType.VALUESTORE_GENERATOR, partDef.getType());
        assertEquals("code-generator", partDef.getText());
        assertEquals("code-generator", partDef.getCode());
        assertEquals(0, partDef.getNumLen());
    }

    @Test
    public void testGetNextSequenceCode() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 145);
        cal.set(Calendar.YEAR, 2002);
        Date date = cal.getTime();
        
        String code = scg.getNextSequenceCode("test", "QT{n:10}", date, null);
        assertEquals("QT0000000001", code);
        code = scg.getNextSequenceCode("test", "QT{n:10}", date, null);
        assertEquals("QT0000000002", code);
        
        code = scg.getNextSequenceCode("test", "{n:10}QT", date, null);
        assertEquals("0000000001QT", code);
        
        code = scg.getNextSequenceCode("test", "PC{n:0}", date, null);
        assertEquals("PC1", code);
        
        code = scg.getNextSequenceCode("test", "{n:0}PC", date, null);
        assertEquals("1PC", code);
        
        code = scg.getNextSequenceCode("test", "PC/{yyyy}/{n:0}", date, null);
        assertEquals("PC/2002/1", code);
        code = scg.getNextSequenceCode("test", "PC/{yyyy}/{n:0}", date, null);
        assertEquals("PC/2002/2", code);
        
        code = scg.getNextSequenceCode("test", "{yyyy}/PC/{n:0}", date, null);
        assertEquals("2002/PC/1", code);
        code = scg.getNextSequenceCode("test", "{yyyy}/PC/{n:0}", date, null);
        assertEquals("2002/PC/2", code);
        
        code = scg.getNextSequenceCode("test", "GT{yyyy}/PC/{n:0}", date, null);
        assertEquals("GT2002/PC/1", code);
        code = scg.getNextSequenceCode("test", "GT{yyyy}/PC/{n:0}", date, null);
        assertEquals("GT2002/PC/2", code);
        
        code = scg.getNextSequenceCode("test", "GT{dd}/{yyyy}/PC/{ddd}/{n:0}", date, null);
        assertEquals("GT25/2002/PC/145/1", code);
        code = scg.getNextSequenceCode("test", "GT{dd}/{yyyy}/PC/{ddd}/{n:0}", date, null);
        assertEquals("GT25/2002/PC/145/2", code);

        code = scg.getNextSequenceCode("test", "GT{dd}/{yy}/PC/{ddd}/{n:0}", date, null);
        assertEquals("GT25/02/PC/145/1", code);
    }

    @Test
    public void testGetNextSequenceCodeByDate() throws Exception {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DAY_OF_YEAR, 145);
        cal.set(Calendar.YEAR, 2002);
        Date date1 = cal.getTime();
        cal.set(Calendar.DAY_OF_YEAR, 207);
        cal.set(Calendar.YEAR, 2014);
        Date date2 = cal.getTime();
        cal.set(Calendar.DAY_OF_YEAR, 208);
        Date date3 = cal.getTime();
        
        String code = scg.getNextSequenceCode("test", "QT{N:10}", date1, null);
        assertEquals("QT0000000001", code);
        code = scg.getNextSequenceCode("test", "QT{N:10}", date1, null);
        assertEquals("QT0000000002", code);
        code = scg.getNextSequenceCode("test", "QT{N:10}", date2, null);
        assertEquals("QT0000000001", code);
        code = scg.getNextSequenceCode("test", "QT{N:10}", date2, null);
        assertEquals("QT0000000002", code);
        
        code = scg.getNextSequenceCode("test", "{N:10}QT", date1, null);
        assertEquals("0000000001QT", code);
        code = scg.getNextSequenceCode("test", "{N:10}QT", date1, null);
        assertEquals("0000000002QT", code);
        code = scg.getNextSequenceCode("test", "{N:10}QT", date2, null);
        assertEquals("0000000001QT", code);
        code = scg.getNextSequenceCode("test", "{N:10}QT", date2, null);
        assertEquals("0000000002QT", code);
        
        code = scg.getNextSequenceCode("test", "PC{N:0}", date1, null);
        assertEquals("PC1", code);
        code = scg.getNextSequenceCode("test", "PC{N:0}", date1, null);
        assertEquals("PC2", code);
        code = scg.getNextSequenceCode("test", "PC{N:0}", date2, null);
        assertEquals("PC1", code);
        code = scg.getNextSequenceCode("test", "PC{N:0}", date2, null);
        assertEquals("PC2", code);
        code = scg.getNextSequenceCode("test", "PC{N:0}", date3, null);
        assertEquals("PC3", code);
        code = scg.getNextSequenceCode("test", "PC{N:0}", date3, null);
        assertEquals("PC4", code);

        code = scg.getNextSequenceCode("test", "GT{dd}/{yyyy}/PC/{ddd}/{N:0}", date1, null);
        assertEquals("GT25/2002/PC/145/1", code);
        code = scg.getNextSequenceCode("test", "GT{dd}/{yyyy}/PC/{ddd}/{N:0}", date1, null);
        assertEquals("GT25/2002/PC/145/2", code);
        code = scg.getNextSequenceCode("test", "GT{dd}/{yyyy}/PC/{ddd}/{N:0}", date2, null);
        assertEquals("GT26/2014/PC/207/1", code);
        code = scg.getNextSequenceCode("test", "GT{dd}/{yyyy}/PC/{ddd}/{N:0}", date2, null);
        assertEquals("GT26/2014/PC/207/2", code);
        code = scg.getNextSequenceCode("test", "GT{dd}/{yyyy}/PC/{ddd}/{N:0}", date3, null);
        assertEquals("GT27/2014/PC/208/1", code);
        code = scg.getNextSequenceCode("test", "GT{dd}/{yyyy}/PC/{ddd}/{N:0}", date3, null);
        assertEquals("GT27/2014/PC/208/2", code);
    }
    
    @Override
    protected void onSetup() throws Exception {
        scg = (SequenceCodeGenerator) getComponent("common-sequencecodegenerator");
    }

    @Override
    protected void onTearDown() throws Exception {

    }

}
