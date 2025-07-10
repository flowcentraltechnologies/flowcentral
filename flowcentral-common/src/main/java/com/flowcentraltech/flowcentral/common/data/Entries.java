package com.flowcentraltech.flowcentral.common.data;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

import com.tcdng.unify.core.criterion.Restriction;

public class Entries {

    private final Map<String, Object> inputs;

    private final Restriction restriction;

    public Entries(Map<String, Object> inputs, Restriction restriction) {
        this.inputs = inputs;
        this.restriction = restriction;
    }

    public Entries() {
        this.inputs = Collections.emptyMap();
        this.restriction = null;
    }

    public Map<String, Object> getInputs() {
        return inputs;
    }

    public Set<String> getInputNames() {
        return inputs != null ? inputs.keySet() : Collections.emptySet();
    }
    
    public Object getInput(String name) {
        return inputs != null ? inputs.get(name) : null;
    }
    
    public Restriction getRestriction() {
        return restriction;
    }

    public boolean isWithRestriction() {
        return restriction != null;
    }
}