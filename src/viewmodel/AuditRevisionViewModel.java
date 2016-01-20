package viewmodel;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import model.audit.EntityModification;
import model.user.User;
import utils.DateTimeSerialiser;

import java.util.*;

public class AuditRevisionViewModel {

  private int id;
  private Date revisionDate;
  private User user;
  private Set<EntityModification> entityModifications = new HashSet<>();

  public int getId() {
    return id;
  }

  public void setId(int id) {
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
    if (user == null) {
      return null;
    }
    return new UserViewModel(user);
  }

  public void setUser(User user) {
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
