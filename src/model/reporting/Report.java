package model.reporting;

import java.util.Date;
import java.util.List;
import utils.DateTimeSerialiser;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class Report {
    
    private Date startDate;
    private Date endDate;
    private List<Indicator> indicators;
    
    @JsonSerialize(using = DateTimeSerialiser.class)
    public Date getStartDate() {
        return startDate;
    }
    
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }
    
    @JsonSerialize(using = DateTimeSerialiser.class)
    public Date getEndDate() {
        return endDate;
    }
    
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
    
    public List<Indicator> getIndicators() {
        return indicators;
    }
    
    public void setIndicators(List<Indicator> indicators) {
        this.indicators = indicators;
    }

}
