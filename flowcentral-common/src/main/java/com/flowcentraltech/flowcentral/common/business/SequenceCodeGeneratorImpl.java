/*
 * Copyright 2021-2022 FlowCentral Technologies Limited.
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.flowcentraltech.flowcentral.common.constants.SequencePartType;
import com.flowcentraltech.flowcentral.common.data.SequenceDef;
import com.flowcentraltech.flowcentral.common.data.SequencePartDef;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.FactoryMap;
import com.tcdng.unify.core.system.SequenceNumberService;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of common sequence code generator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component("common-sequencecodegenerator")
public class SequenceCodeGeneratorImpl extends AbstractSequenceCodeGenerator {

    private static final List<SequencePartType> varTypes = Collections.unmodifiableList(Arrays.asList(SequencePartType.SEQUENCE_NUMBER,
            SequencePartType.SEQUENCE_NUMBER_BY_DATE, SequencePartType.LONG_YEAR, SequencePartType.SHORT_YEAR, SequencePartType.DAY_OF_MONTH, SequencePartType.DAY_OF_YEAR));

    @Configurable
    private SequenceNumberService seqNumberService;

    private final FactoryMap<String, SequenceDef> sequenceDefs;

    public SequenceCodeGeneratorImpl() {
        sequenceDefs = new FactoryMap<String, SequenceDef>()
            {

                @Override
                protected SequenceDef create(String definition, Object... arg1) throws Exception {
                    return createSequenceDef(definition);
                }

            };
    }

    public void setSeqNumberService(SequenceNumberService seqNumberService) {
        this.seqNumberService = seqNumberService;
    }

    @Override
    public String getCodeSkeleton(String sequenceDefintion) throws UnifyException {
        return resolveSessionMessage("$m{placeholder.generated}");
    }

    @Override
    public SequenceDef getSequenceDefinition(String sequenceDefintion) throws UnifyException {
        return sequenceDefs.get(sequenceDefintion);
    }

    @Override
    public String getNextSequenceCode(String ownerId, String sequenceDefintion) throws UnifyException {
        return getNextSequenceCode(ownerId, sequenceDefintion, seqNumberService.getNow());
    }

    @Override
    public String getNextSequenceCode(String ownerId, String sequenceDefintion, Date date) throws UnifyException {
        final String seqKey = StringUtils.dotify(ownerId, sequenceDefintion);
        SequenceDef _sequenceDef = sequenceDefs.get(sequenceDefintion);
        Calendar cal = null;
        if (_sequenceDef.isWithDatePart()) {
            cal = Calendar.getInstance();
            cal.setTime(date);
        }

        StringBuilder sb = new StringBuilder();
        for (SequencePartDef partDef : _sequenceDef.getPartList()) {
            switch (partDef.getType()) {
                case DAY_OF_MONTH:
                    sb.append(cal.get(Calendar.DAY_OF_MONTH));
                    break;
                case DAY_OF_YEAR:
                    sb.append(cal.get(Calendar.DAY_OF_YEAR));
                    break;
                case LONG_YEAR:
                    sb.append(cal.get(Calendar.YEAR));
                    break;
                case SHORT_YEAR: {
                    String year = String.valueOf(cal.get(Calendar.YEAR));
                    sb.append(year.substring(year.length() - 2));
                }
                    break;
                case SEQUENCE_NUMBER: {
                    String num = String.valueOf(seqNumberService.getNextSequenceNumber(seqKey));
                    if (num.length() < partDef.getNumLen()) {
                        num = StringUtils.padLeft(num, '0', partDef.getNumLen());
                    }

                    sb.append(num);
                }
                    break;
                case SEQUENCE_NUMBER_BY_DATE: {
                    String num = String.valueOf(seqNumberService.getNextSequenceNumber(seqKey, date));
                    if (num.length() < partDef.getNumLen()) {
                        num = StringUtils.padLeft(num, '0', partDef.getNumLen());
                    }

                    sb.append(num);
                }
                    break;
                case TEXT:
                    sb.append(partDef.getText());
                    break;
                default:
                    break;
            }
        }

        return sb.toString();
    }

    private SequenceDef createSequenceDef(String definition) throws UnifyException {
        // Break down definition to parts
        final int len = definition.length();
        boolean withDatePart = false;
        int fromIndex = 0;
        int index = 0;
        List<SequencePartDef> partList = new ArrayList<SequencePartDef>();
        Set<SequencePartType> exist = new HashSet<SequencePartType>();
        while (fromIndex < len) {
            index = definition.indexOf('{', fromIndex);
            if (index >= 0) {
                SequencePartType _type = null;
                for (SequencePartType type : varTypes) {
                    if (index == definition.indexOf(type.track(), index)) {
                        _type = type;
                        break;
                    }
                }
                
                if (_type == null) {
                    throwOperationErrorException(new RuntimeException(
                            "Invalid sequence code defintion. Unknown placeholder ["
                                    + definition + "]."));
                }
                

                if (exist.contains(_type)) {
                    throwOperationErrorException(new RuntimeException(
                            "Invalid sequence code defintion. Repeated parts for [" + definition + "]."));
                }
                exist.add(_type);
                withDatePart |= _type.isDatePart();

                if (fromIndex < index) {
                    partList.add(new SequencePartDef(definition.substring(fromIndex, index)));
                }

                fromIndex = index + _type.track().length();
                if (_type.isSequenceNumber()) {
                    index = definition.indexOf('}', fromIndex);
                    if (index <= 0) {
                        throwOperationErrorException(new RuntimeException(
                                "Invalid sequence code defintion. Expected closing brace for number placeholder ["
                                        + definition + "]."));
                    }

                    int numLen = Integer.parseInt(definition.substring(fromIndex, index));
                    partList.add(new SequencePartDef(_type, numLen));
                    fromIndex = index + 1;
                } else {
                    partList.add(new SequencePartDef(_type));
                }
            }
            
            if (index < 0) {
                partList.add(new SequencePartDef(definition.substring(fromIndex)));
                fromIndex = len;
            }
        }

        if (!exist.contains(SequencePartType.SEQUENCE_NUMBER) && !exist.contains(SequencePartType.SEQUENCE_NUMBER_BY_DATE)) {
            throwOperationErrorException(new RuntimeException(
                    "Invalid sequence code defintion. No sequence number placeholder found [" + definition + "]."));
        }

        // Build skeleton
        StringBuilder sb = new StringBuilder();
        for (SequencePartDef part : partList) {
            switch (part.getType()) {
                case DAY_OF_MONTH:
                case DAY_OF_YEAR:
                case LONG_YEAR:
                case SHORT_YEAR:
                    sb.append(part.getType().skeleton());
                    break;
                case SEQUENCE_NUMBER:
                case SEQUENCE_NUMBER_BY_DATE:
                    if (part.getNumLen() > 0) {
                        for (int i = 0; i < part.getNumLen(); i++) {
                            sb.append('_');
                        }
                    } else {
                        sb.append('_');
                    }
                    break;
                case TEXT:
                    sb.append(part.getText());
                    break;
                default:
                    break;
            }
        }

        return new SequenceDef(sb.toString(), partList, withDatePart);
    }
}
