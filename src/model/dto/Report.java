package model.dto;

import java.util.ArrayList;
import java.util.List;

public class Report {
    
    private List<ReportRow> rows;

    public List<ReportRow> getRows() {
        return rows;
    }

    public void setRows(List<ReportRow> rows) {
        this.rows = rows;
    }
    
    public void addRow(ReportRow row) {
        if (rows == null) {
            rows = new ArrayList<>();
        }
        rows.add(row);
    }

}
