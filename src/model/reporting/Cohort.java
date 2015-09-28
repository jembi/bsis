package model.reporting;

public class Cohort {
    
    private String category;
    private Object option;
    private Comparator comparator;
    
    public String getCategory() {
        return category;
    }
    
    public void setCategory(String category) {
        this.category = category;
    }
    
    public Object getOption() {
        return option;
    }
    
    public void setOption(Object option) {
        this.option = option;
    }
    
    public Comparator getComparator() {
        return comparator;
    }
    
    public void setComparator(Comparator comparator) {
        this.comparator = comparator;
    }

}
