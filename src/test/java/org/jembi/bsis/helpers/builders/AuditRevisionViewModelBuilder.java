package org.jembi.bsis.helpers.builders;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.jembi.bsis.model.audit.EntityModification;
import org.jembi.bsis.model.user.User;
import org.jembi.bsis.viewmodel.AuditRevisionViewModel;

public class AuditRevisionViewModelBuilder extends AbstractBuilder<AuditRevisionViewModel> {

  private int id;
  private Date revisionDate;
  private User user;
  private Set<EntityModification> entityModifications = new HashSet<>();

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

  @Override
  public AuditRevisionViewModel build() {
    AuditRevisionViewModel viewModel = new AuditRevisionViewModel();
    viewModel.setId(id);
    viewModel.setRevisionDate(revisionDate);
    viewModel.setUser(user);
    viewModel.setEntityModifications(entityModifications);
    return viewModel;
  }

  public static AuditRevisionViewModelBuilder anAuditRevisionViewModel() {
    return new AuditRevisionViewModelBuilder();
  }

}
