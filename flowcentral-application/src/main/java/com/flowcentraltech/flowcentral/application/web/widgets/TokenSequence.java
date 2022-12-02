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
package com.flowcentraltech.flowcentral.application.web.widgets;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.tcdng.unify.common.constants.StandardFormatType;
import com.tcdng.unify.common.util.NewlineToken;
import com.tcdng.unify.common.util.ParamToken;
import com.tcdng.unify.common.util.StringToken;
import com.tcdng.unify.common.util.StringTokenType;
import com.tcdng.unify.common.util.TextToken;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.constant.Editable;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Token sequence object.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class TokenSequence {

    private final EntityDef entityDef;

    private List<TokenSequenceEntry> entryList;

    private List<TokenSequenceEntry> viewEntryList;

    private final Editable editable;
    
    private String preview;
    
    public TokenSequence(EntityDef entityDef) {
        this(entityDef, Editable.TRUE);
    }

    public TokenSequence(EntityDef entityDef, Editable rootEditable) {
        this.entityDef = entityDef;
        this.entryList = new ArrayList<TokenSequenceEntry>();
        this.entryList.add(new TokenSequenceEntry(entityDef, rootEditable.isTrue()));
        this.viewEntryList = Collections.unmodifiableList(entryList);
        this.editable = rootEditable;
    }

    public TokenSequence(EntityDef entityDef, String parameterizedString) throws UnifyException {
        this(entityDef, parameterizedString, Editable.TRUE);
    }

    public TokenSequence(EntityDef entityDef, String parameterizedString, Editable editable) throws UnifyException {
        this.entityDef = entityDef;
        this.entryList = new ArrayList<TokenSequenceEntry>();
        this.viewEntryList = Collections.unmodifiableList(entryList);
        this.editable = editable;
        loadEntryList(parameterizedString, editable);
    }

    public int addTokenSequenceEntry(StringTokenType tokenType, String fieldName, String param, Editable editable)
            throws UnifyException {
        TokenSequenceEntry svo = new TokenSequenceEntry(entityDef, editable.isTrue());
        setFieldAndInputParams(svo, tokenType, fieldName, param);
        entryList.add(svo);
        return entryList.size() - 1;
    }

    public void clear() throws UnifyException {
        entryList.clear();
        entryList.add(new TokenSequenceEntry(entityDef, editable.isTrue()));
        preview();
    }

    public void moveUpEntry(int index) throws UnifyException {
        if (index > 0) {
            TokenSequenceEntry svo = entryList.remove(index);
            entryList.add(index - 1, svo);
            preview();
        }
    }

    public void moveDownEntry(int index) throws UnifyException {
        if (index < entryList.size() - 2) {
            TokenSequenceEntry svo = entryList.remove(index);
            entryList.add(index + 1, svo);
            preview();
        }
    }

    public void removeEntry(int index) throws UnifyException {
        entryList.remove(index);
        preview();
    }

    public EntityDef getEntityDef() {
        return entityDef;
    }

    public TokenSequenceEntry getEntry(int index) {
        return entryList.get(index);
    }

    public List<TokenSequenceEntry> getEntryList() {
        return viewEntryList;
    }

    public int size() {
        return entryList.size();
    }

    public void normalize() throws UnifyException {
        ListIterator<TokenSequenceEntry> it = entryList.listIterator();
        int i = 0;
        int lim = entryList.size() - 1;
        while (it.hasNext()) {
            TokenSequenceEntry svo = it.next();
            svo.normalize();
            if (!svo.isWithTokenType()) {
                if (i < lim) {
                    it.remove();
                }
            }
            i++;
        }

        TokenSequenceEntry last = entryList.get(entryList.size() - 1);
        if (last.isWithTokenType()) {
            entryList.add(new TokenSequenceEntry(entityDef, true));
        }
        
        preview();
    }

    public void preview() throws UnifyException {
        preview = getParameterizedString();
    }
        
    public String getPreview() {
        return preview;
    }

    private String getParameterizedString() throws UnifyException {
        int lim = entryList.size() - 1;
        if (lim > 0) {
            List<StringToken> tokens = new ArrayList<StringToken>();
            for (int i = 0; i < lim; i++) {
                TokenSequenceEntry fso = entryList.get(i);
                if (fso.isWithTokenType()) {
                    StringToken token = null;
                    switch (fso.getTokenType()) {
                        case FORMATTED_PARAM:
                            if (fso.isWithFieldName() && fso.isWithParam()) {
                                token = ParamToken.getFormattedParamToken(StandardFormatType.fromCode(fso.getParam()),
                                        fso.getFieldName());
                            }
                            break;
                        case GENERATOR_PARAM:
                            if (fso.isWithParam()) {
                                token = ParamToken.getGeneratorParamToken(fso.getParam());
                            }
                            break;
                        case NEWLINE:
                            token = new NewlineToken();
                            break;
                        case PARAM:
                            if (fso.isWithFieldName()) {
                                token = ParamToken.getParamToken(fso.getFieldName());
                            }
                            break;
                        case TEXT:
                            if (fso.isWithParam()) {
                                token = new TextToken(fso.getParam());
                            }
                            break;
                        default:
                            break;
                    }

                    if (token != null) {
                        tokens.add(token);
                    }
                }
            }

            return StringUtils.buildParameterizedString(tokens);
        }

        return null;
    }

    private void loadEntryList(String parameterizedString, Editable editable) throws UnifyException {
        if (parameterizedString != null) {
            preview = parameterizedString;
            for (StringToken token : StringUtils.breakdownParameterizedString(parameterizedString)) {
                TokenSequenceEntry fso = new TokenSequenceEntry(entityDef, editable.isTrue());
                String fieldName = null;
                String param = null;
                switch(token.getType()) {
                    case FORMATTED_PARAM:
                        fieldName = ((ParamToken) token).getParam();
                        param = ((ParamToken) token).getComponent();
                        break;
                    case GENERATOR_PARAM:
                        param = ((ParamToken) token).getParam();
                        break;
                    case NEWLINE:
                        break;
                    case PARAM:
                        fieldName = ((ParamToken) token).getParam();
                        break;
                    case TEXT:
                        param = token.getToken();
                        break;
                    default:
                        break;                    
                }

                setFieldAndInputParams(fso, token.getType(), fieldName,
                        param);
                entryList.add(fso);
            }
        }

        entryList.add(new TokenSequenceEntry(entityDef, editable.isTrue()));
    }

    private void setFieldAndInputParams(TokenSequenceEntry fso, StringTokenType tokenType, String fieldName,
            String param) throws UnifyException {
        fso.setTokenType(tokenType);
        fso.setFieldName(fieldName);
        fso.setParam(param);
        fso.normalize();
    }
}
