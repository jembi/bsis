package backingform;

import javax.validation.constraints.NotNull;

public class AdverseEventBackingForm {
    
    private Long id;
    private AdverseEventTypeBackingForm typeBackingForm;
    private String comment;
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getId() {
        return id;
    }
    
    @NotNull
    public void setType(AdverseEventTypeBackingForm typeBackingForm) {
        this.typeBackingForm = typeBackingForm;
    }
    
    public AdverseEventTypeBackingForm getType() {
        return typeBackingForm;
    }
    
    public void setComment(String comment) {
        this.comment = comment;
    }
    
    public String getComment() {
        return comment;
    }
}
