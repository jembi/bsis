package helpers.builders;

import model.audit.EntityModification;
import model.user.User;
import viewmodel.AuditRevisionViewModel;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class AuditRevisionViewModelBuilder {

  private int id;
  private Date revisionDate;
  private User user;
  private Set<EntityModification> entityModifications = new HashSet<>();

  public static AuditRevisionViewModelBuilder anAuditRevisionViewModel() {
    return new AuditRevisionViewModelBuilder();
  }

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

  public AuditRevisionViewModelBuilder withEntityModifications(Set<EntityModification> entityModifications) {
    this.entityModifications = entityModifications;
    return this;
  }

  public AuditRevisionViewModel build() {
    AuditRevisionViewModel viewModel = new AuditRevisionViewModel();
    viewModel.setId(id);
    viewModel.setRevisionDate(revisionDate);
    viewModel.setUser(user);
    viewModel.setEntityModifications(entityModifications);
    return viewModel;
  }

}
