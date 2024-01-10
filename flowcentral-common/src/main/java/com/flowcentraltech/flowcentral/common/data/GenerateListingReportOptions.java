package com.flowcentraltech.flowcentral.common.data;

/**
 * Generate listing report options
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class GenerateListingReportOptions {

    private FormListingOptions formListingOptions;

    private String optionsName;
    
    private String generator;

    public GenerateListingReportOptions(String optionsName, String generator, FormListingOptions formListingOptions) {
        this.optionsName = optionsName;
        this.generator = generator;
        this.formListingOptions = formListingOptions;
        formListingOptions.setOptionsName(optionsName);
    }

    public FormListingOptions getFormListingOptions() {
        return formListingOptions;
    }

    public String getGenerator() {
        return generator;
    }

    public String getOptionsName() {
        return optionsName;
    }
}
