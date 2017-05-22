package org.jembi.bsis.viewmodel;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jembi.bsis.model.audit.EntityModification;
import org.jembi.bsis.utils.DateTimeSerialiser;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

public class AuditRevisionViewModel {

  private long id;
  private Date revisionDate;
  private UserViewModel user;
  private Set<EntityModification> entityModifications = new HashSet<>();

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @JsonSerialize(using = DateTimeSerialiser.class)
  public Date getRevisionDate() {
    return revisionDate;
  }

  public void setRevisionDate(Date revisionDate) {
    this.revisionDate = revisionDate;
  }

  public UserViewModel getUser() {
    return user;
  }

  public void setUser(UserViewModel user) {
    this.user = user;
  }

  public List<EntityModificationViewModel> getEntityModifications() {
    List<EntityModificationViewModel> viewModels = new ArrayList<>();
    for (EntityModification entityModification : entityModifications) {
      EntityModificationViewModel viewModel = new EntityModificationViewModel();
      viewModel.setId(entityModification.getId());
      viewModel.setRevisionType(entityModification.getRevisionType());
      viewModel.setEntityName(entityModification.getEntityName());
      viewModels.add(viewModel);
    }
    return viewModels;
  }

  public void setEntityModifications(Set<EntityModification> entityModifications) {
    this.entityModifications = entityModifications;
  }

}
