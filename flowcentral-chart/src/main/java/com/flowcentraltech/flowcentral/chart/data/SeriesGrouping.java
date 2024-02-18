package com.flowcentraltech.flowcentral.chart.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Series.grouping
 * 
 * @author FlowCentral Technologies Limited
 * @since 1.0
 */
public class SeriesGrouping {

    private String name;

    private String label;
    
    private boolean date;
    
    private List<Object> grouping;

    public SeriesGrouping(String name, String label, boolean date) {
        this.name = name;
        this.label = label;
        this.date = date;
        this.grouping = new ArrayList<Object>();
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public boolean isDate() {
        return date;
    }
    
    public void addGrouping(Object grouping) {
        this.grouping.add(grouping);
    }
    
    public Date getGroupingAsDate(int index) {
        return (Date) this.grouping.get(index);
    }
    
    public String getGroupingAsString(int index) {
        return String.valueOf(this.grouping.get(index));
    }
}
