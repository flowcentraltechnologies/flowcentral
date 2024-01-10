package com.flowcentraltech.flowcentral.common.data;

/**
 * Generate listing report options
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class GenerateListingReportOptions {

    private FormListingOptions formListingOptions;

    private String generator;

    public GenerateListingReportOptions(String generator, FormListingOptions formListingOptions) {
        this.formListingOptions = formListingOptions;
        this.generator = generator;
    }

    public FormListingOptions getFormListingOptions() {
        return formListingOptions;
    }

    public String getGenerator() {
        return generator;
    }
}
