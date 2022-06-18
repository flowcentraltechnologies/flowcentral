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

package com.flowcentraltech.flowcentral.application.web.writers;

import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Locale;

import com.flowcentraltech.flowcentral.application.business.ApplicationModuleService;
import com.flowcentraltech.flowcentral.common.business.EnvironmentService;
import com.tcdng.unify.core.AbstractUnifyComponent;
import com.tcdng.unify.core.UnifyException;
import com.tcdng.unify.core.annotation.Configurable;
import com.tcdng.unify.core.data.LocaleFactoryMap;
import com.tcdng.unify.core.data.ValueStore;
import com.tcdng.unify.core.format.Formatter;
import com.tcdng.unify.core.util.DataUtils;
import com.tcdng.unify.web.ui.widget.ResponseWriter;

/**
 * Convenient abstract base class for form listing generators.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public abstract class AbstractFormListingGenerator extends AbstractUnifyComponent implements FormListingGenerator {

    protected enum AmountFormat {
        WHOLE_NUMBER,
        WITH_DECIMAL
    }

    @Configurable
    private EnvironmentService environmentService;

    @Configurable
    private ApplicationModuleService applicationModuleService;

    private final LocaleFactoryMap<Formatter<?>> dateFormatterMap;

    private final LocaleFactoryMap<Formatter<?>> amountFormatterMap;

    private final LocaleFactoryMap<Formatter<?>> wholeAmountFormatterMap;

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
    
    public final void setApplicationModuleService(ApplicationModuleService applicationModuleService) {
        this.applicationModuleService = applicationModuleService;
    }
    
    public final void setEnvironmentService(EnvironmentService environmentService) {
        this.environmentService = environmentService;
    }

    @Override
    public void generate(ValueStore formBeanValueStore, ResponseWriter writer) throws UnifyException {
        doGenerate(formBeanValueStore, new ListingGeneratorWriter(writer));
    }

    @Override
    protected void onInitialize() throws UnifyException {

    }

    @Override
    protected void onTerminate() throws UnifyException {

    }

    protected EnvironmentService environment() {
        return environmentService;
    }

    protected ApplicationModuleService application() {
        return applicationModuleService;
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
   
    protected abstract void doGenerate(ValueStore formBeanValueStore, ListingGeneratorWriter writer)
            throws UnifyException;
}
