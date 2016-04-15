package model.dto;

import java.util.HashMap;
import java.util.Map;

public class TableRow implements ReportRow {
    
    private Map<String, Object> data;

    public void setData(Map<String, Object> data) {
        this.data = data;
    }
    
    public void addColumn(String name, Object value) {
        if (data == null) {
            data = new HashMap<>();
        }
        data.put(name, value);        
    }
    
    @Override
    public Map<String, Object> getData() {
        return data;
    }

}
