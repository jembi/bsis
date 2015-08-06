package helpers.builders;

import java.util.Date;

import model.user.User;
import viewmodel.AuditRevisionViewModel;

public class AuditRevisionViewModelBuilder {
    
    private int id;
    private Date revisionDate;
    private User user;

    public AuditRevisionViewModelBuilder withId(int id) {
        this.id = id;
        return this;
    }
    
    public AuditRevisionViewModelBuilder withRevisionDate(Date revisionDate) {
        this.revisionDate = revisionDate;
        return this;
    }
    
    public AuditRevisionViewModelBuilder withUser(User user) {
        this.user = user;
        return this;
    }
    
    public AuditRevisionViewModel build() {
        AuditRevisionViewModel viewModel = new AuditRevisionViewModel();
        viewModel.setId(id);
        viewModel.setRevisionDate(revisionDate);
        viewModel.setUser(user);
        return viewModel;
    }
    
    public static AuditRevisionViewModelBuilder anAuditRevisionViewModel() {
        return new AuditRevisionViewModelBuilder();
    }

}
