package helpers.builders;

import backingform.AdverseEventBackingForm;
import backingform.AdverseEventTypeBackingForm;

public class AdverseEventBackingFormBuilder extends AbstractBuilder<AdverseEventBackingForm> {
    
    private Long id;
    private AdverseEventTypeBackingForm type;
    private String comment;

    public AdverseEventBackingFormBuilder withType(AdverseEventTypeBackingForm type) {
        this.type = type;
        return this;
    }
    
    public AdverseEventBackingFormBuilder withComment(String comment) {
        this.comment = comment;
        return this;
    }
    
    public AdverseEventBackingFormBuilder withId(Long id) {
        this.id = id;
        return this;
    }

    @Override
    public AdverseEventBackingForm build() {
        AdverseEventBackingForm adverseEventBackingForm = new AdverseEventBackingForm();
        adverseEventBackingForm.setId(id);
        adverseEventBackingForm.setType(type);
        adverseEventBackingForm.setComment(comment);
        return adverseEventBackingForm;
    }
    
    public static AdverseEventBackingFormBuilder anAdverseEventBackingForm() {
        return new AdverseEventBackingFormBuilder();
    }

}
