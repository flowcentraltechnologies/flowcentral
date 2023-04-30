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

package com.flowcentraltech.flowcentral.application.web.writers;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.flowcentraltech.flowcentral.application.business.AppletUtilities;
import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.application.constants.ApplicationModuleNameConstants;
import com.flowcentraltech.flowcentral.application.constants.ListingColorType;
import com.flowcentraltech.flowcentral.application.data.ListingProperties;
import com.flowcentraltech.flowcentral.application.data.ListingReportGeneratorProperties;
import com.flowcentraltech.flowcentral.application.data.ListingReportProperties;
import com.flowcentraltech.flowcentral.application.util.EntityImage;
import com.flowcentraltech.flowcentral.application.util.EntityImageUtils;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.flowcentraltech.flowcentral.common.data.FormListingOptions;
import com.flowcentraltech.flowcentral.configuration.xml.util.ConfigurationUtils;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.LocaleFactoryMap;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.core.report.Report;
import com.tcdng.unify.core.report.ReportLayoutType;
import com.tcdng.unify.core.resource.ImageProvider;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ThemeManager;
import com.tcdng.unify.web.ui.WebUIApplicationComponents;
import com.tcdng.unify.web.ui.widget.ResponseWriter;

/**
 * Convenient abstract base class for form listing generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractFormListingGenerator extends AbstractFormListingReportGenerator
        implements FormListingGenerator {

    protected enum AmountFormat {
        WHOLE_NUMBER,
        WITH_DECIMAL
    }

    @Configurable
    private AppletUtilities au;

    @Configurable
    private ThemeManager themeManager;

    @Configurable(ApplicationModuleNameConstants.ENTITY_IMAGE_PROVIDER)
    private ImageProvider entityImageProvider;

    private final LocaleFactoryMap<Formatter<?>> dateFormatterMap;

    private final LocaleFactoryMap<Formatter<?>> amountFormatterMap;

    private final LocaleFactoryMap<Formatter<?>> wholeAmountFormatterMap;

    private static String defaultListingReportStyle;

    public AbstractFormListingGenerator() {
        this.dateFormatterMap = new LocaleFactoryMap<Formatter<?>>()
            {
                @Override
                protected Formatter<?> create(Locale locale, Object... params) throws Exception {
                    return (Formatter<?>) getUplComponent(locale, "!fixeddatetimeformat pattern:$s{dd-MM-yyyy}", false);
                }

            };

        this.amountFormatterMap = new LocaleFactoryMap<Formatter<?>>()
            {

                @Override
                protected Formatter<?> create(Locale locale, Object... params) throws Exception {
                    return (Formatter<?>) getUplComponent(locale,
                            "!decimalformat precision:20 scale:2 useGrouping:true", false);
                }

            };

        this.wholeAmountFormatterMap = new LocaleFactoryMap<Formatter<?>>()
            {

                @Override
                protected Formatter<?> create(Locale locale, Object... params) throws Exception {
                    return (Formatter<?>) getUplComponent(locale,
                            "!decimalformat precision:20 scale:0 useGrouping:true", false);
                }

            };
    }

    public final void setAu(AppletUtilities au) {
        this.au = au;
    }

    public final void setThemeManager(ThemeManager themeManager) {
        this.themeManager = themeManager;
    }

    public final void setEntityImageProvider(ImageProvider entityImageProvider) {
        this.entityImageProvider = entityImageProvider;
    }

    @Override
    public final Report generateHtmlReport(ValueStore formBeanValueStore, FormListingOptions listingOptions)
            throws UnifyException {
        ResponseWriter writer = getComponent(ResponseWriter.class,
                WebUIApplicationComponents.APPLICATION_RESPONSEWRITER);
        ListingReportGeneratorProperties properties = getReportProperties(formBeanValueStore, listingOptions);
        Report.Builder rb = Report.newBuilder(ReportLayoutType.MULTIDOCHTML_PDF, properties.getReportPageProperties())
                .title("listingReport");
        Set<ListingColorType> pausePrintColors = getPausePrintColors();
        final String additional = additionalStyleClass() != null ? " " + additionalStyleClass() : "";
        for (ListingReportProperties listingReportProperties : properties.getReportProperties()) {
            writer.reset(Collections.emptyMap());
            writer.write("<div class=\"fc-formlisting");
            writer.write(additional);
            writer.write("\">");
            generateReportHeader(formBeanValueStore, listingReportProperties,
                    new HtmlListingGeneratorWriter(themeManager, entityImageProvider, listingReportProperties.getName(),
                            writer, pausePrintColors, false));
            writer.write("<div class=\"flbody\">");
            generateHtmlListing(listingReportProperties.getName(), formBeanValueStore, listingReportProperties, writer,
                    pausePrintColors, false);
            generateReportAddendum(formBeanValueStore, listingReportProperties,
                    new HtmlListingGeneratorWriter(themeManager, entityImageProvider, listingReportProperties.getName(),
                            writer, pausePrintColors, false));
            writer.write("</div>");
            generateReportFooter(formBeanValueStore, listingReportProperties,
                    new HtmlListingGeneratorWriter(themeManager, entityImageProvider, listingReportProperties.getName(),
                            writer, pausePrintColors, false));
            writer.write("</div>");
            String bodyContent = writer.toString();
            String style = listingReportProperties.getProperty(String.class, ListingReportProperties.PROPERTY_DOCSTYLE);
            if (style == null) {
                style = getDefaultListingStyle();
            }

            rb.addBodyContentHtml(listingReportProperties.getName(), style, bodyContent);
        }

        return rb.build();
    }

    @Override
    public Report generateExcelReport(ValueStore formBeanValueStore, FormListingOptions listingOptions)
            throws UnifyException {
        Workbook workbook = new HSSFWorkbook();
        ListingReportGeneratorProperties properties = getReportProperties(formBeanValueStore, listingOptions);
        Report.Builder rb = Report.newBuilder(ReportLayoutType.WORKBOOK_XLS, properties.getReportPageProperties())
                .title("listingReport");
        Set<ListingColorType> pausePrintColors = getPausePrintColors();
        for (ListingReportProperties listingReportProperties : properties.getReportProperties()) {
            Sheet sheet = workbook.createSheet(listingReportProperties.getName());
            ListingGeneratorWriter writer = new ExcelListingGeneratorWriter(themeManager, entityImageProvider,
                    listingReportProperties.getName(), sheet, pausePrintColors, false);
            generateReportHeader(formBeanValueStore, listingReportProperties, writer);
            doGenerate(formBeanValueStore, listingReportProperties, writer);
            generateReportAddendum(formBeanValueStore, listingReportProperties, writer);
            generateReportFooter(formBeanValueStore, listingReportProperties, writer);
            writer.close();
        }

        rb.customObject(workbook);
        return rb.build();
    }

    @Override
    public final void generateListing(ValueStore formBeanValueStore, ListingProperties listingProperties,
            ResponseWriter writer) throws UnifyException {
        generateHtmlListing("", formBeanValueStore, listingProperties, writer, Collections.emptySet(), true);
    }

    protected abstract Set<ListingColorType> getPausePrintColors() throws UnifyException;

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected EnvironmentService environment() {
        return au.environment();
    }

    protected ApplicationModuleService application() {
        return au.application();
    }

    protected String additionalStyleClass() {
        return null;
    }

    protected synchronized String retrieveFormattedDate(ValueStore valueStore, String propertyName)
            throws UnifyException {
        return valueStore.retrieve(String.class, propertyName, dateFormatterMap.get(getSessionLocale()));
    }

    protected synchronized String formattedDate(Date date) throws UnifyException {
        return DataUtils.convert(String.class, date, dateFormatterMap.get(getSessionLocale()));
    }

    protected synchronized String[] retrieveFormattedDates(ValueStore valueStore, String... propertyName)
            throws UnifyException {
        Formatter<?> formatter = dateFormatterMap.get(getSessionLocale());
        String[] result = new String[propertyName.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = valueStore.retrieve(String.class, propertyName[i], formatter);
        }
        return result;
    }

    protected synchronized String formattedAmount(AmountFormat format, BigDecimal amount) throws UnifyException {
        if (AmountFormat.WHOLE_NUMBER.equals(format)) {
            return DataUtils.convert(String.class, amount, wholeAmountFormatterMap.get(getSessionLocale()));
        }

        return DataUtils.convert(String.class, amount, amountFormatterMap.get(getSessionLocale()));
    }

    protected synchronized String[] retrieveFormattedAmounts(AmountFormat format, ValueStore valueStore,
            String... propertyName) throws UnifyException {
        Formatter<?> formatter = AmountFormat.WHOLE_NUMBER.equals(format)
                ? wholeAmountFormatterMap.get(getSessionLocale())
                : amountFormatterMap.get(getSessionLocale());
        String[] result = new String[propertyName.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = valueStore.retrieve(String.class, propertyName[i], formatter);
        }
        return result;
    }

    protected synchronized String retrieveFormattedAmount(AmountFormat format, ValueStore valueStore,
            String propertyName) throws UnifyException {
        if (AmountFormat.WHOLE_NUMBER.equals(format)) {
            return valueStore.retrieve(String.class, propertyName, wholeAmountFormatterMap.get(getSessionLocale()));
        }

        return valueStore.retrieve(String.class, propertyName, amountFormatterMap.get(getSessionLocale()));
    }

    @SuppressWarnings("unchecked")
    protected <T> T[] retrieveValues(Class<T> typeClass, ValueStore valueStore, String... propertyNames)
            throws UnifyException {
        T[] values = (T[]) Array.newInstance(typeClass, propertyNames.length);
        for (int i = 0; i < values.length; i++) {
            values[i] = valueStore.retrieve(typeClass, propertyNames[i]);
        }

        return values;
    }

    protected String loadListingStyle(String resourceName) throws UnifyException {
        return ConfigurationUtils.readString(resourceName, getUnifyComponentContext().getWorkingPath());
    }

    protected final void writeSingleValue(ListingGeneratorWriter writer, ListingCellType cellType, Object val)
            throws UnifyException {
        ListingCell[] cell = new ListingCell[1];
        cell[0] = new ListingCell(cellType, String.valueOf(val));
        writer.writeRow(cell);
    }

    protected final void writeFileImage(ListingGeneratorWriter writer, String resourceName, String style)
            throws UnifyException {
        writer.writeRow(new ListingCell(ListingCellType.FILE_IMAGE, resourceName, style));
    }

    protected final void writeEntityImage(ListingGeneratorWriter writer, String entity, Long instId, String fieldName,
            String style) throws UnifyException {
        writeEntityImage(writer, new EntityImage(entity, instId, fieldName), style);
    }

    protected final void writeEntityImage(ListingGeneratorWriter writer, EntityImage entityImage, String style)
            throws UnifyException {
        final String resourceName = EntityImageUtils.encode(entityImage);
        writer.writeRow(new ListingCell(ListingCellType.ENTITY_PROVIDER_IMAGE, resourceName, style));
    }

    protected final void writeProviderImage(ListingGeneratorWriter writer, String resourceName, String style)
            throws UnifyException {
        writer.writeRow(new ListingCell(ListingCellType.ENTITY_PROVIDER_IMAGE, resourceName, style));
    }

    private void generateHtmlListing(final String listingType, ValueStore formBeanValueStore,
            ListingProperties listingProperties, ResponseWriter writer, Set<ListingColorType> pausePrintColors,
            boolean highlighting) throws UnifyException {
        ListingGeneratorWriter generator = new HtmlListingGeneratorWriter(themeManager, entityImageProvider,
                listingType, writer, pausePrintColors, highlighting);
        doGenerate(formBeanValueStore, listingProperties, generator);
        generator.close();
    }

    private String getDefaultListingStyle() throws UnifyException {
        if (defaultListingReportStyle == null) {
            synchronized (AbstractFormListingGenerator.class) {
                if (defaultListingReportStyle == null) {
                    defaultListingReportStyle = loadListingStyle("web/themes/farko/css/flowcentral-listingreport.css");
                }
            }
        }

        return defaultListingReportStyle;
    }

    protected abstract void doGenerate(ValueStore formBeanValueStore, ListingProperties listingProperties,
            ListingGeneratorWriter writer) throws UnifyException;
}
