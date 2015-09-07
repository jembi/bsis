package helpers.builders;

import viewmodel.AdverseEventTypeViewModel;

public class AdverseEventTypeViewModelBuilder {
    
    private Long id;
    private String name;
    private String description;
    private boolean deleted;
    
    public AdverseEventTypeViewModelBuilder withId(long id) {
        this.id = id;
        return this;
    }
    
    public AdverseEventTypeViewModelBuilder withName(String name) {
        this.name = name;
        return this;
    }
    
    public AdverseEventTypeViewModelBuilder withDescription(String description) {
        this.description = description;
        return this;
    }
    
    public AdverseEventTypeViewModelBuilder thatIsDeleted() {
        deleted = true;
        return this;
    }

    public AdverseEventTypeViewModel build() {
        AdverseEventTypeViewModel viewModel = new AdverseEventTypeViewModel();
        viewModel.setId(id);
        viewModel.setName(name);
        viewModel.setDescription(description);
        viewModel.setDeleted(deleted);
        return viewModel;
    }
    
    public static AdverseEventTypeViewModelBuilder anAdverseEventTypeViewModel() {
        return new AdverseEventTypeViewModelBuilder();
    }

}
