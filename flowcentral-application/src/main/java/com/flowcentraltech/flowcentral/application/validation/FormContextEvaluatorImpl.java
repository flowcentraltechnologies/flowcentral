/*
 * Copyright 2021-2023 FlowCentral Technologies Limited.
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

import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.EntityMatcher;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.FieldValidationPolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormReviewPolicyDef;
import com.flowcentraltech.flowcentral.application.data.FormValidationPolicyDef;
import com.flowcentraltech.flowcentral.application.data.UniqueConstraintDef;
import com.flowcentraltech.flowcentral.application.web.data.FormContext;
import com.flowcentraltech.flowcentral.application.web.data.FormContext.FormWidgetState;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormReviewPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.ConsolidatedFormValidationPolicy;
import com.flowcentraltech.flowcentral.common.business.policies.ReviewResult;
import com.flowcentraltech.flowcentral.common.constants.EvaluationMode;
import com.flowcentraltech.flowcentral.common.data.TargetFormMessage;
import com.flowcentraltech.flowcentral.common.util.ValidationUtils;
import com.flowcentraltech.flowcentral.configuration.constants.FormReviewType;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Component;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.data.ValueStoreReader;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.StringUtils;

/**
 * Default implementation of a form context evaluator.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
@Component(ApplicationModuleNameConstants.FORMCONTEXT_EVALUATOR)
public class FormContextEvaluatorImpl extends AbstractUnifyComponent implements FormContextEvaluator {

    @Configurable
    private EnvironmentService environmentService;

    private static final Map<String, String> widgetValidatorMap;

    static {
        Map<String, String> map = new HashMap<String, String>();
        map.put("application.email", "fc-emailvalidator");
        map.put("application.emailset", "fc-emailvalidator");
        map.put("application.domain", "fc-domainvalidator");
        map.put("application.mobile", "fc-mobilevalidator");
        map.put("application.mobileset", "fc-mobilevalidator");
        map.put("application.website", "fc-websitevalidator");
        map.put("application.javafieldname", "fc-javafieldnamevalidator");
        widgetValidatorMap = Collections.unmodifiableMap(map);
    }

    public void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void evaluateFormContext(final FormContext ctx, final EvaluationMode evaluationMode) throws UnifyException {
        ctx.clearValidationErrors();
        ctx.clearReviewErrors();
        if (evaluationMode.evaluation()) {
            final FormDef formDef = ctx.getFormDef();
            final EntityDef entityDef = formDef.getEntityDef();
            final Object inst = ctx.getInst();
            final AppletUtilities au = ctx.au();
            final ValueStore instValueStore = ctx.getFormValueStore();
            final ValueStoreReader instValueStoreReader = instValueStore.getReader();
            Map<String, Object> fieldsInScope = new HashMap<String, Object>();
            // Pull fields in scope and check required fields and lengths
            for (FormWidgetState formWidgetState : ctx.getFormWidgetStateList()) {
                if (formWidgetState.isVisible() && !formWidgetState.isDisabled()
                       && ctx.isVisibleMainSection(formWidgetState.getSectionName())) {
                    String fieldName = formWidgetState.getFieldName();
                    Object val = DataUtils.getBeanProperty(Object.class, inst, fieldName);
                    fieldsInScope.put(fieldName, val);
                    if (formWidgetState.isRequired() && ValidationUtils.isBlank(val)) {
                        ctx.addValidationError(fieldName, getApplicationMessage(
                                "application.validation.formfield.required", formWidgetState.getFieldLabel()));
                    } else if (val instanceof String) {
                        String valString = (String) val;
                        if (ValidationUtils.isLessThanMinLen(valString,
                                DataUtils.convert(int.class, formWidgetState.getMinLen()))) {
                            ctx.addValidationError(fieldName,
                                    getApplicationMessage("application.validation.formfield.lessthanminlen",
                                            formWidgetState.getFieldLabel(),
                                            DataUtils.convert(int.class, formWidgetState.getMinLen())));
                        } else if (ValidationUtils.isGreaterThanMaxLen(valString,
                                DataUtils.convert(int.class, formWidgetState.getMaxLen()))) {
                            ctx.addValidationError(fieldName,
                                    getApplicationMessage("application.validation.formfield.greaterthanmaxlen",
                                            formWidgetState.getFieldLabel(),
                                            DataUtils.convert(int.class, formWidgetState.getMaxLen())));
                        } else {
                            String validatorName = widgetValidatorMap.get(formWidgetState.getWidgetName());
                            if (!StringUtils.isBlank(validatorName)) {
                                Validator validator = (Validator) getComponent(validatorName);
                                if ("application.emailset".equals(formWidgetState.getWidgetName())
                                        || "application.mobileset".equals(formWidgetState.getWidgetName())) {
                                    String[] contacts = ((String) val).split(";|,");
                                    for (String contact : contacts) {
                                        if (!validator.validate(null, contact.trim())) {
                                            ctx.addValidationError(fieldName, validator.getFailureMessage(null,
                                                    entityDef.getFieldDef(fieldName).getFieldLabel()));
                                            break;
                                        }
                                    }
                                } else {
                                    if (!validator.validate(null, val)) {
                                        ctx.addValidationError(fieldName, validator.getFailureMessage(null,
                                                entityDef.getFieldDef(fieldName).getFieldLabel()));
                                    }
                                }
                            }
                        }
                    }
                }
            }

            if (fieldsInScope != null && !fieldsInScope.isEmpty()) {
                // Check field validations
                for (FieldValidationPolicyDef policyDef : formDef.getFieldValidationPolicies()) {
                    String fieldName = policyDef.getFieldName();
                    if (fieldsInScope.containsKey(fieldName) && !ctx.isWithFieldError(fieldName)) {
                        Validator validator = (Validator) getComponent(policyDef.getValidator());
                        Object val = fieldsInScope.get(fieldName);
                        if (val != null && !validator.validate(policyDef.getRule(), val)) {
                            ctx.addValidationError(fieldName, validator.getFailureMessage(policyDef.getRule(),
                                    entityDef.getFieldDef(fieldName).getFieldLabel()));
                        }
                    }
                }

                // Check unique constraints
                final Object id = DataUtils.getBeanProperty(Object.class, inst, "id");
                if (entityDef.isWithUniqueConstraints()) {
                    boolean isUpdate = evaluationMode.update();
                    if (isUpdate || evaluationMode.create()) {
                        final EntityClassDef entityClassDef = au.getEntityClassDef(entityDef.getLongName());
                        for (UniqueConstraintDef constDef : entityDef.getUniqueConstraintList()) {
                            List<String> fieldList = constDef.getFieldList();
                            if (!ctx.isWithFieldError(fieldList)) {
                                Query query = Query.of((Class<? extends Entity>) entityClassDef.getEntityClass());
                                if (isUpdate) {
                                    query.addNotEquals("id", id);
                                }

                                for (String fieldName : fieldList) {
                                    Object val = null;
                                    if (!fieldsInScope.containsKey(fieldName)) {
                                        val = DataUtils.getBeanProperty(Object.class, inst, fieldName);
                                        fieldsInScope.put(fieldName, val);
                                    } else {
                                        val = fieldsInScope.get(fieldName);
                                    }

                                    if (constDef.isCaseInsensitive() && val instanceof String) {
                                        query.addIEquals(fieldName, (String) val);
                                    } else {
                                        query.addEquals(fieldName, val);
                                    }
                                }

                                if (environmentService.countAll(query) > 0) {
                                    StringBuilder sb = new StringBuilder();
                                    boolean appendSym = false;
                                    for (String fieldName : fieldList) {
                                        if (appendSym) {
                                            sb.append(", ");
                                        } else {
                                            appendSym = true;
                                        }

                                        sb.append(entityDef.getFieldDef(fieldName).getFieldLabel()).append(" = ")
                                                .append(fieldsInScope.get(fieldName));
                                    }

                                    String msg = getApplicationMessage("application.validation.uniqueconstraint",
                                            sb.toString());
                                    for (String fieldName : fieldList) {
                                        ctx.addValidationError(fieldName, msg);
                                    }
                                }
                            }
                        }
                    }
                }

                if (!ctx.isWithFormErrors()) {
                    // Check form validations
                    if (formDef.isWithConsolidatedFormValidation()) {
                        ConsolidatedFormValidationPolicy policy = au.getComponent(
                                ConsolidatedFormValidationPolicy.class, formDef.getConsolidatedFormValidation());
                        for (TargetFormMessage message : policy.validate(evaluationMode, instValueStore)) {
                            addValidationMessage(ctx, message);
                        }
                    }

                    if (formDef.isWithFormValidationPolicy()) {
                        final Date now = au.getNow();
                        for (FormValidationPolicyDef policyDef : formDef.getFormValidationPolicies()) {
                            if (policyDef.isErrorMatcher()) {
                                EntityMatcher matcher = au.getComponent(EntityMatcher.class,
                                        policyDef.getErrorMatcher());
                                if (matcher.match(entityDef, evaluationMode, instValueStore)) {
                                    addValidationMessage(ctx, policyDef);
                                    continue;
                                }
                            }

                            if (policyDef.isErrorCondition() && policyDef.getErrorCondition()
                                    .getObjectFilter(entityDef, instValueStoreReader, now).matchObject(inst)) {
                                addValidationMessage(ctx, policyDef);
                            }
                        }
                    }
                }
            }
        }
    }

    @Override
    public ReviewResult reviewFormContext(FormContext ctx, EvaluationMode evaluationMode, FormReviewType reviewType)
            throws UnifyException {
        ReviewResult.Builder rrb = ReviewResult.newBuilder();
        ctx.clearReviewErrors();
        if (ctx.isWithReviewPolicies(reviewType)) {
            final FormDef formDef = ctx.getFormDef();
            final EntityDef entityDef = formDef.getEntityDef();
            final AppletUtilities au = ctx.au();
            final Date now = au.getNow();
            final Object inst = ctx.getInst();
            final ValueStore instValueStore = ctx.getFormValueStore();
            final ValueStoreReader instValueStoreReader = instValueStore.getReader();
            if (formDef.isWithConsolidatedFormReview()) {
                ConsolidatedFormReviewPolicy policy = au.getComponent(ConsolidatedFormReviewPolicy.class,
                        formDef.getConsolidatedFormReview());
                for (TargetFormMessage message : policy.review(instValueStore, reviewType)) {
                    ctx.addReviewError(rrb, message);
                }
            }

            for (FormReviewPolicyDef policyDef : ctx.getReviewPolicies(reviewType)) {
                if (policyDef.isErrorMatcher()) {
                    EntityMatcher matcher = au.getComponent(EntityMatcher.class, policyDef.getErrorMatcher());
                    if (matcher.match(entityDef, evaluationMode, instValueStore)) {
                        ctx.addReviewError(rrb, policyDef);
                        continue;
                    }
                }

                if (policyDef.isErrorCondition() && policyDef.getErrorCondition()
                        .getObjectFilter(entityDef, instValueStoreReader, now).matchObject(inst)) {
                    ctx.addReviewError(rrb, policyDef);
                }
            }
        }

        return rrb.build();
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    private void addValidationMessage(FormContext ctx, TargetFormMessage message) {
        if (message.isWithTargets()) {
            String msg = message.getFormMessage().getMessage();
            for (String target : message.getTargets()) {
                ctx.addValidationError(target, msg);
            }
        } else {
            ctx.addValidationError(message.getFormMessage());
        }
    }

    private void addValidationMessage(FormContext ctx, FormValidationPolicyDef policyDef) {
        if (policyDef.isWithTarget()) {
            String msg = policyDef.getMessage();
            for (String target : policyDef.getTarget()) {
                ctx.addValidationError(target, msg);
            }
        } else {
            ctx.addValidationError(policyDef.getMessage());
        }
    }
}
