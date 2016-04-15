package viewmodel;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;

import model.dto.Report;
import model.dto.ReportRow;

public class ReportViewModel {
    
    @JsonIgnore
    private Report report;
    
    public ReportViewModel() {
        // no-args constructor
    }
    
    public ReportViewModel(Report report) {
        this.report = report;
    }
    
    public List<Map<String, Object>> getRows() {
        List<Map<String, Object>> rows = new ArrayList<Map<String,Object>>();
        
        for (ReportRow row : report.getRows()) {
            rows.add(row.getData());
        }

        return rows;
    }
    
}
