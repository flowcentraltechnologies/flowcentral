package com.flowcentraltech.flowcentral.common.data;

/**
 * Generate listing report options
 * 
 * @author FlowCentral Technologies Limited
 * @since 4.1
 */
public class GenerateListingReportOptions {

    private FormListingOptions formListingOptions;

    private String optionsName;

    private String generator;

    public GenerateListingReportOptions(String optionsName, String generator, FormListingOptions formListingOptions) {
        this.optionsName = optionsName;
        this.generator = generator;
        this.formListingOptions = formListingOptions;
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
