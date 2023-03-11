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
package com.flowcentraltech.flowcentral.application.util;

/**
 * Widget calculation utilities.
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public final class WidgetCalculationUtils {

    private WidgetCalculationUtils() {

    }

    public static int[] shareAsEqualAsPossible(int quantityToShare, int noOfSlots) {
        if (quantityToShare < 0) {
            throw new IllegalArgumentException("Quantity to share must be greater than or equal to zero.");
        }

        if (noOfSlots <= 0) {
            throw new IllegalArgumentException("Number of slots must be greater than zero.");
        }

        int[] result = new int[noOfSlots];
        int common = quantityToShare / noOfSlots;
        int remainder = quantityToShare % noOfSlots;
        for (int i = 0; i < noOfSlots; i++) {
            if (remainder > 0) {
                result[i] = common + 1;
                remainder--;
            } else {
                result[i] = common;
            }
        }

        return result;
    }

    public static int[] shareAsEqualAsPossible(int quantityToShare, int[] slotRatios) {
        if (quantityToShare < 0) {
            throw new IllegalArgumentException("Quantity to share must be greater than or equal to zero.");
        }

        if (slotRatios == null || slotRatios.length <= 0) {
            throw new IllegalArgumentException("Slot ratios is required and must have at least one item");
        }

        int[] result = new int[slotRatios.length];
        int totalRatios = 0;
        for (int i = 0; i < slotRatios.length; i++) {
            if (slotRatios[i] < 0) {
                throw new IllegalArgumentException("Slot ratio can not be negative");
            }

            totalRatios += slotRatios[i];
        }

        if (totalRatios == 0) {
            throw new IllegalArgumentException("Total ratios can not be zero.");
        }

        int used = 0;
        for (int i = 0; i < result.length; i++) {
            result[i] = (quantityToShare * slotRatios[i]) / totalRatios;
            used += result[i];
        }

        int remainder = quantityToShare - used;
        if (remainder > 0) {
            int[] remainders = WidgetCalculationUtils.shareAsEqualAsPossible(remainder, slotRatios.length);
            for (int i = 0; i <  slotRatios.length; i++) {
                result[i] = result[i] + remainders[i];
            }
        }
        
        return result;
    }
}
