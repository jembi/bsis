package backingform;

import javax.validation.constraints.NotNull;

import model.adverseevent.AdverseEvent;
import model.adverseevent.AdverseEventType;

public class AdverseEventBackingForm {
    
    private AdverseEvent adverseEvent;
    
    public AdverseEventBackingForm() {
        adverseEvent = new AdverseEvent();
    }
    
    public void setId(Long id) {
        adverseEvent.setId(id);
    }
    
    public Long getId() {
        return adverseEvent.getId();
    }
    
    @NotNull
    public void setType(AdverseEventType type) {
        adverseEvent.setType(type);
    }
    
    public AdverseEventType getType() {
        return adverseEvent.getType();
    }
    
    public void setComment(String comment) {
        adverseEvent.setComment(comment);
    }
    
    public String getComment() {
        return adverseEvent.getComment();
    }
}
