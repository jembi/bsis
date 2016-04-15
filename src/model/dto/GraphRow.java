package model.dto;

import java.util.HashMap;
import java.util.Map;

public class GraphRow implements ReportRow {
    
    private String label;
    private String series;
    private Number value;
    
    public GraphRow() {
        // no-args constructor
    }
    
    public GraphRow(String label, String series, Number value) {
        this.label = label;
        this.series = series;
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getSeries() {
        return series;
    }

    public void setSeries(String series) {
        this.series = series;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    @Override
    public Map<String, Object> getData() {
        Map<String, Object> data = new HashMap<>();
        data.put("label", label);
        data.put("series", series);
        data.put("value", value);
        return data;
    }

}
