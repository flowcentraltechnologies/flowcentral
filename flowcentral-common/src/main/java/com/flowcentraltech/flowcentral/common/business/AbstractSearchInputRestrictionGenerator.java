package com.flowcentraltech.flowcentral.common.business;

import com.flowcentraltech.flowcentral.common.AbstractFlowCentralComponent;
import com.flowcentraltech.flowcentral.common.constants.SearchInputRestrictionDataType;
import com.tcdng.unify.core.UnifyException;

/**
 * Convenient abstract base class for flowCentral service.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractSearchInputRestrictionGenerator extends AbstractFlowCentralComponent
        implements SearchInputRestrictionGenerator {

    private final SearchInputRestrictionDataType inputType;
    
    public AbstractSearchInputRestrictionGenerator(SearchInputRestrictionDataType inputType) {
        this.inputType = inputType;
    }

    @Override
    public SearchInputRestrictionDataType getInputType() {
        return inputType;
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

}
