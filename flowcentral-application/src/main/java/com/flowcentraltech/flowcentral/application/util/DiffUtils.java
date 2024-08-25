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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.constants.AppletPropertyConstants;
import com.flowcentraltech.flowcentral.application.constants.DataChangeType;
import com.flowcentraltech.flowcentral.application.data.AppletDef;
import com.flowcentraltech.flowcentral.application.data.AssignmentPageDef;
import com.flowcentraltech.flowcentral.application.data.Diff;
import com.flowcentraltech.flowcentral.application.data.DiffEntity;
import com.flowcentraltech.flowcentral.application.data.DiffEntityField;
import com.flowcentraltech.flowcentral.application.data.EntityClassDef;
import com.flowcentraltech.flowcentral.application.data.EntityDef;
import com.flowcentraltech.flowcentral.application.data.EntityFieldDef;
import com.flowcentraltech.flowcentral.application.data.FilterGroupDef.FilterType;
import com.flowcentraltech.flowcentral.application.data.FormDef;
import com.flowcentraltech.flowcentral.application.data.FormFieldDef;
import com.flowcentraltech.flowcentral.application.data.FormSectionDef;
import com.flowcentraltech.flowcentral.application.data.FormTabDef;
import com.flowcentraltech.flowcentral.common.util.RestrictionUtils;
import com.flowcentraltech.flowcentral.configuration.constants.AppletType;
import com.flowcentraltech.flowcentral.configuration.constants.TabContentType;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.criterion.Restriction;
import com.tcdng.unify.core.data.BeanValueStore;
import com.tcdng.unify.core.data.Formats;
import com.tcdng.unify.core.data.Listable;
import com.tcdng.unify.core.database.Entity;
import com.tcdng.unify.core.database.Query;
import com.tcdng.unify.core.list.ListCommand;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.core.util.ReflectUtils;
import com.tcdng.unify.core.util.StringUtils;
import com.tcdng.unify.web.data.AssignParams;

/**
 * Diff utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class DiffUtils {

    private DiffUtils() {

    }

    /**
     * Performs a form difference.
     * 
     * @param au
     *                      applet utility
     * @param formDef
     *                      the form definition
     * @param leftEntityId
     *                      the left entity ID
     * @param rightEntityId
     *                      the right entity ID
     * @param formats
     *                      formats object
     * @return the difference
     * @throws UnifyException
     *                        if an error occurs
     */
    public static Diff diff(AppletUtilities au, FormDef formDef, Long leftEntityId, Long rightEntityId,
            Formats.Instance formats) throws UnifyException {
        return DiffUtils.diff(au, formDef, leftEntityId, rightEntityId, formats, null, null);
    }

    private static Diff diff(AppletUtilities au, FormDef formDef, Long leftEntityId, Long rightEntityId,
            Formats.Instance formats, String label, String parentEntityName) throws UnifyException {
        final DiffEntityParts leftDiffEntityParts = DiffUtils.getDiffEntityParts(au, formDef, leftEntityId, formats,
                parentEntityName);
        final DiffEntityParts rightDiffEntityParts = DiffUtils.getDiffEntityParts(au, formDef, rightEntityId, formats,
                parentEntityName);
        return DiffUtils.diff(au, formDef, leftDiffEntityParts, rightDiffEntityParts, formats, label);
    }

    @SuppressWarnings("unchecked")
    private static Diff diff(AppletUtilities au, FormDef formDef, DiffEntityParts leftDiffEntityParts,
            DiffEntityParts rightDiffEntityParts, Formats.Instance formats, String label) throws UnifyException {
        final Date now = au.getNow();

        List<DiffEntityField> lfields = Collections.emptyList();
        List<DiffEntityField> rfields = Collections.emptyList();
        if (leftDiffEntityParts != null && rightDiffEntityParts == null) {
            lfields = DiffUtils.getFields(leftDiffEntityParts, DataChangeType.NEW);
        } else if (leftDiffEntityParts == null && rightDiffEntityParts != null) {
            rfields = DiffUtils.getFields(rightDiffEntityParts, DataChangeType.DELETED);
        } else if (leftDiffEntityParts != null && rightDiffEntityParts != null) {
            final EntityDef entityDef = leftDiffEntityParts.getEntityDef();
            lfields = new ArrayList<DiffEntityField>();
            rfields = new ArrayList<DiffEntityField>();
            List<DiffFieldPart> leftFieldParts = leftDiffEntityParts.getFieldParts();
            List<DiffFieldPart> rightFieldParts = rightDiffEntityParts.getFieldParts();
            final int len = leftFieldParts.size();
            for (int i = 0; i < len; i++) {
                final DiffFieldPart lpart = leftFieldParts.get(i);
                final String lvalStr = lpart.getVal();
                final String rvalStr = rightFieldParts.get(i).getVal();
                DataChangeType lChangeType = DataChangeType.NONE;
                DataChangeType rChangeType = DataChangeType.NONE;
                if (!DataUtils.equals(lvalStr, rvalStr)) {
                    lChangeType = lvalStr != null ? (rvalStr != null ? DataChangeType.UPDATED : DataChangeType.NEW)
                            : DataChangeType.NONE;

                    rChangeType = lvalStr != null ? (rvalStr != null ? DataChangeType.NONE : DataChangeType.NONE)
                            : DataChangeType.DELETED;
                }

                final String fieldName = lpart.getFieldName();
                final boolean number = entityDef.getFieldDef(fieldName).isNumber();
                lfields.add(new DiffEntityField(lChangeType, fieldName, lpart.getLabel(), lvalStr, number));
                rfields.add(new DiffEntityField(rChangeType, fieldName, lpart.getLabel(), rvalStr, number));
            }
        }

        final DiffEntity dleft = new DiffEntity(label == null ? au.resolveSessionMessage("$m{formdiff.draft}") : label,
                lfields);
        final DiffEntity dright = new DiffEntity(
                label == null ? au.resolveSessionMessage("$m{formdiff.original}") : label, rfields);

        final List<Diff> children = new ArrayList<Diff>();
        final List<FormTabDef> tabs = formDef.getFormTabDefList();
        final int len = tabs.size();
        for (int i = 1; i < len; i++) {
            FormTabDef childTabDef = tabs.get(i);
            TabContentType contentType = childTabDef.getContentType();
            if (contentType.isChildOrChildList()) {
                final AppletDef _appletDef = au.getAppletDef(childTabDef.getApplet());
                final AppletType appletType = _appletDef.getType();
                if (!_appletDef.getPropValue(boolean.class, AppletPropertyConstants.SEARCH_TABLE_DIFF_IGNORE)) {
                    final List<Long> lChildIdList = DiffUtils.getChildIdList(au, childTabDef, leftDiffEntityParts, now,
                            contentType.isChildList());
                    final List<Long> rChildIdList = DiffUtils.getChildIdList(au, childTabDef, rightDiffEntityParts, now,
                            contentType.isChildList());
                    final int clen = lChildIdList.size() < rChildIdList.size() ? rChildIdList.size()
                            : lChildIdList.size();
                    if (clen > 0) {
                        if (appletType.isAssignment()) {
                            final String assignmentPage = _appletDef.getPropValue(String.class,
                                    AppletPropertyConstants.ASSIGNMENT_PAGE);
                            AssignmentPageDef assignmentPageDef = au.getAssignmentPageDef(assignmentPage);
                            final EntityClassDef assignEntityClassDef = au
                                    .getEntityClassDef(assignmentPageDef.getEntity());
                            final List<Long> lAssignedIdList = !lChildIdList.isEmpty()
                                    ? au.environment()
                                            .valueList(Long.class, assignmentPageDef.getAssignField(), Query
                                                    .of((Class<? extends Entity>) assignEntityClassDef.getEntityClass())
                                                    .addAmongst("id", lChildIdList))
                                    : Collections.emptyList();

                            final List<Long> rAssignedIdList = !rChildIdList.isEmpty()
                                    ? au.environment()
                                            .valueList(Long.class, assignmentPageDef.getAssignField(), Query
                                                    .of((Class<? extends Entity>) assignEntityClassDef.getEntityClass())
                                                    .addAmongst("id", rChildIdList))
                                    : Collections.emptyList();

                            ListCommand<AssignParams> listCommand = (ListCommand<AssignParams>) au
                                    .getComponent(ListCommand.class, assignmentPageDef.getAssignList());

                            List<? extends Listable> lListables = !lAssignedIdList.isEmpty()
                                    ? listCommand.execute(Locale.getDefault(), new AssignParams(lAssignedIdList))
                                    : Collections.emptyList();
                            List<? extends Listable> rListables = !rAssignedIdList.isEmpty()
                                    ? listCommand.execute(Locale.getDefault(), new AssignParams(rAssignedIdList))
                                    : Collections.emptyList();
                            Diff _diff = DiffUtils.diff(lListables, rListables, childTabDef.getLabel());
                            children.add(_diff);
                        } else if (appletType.isEntityList()) {
                            final EntityDef entityDef = leftDiffEntityParts != null ? leftDiffEntityParts.getEntityDef()
                                    : rightDiffEntityParts.getEntityDef();
                            final String cFormName = _appletDef.getPropValue(String.class,
                                    AppletPropertyConstants.MAINTAIN_FORM);
                            FormDef cformDef = au.getFormDef(cFormName);
                            DiffParts diffParts = DiffUtils.getDiffParts(au, cformDef, lChildIdList, rChildIdList,
                                    formats, entityDef.getLongName());
                            diffParts.sort();

                            final List<DiffEntityParts> leftParts = diffParts.getLeftParts();
                            final List<DiffEntityParts> rightParts = diffParts.getRightParts();

                            for (int j = 0; j < clen; j++) {
                                DiffEntityParts cLeftParts = j < leftParts.size() ? leftParts.get(j) : null;
                                DiffEntityParts cRightParts = j < rightParts.size() ? rightParts.get(j) : null;
                                Diff _diff = DiffUtils.diff(au, cformDef, cLeftParts, cRightParts, formats,
                                        childTabDef.getLabel());
                                children.add(_diff);
                            }
                        }
                    }
                }
            }
        }

        return new Diff(dleft, dright, children);
    }

    private static Diff diff(List<? extends Listable> lListables, List<? extends Listable> rListables, String label)
            throws UnifyException {
        // Sets
        final Set<String> lset = new HashSet<String>();
        for (Listable listable : lListables) {
            lset.add(listable.getListDescription());
        }

        final Set<String> rset = new HashSet<String>();
        for (Listable listable : rListables) {
            rset.add(listable.getListDescription());
        }

        // Fields
        final List<DiffEntityField> lfields = new ArrayList<DiffEntityField>();
        final List<DiffEntityField> rfields = new ArrayList<DiffEntityField>();
        for (Listable listable : lListables) {
            lfields.add(new DiffEntityField(
                    rset.contains(listable.getListDescription()) ? DataChangeType.NONE : DataChangeType.NEW,
                    listable.getListKey(), "item", listable.getListDescription(), false));
        }

        for (Listable listable : rListables) {
            rfields.add(new DiffEntityField(
                    lset.contains(listable.getListDescription()) ? DataChangeType.NONE : DataChangeType.DELETED,
                    listable.getListKey(), "item", listable.getListDescription(), false));
        }

        final DiffEntity dleft = new DiffEntity(label, lfields);
        final DiffEntity dright = new DiffEntity(label, rfields);
        return new Diff(dleft, dright);
    }

    @SuppressWarnings("unchecked")
    private static List<Long> getChildIdList(AppletUtilities au, FormTabDef childTabDef,
            DiffEntityParts parentDiffEntityParts, Date now, boolean list) throws UnifyException {
        if (parentDiffEntityParts.isPresent()) {
            Restriction childRestriction = au.getChildRestriction(parentDiffEntityParts.getEntityDef(),
                    childTabDef.getReference(), parentDiffEntityParts.getInst());
            if (list) {
                Restriction tabRestriction = childTabDef.getRestriction(FilterType.TAB,
                        new BeanValueStore(parentDiffEntityParts.getInst()).getReader(), now);
                childRestriction = RestrictionUtils.and(childRestriction, tabRestriction);
            }

            EntityClassDef cEntityDef = au.getAppletEntityClassDef(childTabDef.getApplet());
            List<Long> resultList = au.environment().valueList(Long.class, "id",
                    Query.of((Class<? extends Entity>) cEntityDef.getEntityClass()).addRestriction(childRestriction));
            Collections.sort(resultList);
            return resultList;
        }

        return Collections.emptyList();
    }

    private static List<DiffEntityField> getFields(DiffEntityParts diffEntityParts, DataChangeType changeType)
            throws UnifyException {
        final EntityDef entityDef = diffEntityParts.getEntityDef();
        List<DiffEntityField> fields = new ArrayList<DiffEntityField>();
        for (DiffFieldPart diffFieldPart : diffEntityParts.getFieldParts()) {
            fields.add(new DiffEntityField(diffFieldPart.getVal() == null ? DataChangeType.NONE : changeType,
                    diffFieldPart.getFieldName(), diffFieldPart.getLabel(), diffFieldPart.getVal(),
                    entityDef.getFieldDef(diffFieldPart.getFieldName()).isNumber()));
        }

        return fields;
    }

    private static DiffParts getDiffParts(AppletUtilities au, FormDef formDef, List<Long> leftEntityIds,
            List<Long> rightEntityIds, Formats.Instance formats, String parentEntityName) throws UnifyException {
        final List<DiffEntityParts> leftParts = new ArrayList<DiffEntityParts>();
        for (Long id : leftEntityIds) {
            leftParts.add(DiffUtils.getDiffEntityParts(au, formDef, id, formats, parentEntityName));
        }

        final List<DiffEntityParts> rightParts = new ArrayList<DiffEntityParts>();
        for (Long id : rightEntityIds) {
            rightParts.add(DiffUtils.getDiffEntityParts(au, formDef, id, formats, parentEntityName));
        }

        return new DiffParts(DataUtils.unmodifiableList(leftParts), DataUtils.unmodifiableList(rightParts));
    }

    @SuppressWarnings("unchecked")
    private static DiffEntityParts getDiffEntityParts(AppletUtilities au, FormDef formDef, Long id,
            Formats.Instance formats, String parentEntityName) throws UnifyException {
        final EntityDef entityDef = formDef.getEntityDef();
        final EntityClassDef entityClassDef = au.getEntityClassDef(entityDef.getLongName());
        final Entity inst = id != null
                ? au.environment().find((Class<? extends Entity>) entityClassDef.getEntityClass(), id)
                : null;
        if (inst != null) {
            final List<DiffFieldPart> fieldParts = new ArrayList<DiffFieldPart>();
            final FormTabDef formTabDef = formDef.getFormTabDefList().get(0);
            for (FormSectionDef formSectionDef : formTabDef.getFormSectionDefList()) {
                for (FormFieldDef formFieldDef : formSectionDef.getFormFieldDefList()) {
                    final String fieldName = formFieldDef.getFieldName();
                    final EntityFieldDef entityFieldDef = formFieldDef.getEntityFieldDef();
                    if (entityFieldDef.isForeignKey() && entityFieldDef.isEntityRef()
                            && entityFieldDef.getRefDef().getEntity().equals(parentEntityName)) {
                        continue;
                    }

                    final Object val = ReflectUtils.getBeanProperty(inst, fieldName);
                    final String valStr = formats.format(val);
                    fieldParts.add(new DiffFieldPart(fieldName, formFieldDef.getFieldLabel(), valStr));
                }
            }

            return new DiffEntityParts(entityDef, inst, DataUtils.unmodifiableList(fieldParts));
        }

        return null;
    }

    private static class DiffParts {

        private final List<DiffEntityParts> leftParts;

        private final List<DiffEntityParts> rightParts;

        private List<DiffEntityParts> cleftParts;

        private List<DiffEntityParts> crightParts;

        public DiffParts(List<DiffEntityParts> leftParts, List<DiffEntityParts> rightParts) {
            this.leftParts = leftParts;
            this.rightParts = rightParts;
            this.cleftParts = Collections.emptyList();
            this.crightParts = Collections.emptyList();
        }

        public List<DiffEntityParts> getLeftParts() {
            return cleftParts;
        }

        public List<DiffEntityParts> getRightParts() {
            return crightParts;
        }

        public void sort() {
            cleftParts = new ArrayList<DiffEntityParts>();
            crightParts = new ArrayList<DiffEntityParts>();

            for (DiffEntityParts _rightParts : rightParts) {
                for (DiffEntityParts _leftParts : leftParts) {
                    _leftParts.match(_rightParts);
                }
            }

            Set<DiffEntityParts> leftBucket = new LinkedHashSet<DiffEntityParts>(leftParts);
            Set<DiffEntityParts> rightBucket = new LinkedHashSet<DiffEntityParts>(rightParts);
            for (DiffEntityParts _leftParts : leftParts) {
                for (DiffMatch diffMatch : _leftParts.getMatches()) {
                    if (rightBucket.remove(diffMatch.getDiffEntityParts())) {
                        cleftParts.add(_leftParts);
                        crightParts.add(diffMatch.getDiffEntityParts());
                        leftBucket.remove(_leftParts);
                        break;
                    }
                }
            }

            for (DiffEntityParts _leftParts : leftBucket) {
                cleftParts.add(_leftParts);
            }

            for (DiffEntityParts _rightParts : rightBucket) {
                crightParts.add(_rightParts);
            }
        }
    }

    private static class DiffEntityParts {

        private final EntityDef entityDef;

        private final Entity inst;

        private final List<DiffFieldPart> fieldParts;

        private List<DiffMatch> matches;

        public DiffEntityParts(EntityDef entityDef, Entity inst, List<DiffFieldPart> fieldParts) {
            this.entityDef = entityDef;
            this.inst = inst;
            this.fieldParts = fieldParts;
            this.matches = new ArrayList<DiffMatch>();
        }

        public EntityDef getEntityDef() {
            return entityDef;
        }

        public Entity getInst() {
            return inst;
        }

        public List<DiffFieldPart> getFieldParts() {
            return fieldParts;
        }

        public List<DiffMatch> getMatches() {
            Collections.sort(matches, new Comparator<DiffMatch>()
                {
                    public int compare(DiffMatch m1, DiffMatch m2) {
                        return m2.getMatch() - m1.getMatch();
                    }
                });

            return matches;
        }

        public boolean isPresent() {
            return inst != null && !fieldParts.isEmpty();
        }

        public int size() {
            return fieldParts != null ? fieldParts.size() : 0;
        }

        public void match(DiffEntityParts parts) {
            if (isPresent() && parts.isPresent() && entityDef.getLongName().equals(parts.getEntityDef().getLongName())
                    && size() == parts.size()) {
                final List<DiffFieldPart> _fieldParts = parts.getFieldParts();
                final int len = size();
                int match = 0;
                for (int i = 0; i < len; i++) {
                    if (DataUtils.equals(fieldParts.get(i).getVal(), _fieldParts.get(i).getVal())) {
                        match++;
                    }
                }

                if (match > 0) {
                    matches.add(new DiffMatch(parts, match));
                }
            }
        }
    }

    private static class DiffMatch {

        private DiffEntityParts diffEntityParts;

        private int match;

        public DiffMatch(DiffEntityParts diffEntityParts, int match) {
            this.diffEntityParts = diffEntityParts;
            this.match = match;
        }

        public DiffEntityParts getDiffEntityParts() {
            return diffEntityParts;
        }

        public int getMatch() {
            return match;
        }

    }

    private static class DiffFieldPart {

        private final String fieldName;

        private final String label;

        private final String val;

        public DiffFieldPart(String fieldName, String label, String val) {
            this.fieldName = fieldName;
            this.label = label;
            this.val = val;
        }

        public String getFieldName() {
            return fieldName;
        }

        public String getLabel() {
            return label;
        }

        public String getVal() {
            return val;
        }

    }
}
